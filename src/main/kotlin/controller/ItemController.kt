package controller

import model.*
import tornadofx.Controller


internal object ItemController: Controller() {
    fun tryAddItem(name: String, price: Int, othersPrice: Int, quantity: Int){
        CItemDatabase.inputItem(CUserDatabase.currentUser,name,price,othersPrice,quantity)
    }
    fun tryWithdrawItem(ID: Int, quantity: Int):ReturnValues{
        return CItemDatabase.extractItem(ID, quantity)
    }
    fun saveChanges(){
        CItemDatabase.exportToFile("items.txt", CUserDatabase.currentUser)
    }
}