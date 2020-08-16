package me.koallann.myagenda.domain

data class Topic(
    var id: Int = 0,
    var title: String = "",
    var description: String = "",
    var status: Status = Status.CLOSED,
    var author: User= User()
) {
    enum class Status { OPEN, CLOSED }
}
