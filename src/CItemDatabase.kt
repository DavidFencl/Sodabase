import java.io.File
import java.time.LocalDateTime

data class CItemDatabase (val inventory: HashMap<String, CItem>){
    fun addNewItem(item: CItem){
        // Update already existing item
        if(inventory[item.name] != null){
            inventory.remove(item.name)
            inventory[item.name] = item
        }
        // Insert new item
        else
            inventory[item.name] = item
    }
    fun listAll(){
        for ((k,v) in inventory)
            println("Inventory contains: $v")
    }
    fun listAllWithTabs(){
        for ((k,v) in inventory)
            println("\t\tInventory contains: $v")
    }
    fun contains(item: String): Boolean {
        if(inventory.containsKey(item))
            return true
        return false
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
            sum = sum-num
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
        val file = File(filename)
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
                    addNewItem(CItem(name,price,otherPrice,quantity))
                    count++
                }
                currentLine=input.readLine().toString()
            }
            cleanConsole(5)
            println("Import proběhl v pořádku - bylo importováno $count položek.")
            cleanConsole(3)
        }
        else{
            println("Takový soubor neexistuje!")
            return
        }
    }
}