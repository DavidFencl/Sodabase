package databaseClient

import java.io.File
import java.time.LocalDateTime
import java.util.*

data class CItemDatabase (val inventory: TreeMap<String, CItem>){
    companion object{
        var idCount = 0
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
    fun listAll(){
        for ((_,v) in inventory)
            println("Inventory contains: $v")
    }
    fun listAllWithTabs(){
        for ((_,v) in inventory)
            println("\t\tInventory contains: $v")
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
        file.appendText("[+] User ${user.name} inputed $num pieces of ${item.name} at ${LocalDateTime.now()}.\n")
    }
    fun inputItem(user: CUser): Boolean{
        println("Zadejte jméno položky:")
        val name = readLine().toString().toLowerCase()
        println("Zadejte cenu pro kamarády:")
        val price = readLine()?.toInt()
        println("Zadejte cenu pro ostatní kolegy:")
        val othersPrice = readLine()?.toInt()
        println("Zadejte počet kusů:")
        val quantity = readLine()?.toInt()
        if(name == "" || null == price || null == othersPrice || null == quantity)
            return false
        val tempItem = CItem(idCount++, name, price, othersPrice, quantity)
        addNewItem(tempItem)
        logImport(user,tempItem,quantity)
        return true
    }
    fun extractItem(userDatabase: CUserDatabase): Boolean {
        cleanConsole(20)
        println("Kdo?")
        val name = readLine().toString().toLowerCase()
        val currentUser: CUser? = userDatabase.getUserByName(name)
        if (null == currentUser) {
            println("Takový uživatel neexistuje")
            return false
        } else {
            println("Co si bere ze skladu?")
            println("\tSklad obsahuje:")
            listAllWithTabs()
            println()
            val itemID = readLine()?.toInt() ?: return false
            val item = getItem(itemID)
            if (item != null) {
                println("Kolik?")
                val quantity = readLine()?.toInt() ?: return false
                if (checkOutItems(item.name, quantity)) {
                    val userBalance = currentUser.balance
                    if (currentUser.vip) {
                        if (userBalance < (quantity * item.price)) {
                            println("Pozor! Jsi v mínusu!")
                        }
                        currentUser.balance = userBalance - quantity * item.price
                    } else {
                        if (userBalance < (quantity * item.price)) {
                            println("Pozor! Jsi v mínusu!")
                        }
                        currentUser.balance = userBalance - quantity * item.price
                    }
                }
            } else {
                println("To ve skladu nemáme.")
                return false
            }
            return true
        }
    }
    fun checkOutItems(itemName: String, num: Int): Boolean{
        val item = inventory[itemName]
        if (item == null){
            println("Taková věc ve skladu není!")
            return false
        }
        var sum = item.quantity
        if(sum == 0)
            println("Ve skladu už ${item.name} není :(")
        else {
            if(num > sum) {
                println("To je moc!")
                return false
            }
            item.quantity=sum-num
            sum -= num
            if(sum<5)
                println("!!!! POZOR !!!! - Zbývá pouze $sum kusů")
            println("${item.name} byl odebrán. Nový počet kusů je $sum")
        }
        return true
    }
    fun checkOutItems(user: CUser, itemID: Int?, num: Int): Boolean{
        if(itemID == null)
            return false
        val item = getItem(itemID)
        if (item == null){
            println("Taková věc ve skladu není!")
            return false
        }
        var sum = item.quantity
        if(sum == 0)
            println("Ve skladu už ${item.name} není :(")
        else {
            if(num > sum) {
                println("To je moc!")
                return false
            }
            logExtract(user, item, num)
            item.quantity=sum-num
            sum -= num
            if(sum<5)
                println("!!!! POZOR !!!! - Zbývá pouze $sum kusů")
            println("${item.name} byl odebrán. Nový počet kusů je $sum")
        }
        return true
    }
    fun getItem(name: String): CItem?{
        if(inventory.containsKey(name))
            return inventory.getValue(name)
        return null
    }
    fun getItem(ID: Int?): CItem?{
        if(ID == null)
            return null
        val tempItem = null
        for((_,v) in inventory)
            if(v.ID==ID)
                return v
        return tempItem
    }
    fun restock(items: Set<CItem>){
        items.forEach{
            if(inventory.containsKey(it.name))
                inventory.getValue(it.name).quantity = inventory.getValue(it.name).quantity + it.quantity
            else
                inventory[it.name] = it
        }
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
    private fun skipHead(line: String): Int{
        var i = 0
        while(line[i] != ':') {
            i++
        }
        // first occurrence of '"'
        i+=2
        return i
    }
    fun importFromFile(filename: String){
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
            cleanConsole(5)
            println("Import položek proběhl v pořádku - bylo importováno $count položek.")
            cleanConsole(3)
        }
        else{
            println("Takový soubor neexistuje!")
            return
        }
    }
}