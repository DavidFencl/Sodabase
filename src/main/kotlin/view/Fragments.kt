package view

import javafx.geometry.Pos
import javafx.scene.paint.Color
import model.CUserDatabase
import tornadofx.*

class LoginFailed: Fragment(){
    override val root = vbox(20, Pos.BASELINE_CENTER){
        addClass(MyStylesheet.infoFragment)
        label("Invalid username or password!")
        button("Ok"){
            action {
                close()
            }
        }
    }
}

class BadItemId: Fragment(){
    override val root = vbox(20, Pos.BASELINE_CENTER){
        addClass(MyStylesheet.infoFragment)
        label("Bad item ID!")
        button("Ok"){
            action {
                close()
            }
        }
    }
}

class TooManyItems: Fragment("Too many items!"){
    override val root = vbox(20, Pos.BASELINE_CENTER){
        addClass(MyStylesheet.infoFragment)
        label("That's too many!!")
        button("Ok"){
            action {
                close()
            }
        }
    }
}

class WithdrawalOk: Fragment("Complete"){
    private val currentUser = CUserDatabase.currentUser
    override val root = vbox(20, Pos.BASELINE_CENTER){
        addClass(MyStylesheet.infoFragment)
        label("Withdrawal complete - your current balance is:")
        if(currentUser.balance<=0)
            label("${currentUser.balance}") {
                style{
                    textFill=Color.RED
                }
            }
        else
            label("${currentUser.balance}")
        button("Ok"){
            action {
                close()
            }
        }
    }
}

class ImportOk: Fragment("Complete"){
    override val root = vbox(20, Pos.BASELINE_CENTER){
        addClass(MyStylesheet.infoFragment)
        label("Import complete!")

        button("Ok"){
            action {
                close()
            }
        }
    }
}

class PasswordMismatch: Fragment("Passwords don't match!"){
    override val root = vbox(20, Pos.BASELINE_CENTER){
        addClass(MyStylesheet.infoFragment)
        label("You have entered two different passwords!")
        button("Back"){
            action {
                close()
            }
        }
    }
}

class ExistingUser: Fragment("User already exists!"){
    override val root = vbox(20, Pos.BASELINE_CENTER){
        addClass(MyStylesheet.infoFragment)
        label("User with this name already exists!")
        button("Back"){
            action {
                close()
            }
        }
    }
}