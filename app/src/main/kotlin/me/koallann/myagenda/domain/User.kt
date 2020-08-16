package me.koallann.myagenda.domain

data class User(
    var name: String = "",
    var email: String = "",
    var secret: Secret? = null
) {
    data class Secret(var password: String = "")
}
