package view

import tornadofx.*

fun main(){
    launch<MyApp>()
}

class MyApp: App(LoginView::class) {

}