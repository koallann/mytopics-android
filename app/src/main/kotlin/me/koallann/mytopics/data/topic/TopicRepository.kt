package me.koallann.mytopics.data.topic

import io.reactivex.Completable
import io.reactivex.Single
import me.koallann.mytopics.domain.Topic

interface TopicRepository {

    fun getTopicsByStatus(status: Topic.Status): Single<List<Topic>>

    fun createTopic(topic: Topic): Completable

    fun updateTopic(topic: Topic): Completable

}
