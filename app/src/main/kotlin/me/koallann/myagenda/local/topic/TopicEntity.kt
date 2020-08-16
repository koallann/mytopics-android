package me.koallann.myagenda.local.topic

import androidx.room.*
import me.koallann.myagenda.domain.Topic
import me.koallann.myagenda.local.user.UserEntity

@Entity(
    tableName = "topics_topic",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(name = "user", value = ["user_id"])]
)
data class TopicEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val status: Topic.Status,
    @ColumnInfo(name = "user_id")
    val userID: Int
)
