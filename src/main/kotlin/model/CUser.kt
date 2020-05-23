package model

data class CUser (val name: String, var balance: Int = 0, val password: String = "", val vip: Boolean){
    fun changeBalance(amount: Int){
        balance+=amount
    }

}