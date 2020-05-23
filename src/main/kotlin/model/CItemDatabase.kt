package model

import java.io.File
import java.time.LocalDateTime
import java.util.*

object CItemDatabase {
    private var idCount = 1
    private val inventory: TreeMap<String, CItem> = sortedMapOf<String,CItem>() as TreeMap<String, CItem>

    init {
        silentImportFromFile("items.txt")
    }
    private fun addNewItem(item: CItem){
        // Update already existing item
        if(inventory[item.name] != null){
            item.quantity += inventory[item.name]?.quantity ?: 0
            inventory.remove(item.name)
            inventory[item.name] = item
        }
        // Insert new item
        else
            inventory[item.name] = item
    }

    fun contains(item: String): Boolean {
        if(inventory.containsKey(item))
            return true
        return false
    }
    private fun logExtract(user: CUser, item: CItem, num: Int) {
        val outputDir = File("./logs/")
        if (!outputDir.exists())
            outputDir.mkdir()
        val file = File("./logs/log.txt")
        file.appendText("[-] User ${user.name} extracted $num pieces of ${item.name} at ${LocalDateTime.now()}.\n" +
                "\tCurrent number of ${item.name} is ${item.quantity - num}.\n")
            if(user.vip)
                file.appendText("\tUser ${user.name} paid ${num * item.price}. His current account balance is ${user.balance - num * item.price}.\n\n")
            else
                file.appendText("\tUser ${user.name} paid ${num * item.otherPrice}. His current account balance is ${user.balance - num * item.otherPrice}.\n\n")
    }
    private fun logImport (user: CUser, item: CItem, num: Int) {
        val outputDir = File("./logs/")
        if (!outputDir.exists())
            outputDir.mkdir()
        val file = File("./logs/log.txt")
        file.appendText("[+] User ${user.name} inputed $num pieces of ${item.name} at ${LocalDateTime.now()}.\n\n")
    }
    fun inputItem(user: CUser, name: String, price: Int, othersPrice: Int, quantity: Int){
        val tempItem = CItem(idCount++, name, price, othersPrice, quantity)
        CUserDatabase.currentUser.changeBalance(price*quantity)
        addNewItem(tempItem)
        logImport(user,tempItem,quantity)
    }
    fun extractItem(ID: Int, quantity: Int): ReturnValues {
        val currentUser = CUserDatabase.currentUser
        if(currentUser.name=="EMPTY")
            return ReturnValues.BAD_USER
        val item = getItem(ID) ?: return ReturnValues.BAD_ID
        val retVal = checkOutItems(currentUser,ID, quantity)

        if (retVal == ReturnValues.OK) {
            if (currentUser.vip) {

                currentUser.balance -= quantity * item.price
            }
            else
                currentUser.balance -= quantity * item.otherPrice
        }
        return retVal
    }
    private fun checkOutItems(user: CUser, itemID: Int, num: Int): ReturnValues{
        val item = getItem(itemID)?:return ReturnValues.BAD_ID
        val quantity = item.quantity

        if(num > quantity)
            return ReturnValues.TOO_MANY
        logExtract(user, item, num)
        item.quantity-=num

        return ReturnValues.OK
    }
    private fun getItem(ID: Int?): CItem?{
        if(ID == null)
            return null
        val tempItem = null
        for((_,v) in inventory)
            if(v.ID==ID)
                return v
        return tempItem
    }
    fun exportToFile(filename: String, user: CUser){
        val outputDir = File("./data/")
        val pathname = "./data/$filename"
        if(!outputDir.exists())
            outputDir.mkdir()
        File(pathname).printWriter().use {out ->
            out.println("[CInventory:")
            inventory.forEach{
                out.println("\t{\n\t\"name\":\"${it.value.name}\",")
                out.println("\t\"price\":\"${it.value.price}\",")
                out.println("\t\"otherPrice\":\"${it.value.otherPrice}\",")
                out.println("\t\"quantity\":\"${it.value.quantity}\"\n\t}")
        }
            out.println("]")
            out.print("Last edited by: ${user.name} at ${LocalDateTime.now()}")
        }
    }
    fun getData(): List<CItem>{
        return inventory.values.toList()
    }
    private fun skipHead(line: String): Int{
        var i = 0
        while(line[i] != ':') {
            i++
        }
        // first occurrence of '"'
        i+=2
        return i
    }

    private fun silentImportFromFile(filename: String){
        val file = File("./data/$filename")
        if(file.exists()){
            val input = file.bufferedReader()
            var currentLine = input.readLine().toString()
            // Clear potential garbage before data
            while(!currentLine.contains("[CInventory:"))
                currentLine = input.readLine().toString()

            // Helper variables for parsing
            var name = ""
            var price = 0
            var otherPrice = 0
            var quantity = 0
            var count = 0
            // read until you reach end of file
            while(!currentLine.contains("]")){
                if(currentLine.contains("\"name\":")){
                    val i = skipHead(currentLine)
                    name = currentLine.subSequence(i, currentLine.length - 2).toString()
                }
                if(currentLine.contains("\"price\":")){
                    val i = skipHead(currentLine)
                    price = (currentLine.subSequence(i, currentLine.length - 2)).toString().toInt()
                }
                if(currentLine.contains("\"otherPrice\":")){
                    val i = skipHead(currentLine)
                    otherPrice = (currentLine.subSequence(i, currentLine.length - 2)).toString().toInt()
                }
                if(currentLine.contains("\"quantity\":")){
                    val i = skipHead(currentLine)
                    quantity = (currentLine.subSequence(i, currentLine.length - 1)).toString().toInt()
                }
                // whole item is parsed
                if(currentLine.contains("}")) {
                    addNewItem(CItem(idCount++, name, price, otherPrice, quantity))
                    count++
                }
                currentLine=input.readLine().toString()
            }
        }
        else{
            return
        }
    }
}