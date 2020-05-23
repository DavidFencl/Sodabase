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

    override val root =form{
        addClass(MyStylesheet.loginScreen)
        fieldset ("Login", labelPosition = Orientation.VERTICAL){
            field("Name") {
                textfield(name)
            }
            field("Password") {
                passwordfield(password)
            }
            hbox (30) {
                button("Log in") {
                    shortcut("Enter")
                    action {
                        if (name.isNull.value || password.isNull.value)
                            println("Please fill both fields!")
                        else if (ReturnValues.OK == UserController.loginAttempt(name.get(), password.get())) {
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


