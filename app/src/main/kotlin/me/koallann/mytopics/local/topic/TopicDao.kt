package me.koallann.mytopics.local.topic

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single
import me.koallann.mytopics.domain.Topic

@Dao
interface TopicDao {

    @Query("SELECT * FROM topics_topic WHERE status = :status")
    fun findByStatus(status: Topic.Status): Single<List<TopicEntityWithUser>>

    @Insert
    fun insert(topic: TopicEntity): Completable

    @Query("UPDATE topics_topic SET status = :status WHERE id = :id")
    fun updateStatus(id: Int, status: Topic.Status): Completable

}
