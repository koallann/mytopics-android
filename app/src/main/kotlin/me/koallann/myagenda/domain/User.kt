package me.koallann.myagenda.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var id: Int = -1,
    var name: String = "",
    var email: String = "",
    var secret: Secret? = null
) : Parcelable {
    @Parcelize
    data class Secret(var password: String = "") : Parcelable
}
