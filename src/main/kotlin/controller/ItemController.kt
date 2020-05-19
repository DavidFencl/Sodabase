package controller

import model.*
import tornadofx.Controller


internal object ItemController: Controller() {
    private val itemDatabase = CItemDatabase
    private val currentUser = CUserDatabase.currentUser

    init {
        itemDatabase.silentImportFromFile("items.txt")
    }
    fun tryAddItem(name: String, price: Int, othersPrice: Int, quantity: Int){
        itemDatabase.inputItem(currentUser?:CUser("Error by addition"),name,price,othersPrice,quantity)
    }
    fun tryWithdrawItem(ID: Int, quantity: Int):ReturnValues{
        return itemDatabase.extractItem(ID, quantity)
    }
    fun saveChanges(){
        itemDatabase.exportToFile("items.txt", currentUser?: CUser("Error by export"))
    }
}