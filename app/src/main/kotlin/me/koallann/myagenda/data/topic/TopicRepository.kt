package me.koallann.myagenda.data.topic

import io.reactivex.Completable
import io.reactivex.Single
import me.koallann.myagenda.domain.Topic
import me.koallann.myagenda.domain.User

interface TopicRepository {

    fun getTopicsByUser(user: User): Single<List<Topic>>

    fun createTopic(topic: Topic): Completable

    fun updateTopic(topic: Topic): Completable

}
