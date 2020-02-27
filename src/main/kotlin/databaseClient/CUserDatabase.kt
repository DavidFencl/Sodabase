package databaseClient

import java.io.File
import java.time.LocalDateTime
import java.util.*

data class CUserDatabase(var userDatabase: TreeMap<String, CUser>){
    fun getUserByName(name: String): CUser?{
        if(userDatabase.containsKey(name))
            return userDatabase.getValue(name)
        return null
    }
    fun addUser(user: CUser){
        userDatabase[user.name] = user
    }
    fun importUser(): Boolean{
        println("Zadej jméno nového uživatele")
        val name = readLine().toString().toLowerCase()
        if(isPresent(name)) {
            println("Takový uživatel již existuje!")
            return false
        }
        else{
            println("Zadej počáteční stav konta")
            val ammount = readLine()?.toInt()
            if(ammount == null)
                println("Neplatná hodnota!")
            else{
                println("Je uživatel vip? Ano/Ne")
                val answer = readLine().toString().toLowerCase()
                if(answer == "ano")
                    addUser(CUser(name, ammount, "", true))
                else
                    addUser(CUser(name, ammount, "", false))
            }
        }
        return true
    }
    fun isPresent(name: String): Boolean{
        if(userDatabase.contains(name))
            return true
        return false
    }
    fun listUsers(){
        for ((_, value) in userDatabase)
            println("CUser(name=${value.name}, balance=${value.balance}, vip=${value.vip}, admin=${value.admin})")
    }
    fun changeBalance(): Boolean{
        cleanConsole(3)
        println("Komu?")
        val name = readLine().toString().toLowerCase()
        val tempUser = getUserByName(name)
        if(null != tempUser) {
            println("Kolik chceš přidat?")
            val amount = readLine()?.toInt()
            tempUser.balance += amount?:0
        }
        else {
            println("Takový uživatel neexistuje!")
            return false
        }
        return true
    }
    fun exportToFile(filename: String, user: CUser){
        val outputDir = File("./data/")
        val pathname = "./data/$filename"
        if(!outputDir.exists())
            outputDir.mkdir()
        File(pathname).printWriter().use {out ->
            out.println("[CUserDatabase:")
            userDatabase.forEach{
                // Dont export admins!
                if(!it.value.admin) {
                    out.println("\t{\n\t\"name\":\"${it.value.name}\",")
                    out.println("\t\"balance\":\"${it.value.balance}\",")
                    out.println("\t\"vip\":\"${it.value.vip}\"\n\t}")
                }
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
            while(!currentLine.contains("[CUserDatabase:"))
                currentLine = input.readLine().toString()

            // Helper variables for parsing
            var name = ""
            var balance = 0
            var vip = false

            // read until you reach end of file
            while(!currentLine.contains("]")){
                if(currentLine.contains("\"name\":")){
                    val i = skipHead(currentLine)
                    name = currentLine.subSequence(i, currentLine.length - 2).toString()
                }
                if(currentLine.contains("\"balance\":")){
                    val i = skipHead(currentLine)
                    balance = (currentLine.subSequence(i, currentLine.length - 2)).toString().toInt()
                }
                if(currentLine.contains("\"vip\":")){
                    val i = skipHead(currentLine)
                    vip = (currentLine.subSequence(i, currentLine.length - 2)).toString().toBoolean()
                }
                // whole item is parsed
                if(currentLine.contains("}"))
                    userDatabase[name] = CUser(name, balance, "", vip)
                currentLine=input.readLine().toString()
            }
        }
        else{
            println("Takový soubor neexistuje!")
            return
        }
    }
    fun silentImportFromFile(filename: String){
        val file = File("./data/$filename")
        if(file.exists()){
            val input = file.bufferedReader()
            var currentLine = input.readLine().toString()
            // Clear potential garbage before data
            while(!currentLine.contains("[CUserDatabase:"))
                currentLine = input.readLine().toString()
            // Helper variables for parsing
            var name = ""
            var balance = 0
            var vip = false

            // read until you reach end of file
            while(!currentLine.contains("]")){
                if(currentLine.contains("\"name\":")){
                    val i = skipHead(currentLine)
                    name = currentLine.subSequence(i, currentLine.length - 2).toString()
                }
                if(currentLine.contains("\"balance\":")){
                    val i = skipHead(currentLine)
                    balance = (currentLine.subSequence(i, currentLine.length - 2)).toString().toInt()
                }
                if(currentLine.contains("\"vip\":")){
                    val i = skipHead(currentLine)
                    vip = (currentLine.subSequence(i, currentLine.length - 2)).toString().toBoolean()
                }
                // whole item is parsed
                if(currentLine.contains("}"))
                    userDatabase[name] = CUser(name, balance, "", vip)
                currentLine=input.readLine().toString()
            }
        }
        else{
            return
        }
    }
}