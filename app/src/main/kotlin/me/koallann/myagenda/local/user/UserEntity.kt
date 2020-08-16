package me.koallann.myagenda.local.user

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import me.koallann.myagenda.domain.User
import me.koallann.myagenda.local.topic.TopicEntity

@Entity(
    tableName = "users_user",
    indices = [Index(name = "email", unique = true, value = ["email"])]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val password: String
) {

    fun toDomain(): User = User(id, name, email)

}
