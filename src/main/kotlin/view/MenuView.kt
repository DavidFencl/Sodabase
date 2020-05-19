package view

import controller.ItemController
import controller.UserController
import javafx.geometry.Pos
import model.CItem
import model.CItemDatabase
import tornadofx.*

class MenuView : View("Sodabase"){
    private val menu: ButtonMenu by inject()
    private val itemDBPreview: ItemDatabaseView by inject()

    override val root = borderpane{
        left=menu.root
        center=itemDBPreview.root
    }

}

class ButtonMenu : View(){
    private val userController = UserController
    private val itemController = ItemController
    override val root = borderpane() {
        center {
            vbox (10,Pos.BASELINE_CENTER){
                label("Menu")
                button("Add item"){
                    action {
                        replaceWith(ItemImportView::class,ViewTransition.Wipe(0.5.seconds, ViewTransition.Direction.RIGHT))
                    }
                }
                button("Withdraw item"){
                    action {
                        replaceWith(ItemWithdrawalView::class,ViewTransition.Wipe(0.5.seconds, ViewTransition.Direction.RIGHT))
                    }
                }
                button("Button 3")
                button("Button 4")
            }
        }
        bottom {
            hbox (10,Pos.BASELINE_CENTER){
                button("Exit") {
                    action {
                        userController.saveChanges()
                        itemController.saveChanges()
                        close()
                    }
                }
            }
        }
    }
}

class ItemDatabaseView: View() {
    private val itemDatabase = CItemDatabase
    private val items = itemDatabase.getData().asObservable()

    override val root = tableview(items) {
        readonlyColumn("ID", CItem::ID)
        readonlyColumn("Name", CItem::name)
        readonlyColumn("Friends price", CItem::price)
        readonlyColumn("Others price", CItem::otherPrice)
        readonlyColumn("Quantity", CItem::quantity)
    }
}