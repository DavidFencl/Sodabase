package view

import controller.UserController
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.stage.StageStyle
import model.ReturnValues
import tornadofx.*

class UserAdditionView : View("Sodabase - Add user"){
    private var userName =  SimpleStringProperty()
    private var password1 =  SimpleStringProperty()
    private var password2 =  SimpleStringProperty()
    private var initialBalance =  SimpleIntegerProperty()
    private var vipStatus =  SimpleBooleanProperty()

    override val root = form{
        fieldset ("Add user", labelPosition = Orientation.VERTICAL){
            field("User name") {
                textfield(userName)
            }
            field("User password") {
                passwordfield(password1)
            }
            field("Confirm password") {
                passwordfield(password2)
            }
                field("Initial balance") {
                    hbox {
                        textfield(initialBalance) {
                        filterInput { it.controlNewText.isInt() }
                    }
                        checkbox("VIP",vipStatus)
                    }
            }
            hbox (30) {
                button("Add user"){
                    shortcut("Enter")
                    action {
                        if(userName.get() != "" && password1.get() != "" && password2.get() != "") {
                            when(UserController.registerUser(userName.get(),password1.get(), password2.get(),initialBalance.get(),vipStatus.get())){
                                ReturnValues.PASSWORD_MISMATCH -> find<PasswordMismatch>().openModal(StageStyle.DECORATED)
                                ReturnValues.BAD_USER -> find<ExistingUser>().openModal(StageStyle.DECORATED)
                                else -> {
                                    find<ImportOk>().openModal(StageStyle.DECORATED)
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
        userName.set("")
        password1.set("")
        password2.set("")
        initialBalance.set(0)
        vipStatus.set(false)
    }
}