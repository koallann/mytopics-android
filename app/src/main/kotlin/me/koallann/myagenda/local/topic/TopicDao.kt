package me.koallann.myagenda.local.topic

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Single

@Dao
interface TopicDao {

    @Query("SELECT * FROM topics_topic WHERE user_id = :userId")
    fun findByUser(userId: Int): Single<List<TopicEntity>>

}
