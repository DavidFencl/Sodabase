package controller


import model.CUser
import model.CUserDatabase
import model.ReturnValues
import tornadofx.*
import java.util.*

internal object UserController: Controller() {
    private val userDatabase = CUserDatabase
    init {
        userDatabase.silentImportFromFile("users.txt")
    }

    fun loginAttempt(name: String, password: String):ReturnValues{
        return userDatabase.authenticateUser(name,password)
    }

    fun saveChanges(){
        userDatabase.exportToFile("users.txt", userDatabase.currentUser?: CUser("Error by export"))
    }
}