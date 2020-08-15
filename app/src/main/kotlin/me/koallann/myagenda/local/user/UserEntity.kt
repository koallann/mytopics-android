package me.koallann.myagenda.local.user

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import me.koallann.myagenda.domain.User

@Entity(tableName = "users_user", indices = [Index(name = "email", unique = true)])
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val email: String,
    val password: String
) {

    fun toDomain(): User = User(this.name, this.email)

}
