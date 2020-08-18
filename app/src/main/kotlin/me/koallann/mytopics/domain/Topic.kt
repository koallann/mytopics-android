package me.koallann.mytopics.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Topic(
    var id: Int = 0,
    var title: String = "",
    var briefDescription: String = "",
    var details: String = "",
    var status: Status = Status.UNKNOWN,
    var author: User = User()
) : Parcelable {
    enum class Status { OPEN, CLOSED, UNKNOWN }
}
