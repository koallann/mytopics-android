package me.koallann.mytopics.data_db.topic

import androidx.room.*
import me.koallann.mytopics.domain.Topic
import me.koallann.mytopics.data_db.user.UserEntity

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
open class TopicEntity {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var title: String = ""
    @ColumnInfo(name = "brief_description")
    var briefDescription: String = ""
    var details: String = ""
    var status: Topic.Status = Topic.Status.UNKNOWN
    @ColumnInfo(name = "user_id")
    var userId: Int = 0
    @Ignore
    open var user: UserEntity = UserEntity()
}
