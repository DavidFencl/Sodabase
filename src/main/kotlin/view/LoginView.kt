package view

import controller.UserController
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.stage.StageStyle
import model.ReturnValues
import tornadofx.*

class LoginView : View("Log in"){
    private var name =  SimpleStringProperty()
    private var password = SimpleStringProperty()
    private val userController = UserController

    override val root =form{
        style{
            this.minWidth = Dimension(50.0, Dimension.LinearUnits.pc)
            this.minHeight = Dimension(20.0, Dimension.LinearUnits.pc)
        }
        fieldset ("Login", labelPosition = Orientation.VERTICAL){
            field("Name") {
                textfield(name)
            }
            field("Password") {
                passwordfield(password)
            }
            hbox (30) {
                button("Log in") {
                    action {
                        if (name.isNull.value || password.isNull.value)
                            println("Please fill both fields!")
                        else if (ReturnValues.OK == userController.loginAttempt(name.get(), password.get())) {
                            replaceWith(
                                MenuView::class,ViewTransition.Wipe(0.5.seconds, ViewTransition.Direction.DOWN)
                            )
                        } else {
                            find<LoginFailed>().openModal(StageStyle.DECORATED, block = true)
                        }
                    }
                }
                button("Exit") {
                    action {
                        close()
                    }
                }
            }
        }
    }
}


