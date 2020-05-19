////package model
////
////import java.util.*
////
////fun depMain() {
////    // Test block
////    val userDatabase = CUserDatabase
////    val user1 = CUser("praja", 100, "1", true, true)
////    val user2 = CUser("matous", 100, "2", true, true)
////
////    val itemDatabase = CItemDatabase
////    userDatabase.addUser(user1)
////    userDatabase.addUser(user2)
////
////    while(true) {
////        println("Zadej uživatelské jméno:")
////        val name = readLine().toString().toLowerCase()
////        println("Zadej heslo:")
////        val password = readLine().toString()
////        val retUser = userDatabase.authenticateUser(name, password)
////        if(null != retUser) {
////            userMenu(retUser, userDatabase, itemDatabase)
////            return
////        }
////        else
////            println("Špatné jméno nebo heslo!")
////    }
////}
////
////fun checkPassword(user: String, database: CUserDatabase): Boolean{
////    val tempUser = database.getUserByName(user)
////    println("Ahoj $user, zadej heslo:")
////    val pass: String = readLine().toString()
////    if(null != tempUser && tempUser.password == pass)
////        return true
////    return false
////}
////
////fun userMenu(user: CUser, userDatabase: CUserDatabase, itemDatabase: CItemDatabase){
////    var input: Int
////    initialize(itemDatabase, userDatabase)
////    while(true){
////        println("What do you want to do?")
////        println("\t[0] - Vypiš sklad")
////        println("\t[1] - Vyjmi ze skladu")
////        println("\t[2] - Přidej do skladu")
////        println("\t[3] - Vypiš uživatele")
////        println("\t[4] - Změň stav konta")
////        println("\t[5] - Import skladu")
////        println("\t[6] - Export skladu")
////        println("\t[7] - Přidej uživatele")
////        println("\t[10] - Konec")
////
////        input = readLine()?.toInt() ?: -1
////        when(input) {
////            0 -> {
////                cleanConsole(3)
////                itemDatabase.listAll()
////                cleanConsole(3)
////            }
////            1 -> {
////                if(itemDatabase.extractItem(userDatabase)) {
////                    cleanConsole(3)
////                    println("Vydání proběhlo v pořádku")
////                    cleanConsole(3)
////                }
////                else{
////                    cleanConsole(3)
////                    println("Vydání se nezdařilo")
////                    cleanConsole(3)
////                }
////            }
////            2 -> {
////                if(itemDatabase.inputItem(user)) {
////                    cleanConsole(3)
////                    println("Přidání proběhlo v pořádku")
////                    cleanConsole(3)
////                }
////                else {
////                    cleanConsole(3)
////                    println("Přidání se nezdařilo!")
////                    cleanConsole(3)
////                }
////            }
////            3 -> {
////                cleanConsole(3)
////                userDatabase.listUsers()
////                cleanConsole(3)
////            }
////            4 -> {
////                if(userDatabase.changeBalance()){
////                    cleanConsole(3)
////                    println("Změna proběhla v pořádku")
////                    cleanConsole(3)
////                }
////                else{
////                    cleanConsole(3)
////                    println("Změna se nezdařila")
////                    cleanConsole(3)
////                }
////            }
////            5 -> {
////                println("Zadej cestu k souboru:")
////                val path = readLine().toString()
////                itemDatabase.importFromFile(path)
////
////            }
////            6 -> {
////                println("Zadej cestu k souboru:")
////                val path = readLine().toString()
////                itemDatabase.exportToFile(path, user)
////            }
////            7 ->{
////                if(userDatabase.importUser()) {
////                    cleanConsole(3)
////                    println("Import proběhl v pořádku")
////                    cleanConsole(3)
////                }
////                else{
////                    cleanConsole(3)
////                    println("Import se nezdařil")
////                    cleanConsole(3)
////                }
////            }
////            10 -> {
////                itemDatabase.exportToFile("items.txt", user)
////                userDatabase.exportToFile("users.txt", user)
////                return
////            }
////            else -> {
////                println("To tu není vole!")
////            }
////        }
////    }
////}
////
////fun initialize(itemDatabase: CItemDatabase, userDatabase: CUserDatabase){
////    itemDatabase.silentImportFromFile("items.txt")
////    userDatabase.silentImportFromFile("users.txt")
////}
////
//fun cleanConsole(it: Int){
//    var iterations = it
//    while(iterations > 0) {
//        println()
//        iterations--
//    }
//}
