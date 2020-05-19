package view

import javafx.geometry.Pos
import tornadofx.*

class LoginFailed: Fragment(){
    override val root = vbox(20, Pos.BASELINE_CENTER){
        style{
            padding= CssBox(
                Dimension(10.0, Dimension.LinearUnits.px),
                Dimension(10.0, Dimension.LinearUnits.px),
                Dimension(10.0, Dimension.LinearUnits.px),
                Dimension(10.0, Dimension.LinearUnits.px)
            )
        }
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
        style{
            padding= CssBox(
                Dimension(10.0, Dimension.LinearUnits.px),
                Dimension(10.0, Dimension.LinearUnits.px),
                Dimension(10.0, Dimension.LinearUnits.px),
                Dimension(10.0, Dimension.LinearUnits.px)
            )
        }
        label("Bad item ID!")
        button("Ok"){
            action {
                close()
            }
        }
    }
}

class TooManyItems: Fragment(){
    override val root = vbox(20, Pos.BASELINE_CENTER){
        style{
            padding= CssBox(
                Dimension(10.0, Dimension.LinearUnits.px),
                Dimension(10.0, Dimension.LinearUnits.px),
                Dimension(10.0, Dimension.LinearUnits.px),
                Dimension(10.0, Dimension.LinearUnits.px)
            )
        }
        label("That's too many!!")
        button("Ok"){
            action {
                close()
            }
        }
    }
}