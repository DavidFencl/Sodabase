package controller

import model.CUserDatabase
import model.ReturnValues
import tornadofx.*

internal object UserController: Controller() {

    fun loginAttempt(name: String, password: String):ReturnValues{
        return CUserDatabase.authenticateUser(name,password)
    }

    fun saveChanges(){
        CUserDatabase.exportToFile("users.txt", CUserDatabase.currentUser)
    }

    fun registerUser(name: String, password1: String, password2: String, balance: Int, vip: Boolean):ReturnValues{
        return CUserDatabase.safeAddUser(name,password1,password2,balance, vip)
    }
}