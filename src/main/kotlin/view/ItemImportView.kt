package view

import controller.ItemController
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.stage.StageStyle

import tornadofx.*

class ItemImportView :View("Sodabase - Input item") {
    private var itemName =  SimpleStringProperty()
    private var itemPrice =  SimpleIntegerProperty()
    private var otherPrice =  SimpleIntegerProperty()
    private var itemQuantity =  SimpleIntegerProperty()

    override val root = form{
        fieldset ("Add item", labelPosition = Orientation.VERTICAL){
            field("Item name") {
                textfield(itemName)
            }
            field("Item price") {
                textfield(itemPrice){
                    filterInput { it.controlNewText.isInt() }
                }
            }
            field("Item price for others") {
                textfield(otherPrice){
                    filterInput { it.controlNewText.isInt() }
                }
            }
            field("Item quantity") {
                textfield(itemQuantity){
                    filterInput { it.controlNewText.isInt() }
                }
            }
            hbox (30) {
                button("Add item"){
                    shortcut("Enter")
                    action {
                        if(itemName.get() != "" && itemPrice.get() != 0 && otherPrice.get() != 0 && itemQuantity.get() != 0) {
                            ItemController.tryAddItem(itemName.get(),itemPrice.get(),otherPrice.get(),itemQuantity.get())
                            find<ImportOk>().openModal(StageStyle.DECORATED, block = true)
                            replaceWith(ButtonMenu::class,ViewTransition.Wipe(0.5.seconds, ViewTransition.Direction.LEFT))
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
            itemName.set("")
            itemPrice.set(0)
            otherPrice.set(0)
            itemQuantity.set(0)
        }
    }