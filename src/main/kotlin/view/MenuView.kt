package view

import controller.ItemController
import controller.UserController
import javafx.geometry.Pos
import javafx.scene.control.TableView
import model.CItem
import model.CItemDatabase
import model.CUserDatabase
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
    override val root = borderpane() {
        addClass(MyStylesheet.menu)
        center {
            vbox (10,Pos.BASELINE_CENTER){
                label("Menu"){
                    addClass(MyStylesheet.menuLabel)
                }
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
                button("Add user"){
                    action{
                        replaceWith(UserAdditionView::class,ViewTransition.Wipe(0.5.seconds, ViewTransition.Direction.RIGHT))
                    }
                }
                button("Button 4")
            }
        }
        bottom {
            hbox (10,Pos.BASELINE_CENTER){
                button("Exit") {
                    action {
                        UserController.saveChanges()
                        ItemController.saveChanges()
                        close()
                    }
                }
            }
        }
    }
}

class ItemDatabaseView: View() {
    override val root = tableview(CItemDatabase.getData().asObservable()) {
        addClass(MyStylesheet.itemTable)
        readonlyColumn("ID", CItem::ID)
        readonlyColumn("Name", CItem::name)
        readonlyColumn("Friends price", CItem::price)
        readonlyColumn("Others price", CItem::otherPrice)
        readonlyColumn("Quantity", CItem::quantity)

       // smartResize()
    }

}