package me.koallann.myagenda.local.topic

import androidx.room.Ignore
import androidx.room.Relation
import me.koallann.myagenda.domain.Topic
import me.koallann.myagenda.local.user.UserEntity

class TopicEntityWithUser : TopicEntity() {
    @Relation(parentColumn = "id", entityColumn = "user_id", entity = UserEntity::class)
    @Ignore
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
