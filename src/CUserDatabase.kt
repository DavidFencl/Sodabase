import java.io.File

data class CUserDatabase(var ususerDatabase: HashMap<String, CUser>){
    fun getUserByName(name: String): CUser?{
        if(ususerDatabase.containsKey(name))
            return ususerDatabase.getValue(name)
        return null
    }
    fun addUser(user: CUser){
        ususerDatabase[user.name] = user
    }
    fun isPresent(name: String): Boolean{
        if(ususerDatabase.contains(name))
            return true
        println("$name not present :(")
        return false
    }
    fun listUsers(){
        for ((key, value) in ususerDatabase)
            println("$key is $value")
    }
    fun toJSON(filename: String){
        File(filename).printWriter().use { out ->
            out.println("{CInventory:")
            ususerDatabase.forEach{
                out.println("\t{")
                out.println("\t\"name\":\"${it.value.name}\",")
                out.println("\t\"price\":${it.value.balance},")
                out.println("\t\"quantity:\"${it.value.vip}\n\t}")
            }
            out.print("}")
        }
    }
}