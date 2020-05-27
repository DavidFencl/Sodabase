package view

import controller.ItemController
import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Orientation
import javafx.stage.StageStyle
import model.CUserDatabase.currentUser
import model.ReturnValues

import tornadofx.*

class ItemWithdrawalView : View("Sodabase - Withdraw item") {
    private var itemID =  SimpleIntegerProperty()
    private var itemQuantity =  SimpleIntegerProperty()

    override val root = form{
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
            label("Current user balance: ${currentUser.balance}")
            hbox (30) {
                button("Withdraw item"){
                    shortcut("Enter")
                    action {
                        if(itemID.get() != 0 && itemQuantity.get() != 0){
                            when(ItemController.tryWithdrawItem(itemID.get(), itemQuantity.get())){
                                ReturnValues.BAD_ID -> find<BadItemId>().openModal(StageStyle.DECORATED)
                                ReturnValues.TOO_MANY -> find<TooManyItems>().openModal(StageStyle.DECORATED)
                                else -> {
                                    find<WithdrawalOk>().openModal(StageStyle.DECORATED)
                                    replaceWith(ButtonMenu::class,ViewTransition.Wipe(0.5.seconds, ViewTransition.Direction.LEFT))
                                }
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
    override fun onUndock(){
        itemID.set(0)
        itemQuantity.set(0)
    }
}