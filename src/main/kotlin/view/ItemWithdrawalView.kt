package view

import controller.ItemController
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.stage.StageStyle
import model.CUserDatabase.currentUser
import model.ReturnValues

import tornadofx.*

class ItemWithdrawalView : View("Sodabase - Withdraw item") {
    private var itemID =  SimpleIntegerProperty()
    private var itemQuantity =  SimpleIntegerProperty()
    private val itemController = ItemController


    override val root = form(){
        fieldset ("Withdraw item", labelPosition = Orientation.VERTICAL){
                field("Item ID") {
                    textfield(itemID) {
                        filterInput { it.controlNewText.isInt() }
                    }
                }
                field("Item quantity") {
                    textfield(itemQuantity) {
                        filterInput { it.controlNewText.isInt() }
                    }
                }
            label("Current user balance: ${currentUser?.balance}")
            hbox (30) {
                button("Withdraw item"){
                    action {
                        if(itemID.get() != 0 && itemQuantity.get() != 0){
                            itemController.tryWithdrawItem(itemID.get(), itemQuantity.get())
                            when(itemController.tryWithdrawItem(itemID.get(), itemQuantity.get())){
                                ReturnValues.BAD_ID -> find<BadItemId>().openModal(StageStyle.DECORATED, block = true)
                                ReturnValues.TOO_MANY -> find<TooManyItems>().openModal(StageStyle.DECORATED, block = true)
                                ReturnValues.OK -> TODO()

                            }
                        }
                    }
                }
                button("Back"){
                    action {
                        replaceWith(ButtonMenu::class,ViewTransition.Wipe(0.5.seconds, ViewTransition.Direction.LEFT))
                    }
                }
            }
        }
    }
}