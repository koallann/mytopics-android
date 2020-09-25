package me.koallann.mytopics.data_db.topic

import androidx.room.Relation
import me.koallann.mytopics.domain.Topic
import me.koallann.mytopics.data_db.user.UserEntity

class TopicEntityWithUser : TopicEntity() {
    @Relation(parentColumn = "user_id", entityColumn = "id", entity = UserEntity::class)
    override var user: UserEntity = UserEntity()

    fun toDomain(): Topic = Topic(
        id,
        title,
        briefDescription,
        details,
        status,
        user.toDomain()
    )

}
