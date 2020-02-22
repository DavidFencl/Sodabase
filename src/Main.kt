fun main(args: Array<String>){
    // Test block
    val userDatabase = CUserDatabase(hashMapOf())
    val user1 = CUser("praja",100,"1",true)
    val user2 = CUser("matous",100,"XXXProGamerXXX",true)

    val itemDatabase = CItemDatabase(hashMapOf())
//    val item1 = CItem("coca-cola zero",11,15,7)
//    itemDatabase.addNewItem(item1)
//    val testSet = HashSet<CItem>()
//    testSet.add(CItem(name = "coca-cola zero", quantity = 5))
//    testSet.add(CItem("fanta exotic", 11, 15, 15))
//
//    itemDatabase.restock(testSet)

    userDatabase.addUser(user1)
    userDatabase.addUser(user2)
    // End of test block

    println("Zadej uživatelské jméno: ")
    while(true){
        val inputString = readLine().toString().toLowerCase()
        if(userDatabase.isPresent(inputString))
            if(checkPassword(inputString,userDatabase)) {
                val loggedUser = userDatabase.getUserByName(inputString)
                println("Good job $inputString! You son of a bitch - I'm in B-)")
                if (loggedUser != null){
                    userMenu(loggedUser, userDatabase, itemDatabase)
                    return
                }
            }
            else {
                println("Wrong password!!! Fuck you")
                return
            }
        else {
            println("Nope")
        }
    }
}

fun checkPassword(user: String, database: CUserDatabase): Boolean{
    val tempUser = database.getUserByName(user)
    println("Ahoj $user, zadej heslo:")
    val pass: String = readLine().toString()
    if(null != tempUser && tempUser.password == pass)
        return true
    return false
}

fun userMenu(user: CUser, userDatabase: CUserDatabase, itemDatabase: CItemDatabase){
    var input: Int
    itemDatabase.importFromFile("data/items.txt")
    while(true){
        println("What do you want to do?")
        println("\t[0] - Vypiš sklad")
        println("\t[1] - Přidej novou věc do skladu")
        println("\t[2] - Import skladu")
        println("\t[3] - Export skladu")
        println("\t[4] - Vyjmi ze skladu")
        println("\t[5] - Vypiš uživatele")
        println("\t[6] - Změň stav konta")
        println("\t[7] - Přidej uživatele")
        println("\t[10] - Konec")

        input = readLine()?.toInt() ?: -1
        when(input) {
            -1 -> {println("To tu není vole!")
                    return}
            0 -> {
                cleanConsole(20)
                itemDatabase.listAll()
                cleanConsole(3)
            }
            1 -> {
                println("Zadejte jméno položky:")
                val name = readLine().toString().toLowerCase()
                println("Zadejte cenu pro kamarády:")
                val price = readLine()?.toInt()
                println("Zadejte cenu pro ostatní kolegy:")
                val othersPrice = readLine()?.toInt()

                println("Zadejte počet kusů:")
                val quantity = readLine()?.toInt()
                itemDatabase.addNewItem(CItem(name,price?:0,othersPrice?:0,quantity?:0))

                cleanConsole(10)
                println("Přidání proběhlo v pořádku")
                cleanConsole(10)
            }
            2 -> {
                println("Zadej cestu k souboru:")
                val path = readLine().toString()
                itemDatabase.importFromFile(path)

            }
            3 -> {
                println("Zadej cestu k souboru:")
                val path = readLine().toString()
                itemDatabase.exportToFile(path, user)
            }
            4 -> {
                cleanConsole(20)
                println("Kdo?")
                val name = readLine().toString().toLowerCase()
                val currentUser :CUser? = userDatabase.getUserByName(name)
                if(null == currentUser)
                    println("Takový uživatel neexistuje")
                else{
                    println("Co si bere ze skladu?")
                    println("\tSklad obsahuje:")
                    itemDatabase.listAllWithTabs()
                    println()
                    val itemName = readLine().toString()
                    val item = itemDatabase.getItem(itemName)
                    if(item != null){
                        println("Kolik?")
                        val quantity = readLine()?.toInt() ?: return
                        if(itemDatabase.checkOutItems(itemName,quantity)){
                            val userBalance = currentUser.balance
                            if(currentUser.vip) {
                                if(userBalance < (quantity*item.price)) {
                                    println("Pozor! Jsi v mínusu!")
                                }
                                currentUser.balance= userBalance- quantity * item.price
                            }
                            else {
                                if(userBalance < (quantity*item.price)) {
                                    println("Pozor! Jsi v mínusu!")
                                }
                                currentUser.balance= userBalance- quantity * item.price
                            }
                    }
                    }
                    else {
                        println("To ve skladu nemáme.")
                    }
                    }
                }
            5 -> {
                cleanConsole(10)
                userDatabase.listUsers()
                cleanConsole(3)
            }
            6 -> {
                cleanConsole(20)
                println("Komu?")
                val name = readLine().toString().toLowerCase()
                val tempUser = userDatabase.getUserByName(name)
                if(null != tempUser) {
                    println("Kolik chceš přidat?")
                    val amount = readLine()?.toInt()
                    tempUser.balance += amount?:0
                }
                else {
                    println("Takový uživatel neexistuje!")
                }
            }
            7 ->{
                println("Zadej jméno nového uživatele")
                val name = readLine().toString().toLowerCase()
                if(userDatabase.isPresent(name))
                    println("Takový uživatel již existuje!")
                else{
                    println("Zadej heslo pro nového uživatele")
                    val password = readLine().toString()
                        println("Zadej počáteční stav konta")
                        val ammount = readLine()?.toInt()
                        if(ammount == null)
                            println("Neplatná hodnota!")
                        else{
                            println("Je uživatel vip? Ano/Ne")
                            val answer = readLine().toString().toLowerCase()
                            if(answer == "ano")
                                userDatabase.addUser(CUser(name,ammount,password,true))
                            else
                                userDatabase.addUser(CUser(name,ammount,password,false))
                    }
                }
            }
            10 -> {
                itemDatabase.exportToFile("items.txt", user)
                return
            }
            else -> {println("To tu není vole!")
                        return}

        }
    }
}

fun cleanConsole(it: Int){
    var iterations = it
    while(iterations > 0) {
        println()
        iterations--
    }
}

