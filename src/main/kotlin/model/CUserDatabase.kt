package model

import java.io.File
import java.time.LocalDateTime
import java.util.*


object CUserDatabase{
    private val userDatabase: TreeMap<String, CUser> = sortedMapOf<String,CUser>() as TreeMap<String, CUser>
    private val encryptor = PasswordEncrypt
    lateinit var currentUser : CUser

    init{
        addUser(CUser("praja",200,"test",true))
        silentImportFromFile("users.txt")
    }

    private fun addUser(user: CUser){
        userDatabase[user.name] = user
    }

    fun safeAddUser(name: String, password1: String, password2: String, balance: Int, vip: Boolean): ReturnValues{
        if(isPresent(name))
            return ReturnValues.BAD_USER
        if(password1 != password2)
            return ReturnValues.PASSWORD_MISMATCH
        addUser(CUser(name,balance,password1,vip))
        return ReturnValues.OK
    }

    fun authenticateUser(name: String, password: String): ReturnValues{
        if(!isPresent(name))
            return ReturnValues.BAD_USER
        if(userDatabase[name]?.password != password)
            return ReturnValues.BAD_USER
        currentUser = userDatabase[name]?:CUser("EMPTY",vip = false)
        return ReturnValues.OK
    }
    private fun isPresent(name: String): Boolean{
        if(userDatabase.contains(name))
            return true
        return false
    }

    fun exportToFile(filename: String, user: CUser){
        val outputDir = File("./data/")
        val pathname = "./data/$filename"
        if(!outputDir.exists())
            outputDir.mkdir()
        File(pathname).printWriter().use {out ->
            out.println("[CUserDatabase:")
            userDatabase.forEach{
                out.println("\t{\n\t\"name\":\"${it.value.name}\",")
                out.println("\t\"password\":\"${encryptor.encrypt(it.value.password)}\",")
                out.println("\t\"balance\":\"${it.value.balance}\",")
                out.println("\t\"vip\":\"${it.value.vip}\"\n\t}")
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
            var password = ""
            var balance = 0
            var vip = false

            // read until you reach end of file
            while(!currentLine.contains("]")){
                if(currentLine.contains("\"name\":")){
                    val i = skipHead(currentLine)
                    name = currentLine.subSequence(i, currentLine.length - 2).toString()
                }
                if(currentLine.contains("\"password\":")){
                    val i = skipHead(currentLine)
                    password = (currentLine.subSequence(i, currentLine.length - 2)).toString()
                }
                if(currentLine.contains("\"balance\":")){
                    val i = skipHead(currentLine)
                    balance = (currentLine.subSequence(i, currentLine.length - 2)).toString().toInt()
                }
                if(currentLine.contains("\"vip\":")){
                    val i = skipHead(currentLine)
                    vip = (currentLine.subSequence(i, currentLine.length - 1)).toString().toBoolean()
                }
                // whole item is parsed
                if(currentLine.contains("}"))
                    userDatabase[name] = CUser(name, balance, encryptor.decrypt(password)?:return, vip)
                currentLine=input.readLine().toString()
            }
        }
        else{
            return
        }
    }
}