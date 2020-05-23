package view


import javafx.geometry.Pos
import tornadofx.*

class MyStylesheet: Stylesheet(){
    companion object{
        val infoFragment by cssclass()
        val menuLabel by cssclass()
        val menu by cssclass()
        val loginScreen by cssclass()
        val itemTable by cssclass()
    }
    init{
        infoFragment{
            padding= CssBox(
                Dimension(10.0, Dimension.LinearUnits.px),
                Dimension(10.0, Dimension.LinearUnits.px),
                Dimension(10.0, Dimension.LinearUnits.px),
                Dimension(10.0, Dimension.LinearUnits.px)
            )
        }
        menuLabel{
            fontFamily = "Impact"
            fontSize = Dimension(25.0, Dimension.LinearUnits.px)
        }
        menu{
            padding= CssBox(
                Dimension(10.0, Dimension.LinearUnits.px),
                Dimension(30.0, Dimension.LinearUnits.px),
                Dimension(10.0, Dimension.LinearUnits.px),
                Dimension(30.0, Dimension.LinearUnits.px)
            )
            button{
                minWidth = Dimension(10.0,Dimension.LinearUnits.pc)
            }
        }
        loginScreen{
            padding= CssBox(
                Dimension(50.0, Dimension.LinearUnits.px),
                Dimension(50.0, Dimension.LinearUnits.px),
                Dimension(50.0, Dimension.LinearUnits.px),
                Dimension(50.0, Dimension.LinearUnits.px)
            )
            prefWidth = Dimension(1000.0, Dimension.LinearUnits.px)
            prefHeight = Dimension(500.0, Dimension.LinearUnits.px)
        }
        itemTable{
               tableCell{
                   alignment = Pos.CENTER
               }
        }
    }
}