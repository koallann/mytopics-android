package me.koallann.myagenda.domain

data class Topic(
    var id: Int = 0,
    var title: String = "",
    var briefDescription: String = "",
    var details: String = "",
    var status: Status = Status.UNKNOWN,
    var author: User= User()
) {
    enum class Status { OPEN, CLOSED, UNKNOWN }
}
