package me.koallann.myagenda.data.topic

import io.reactivex.Completable
import io.reactivex.Single
import me.koallann.myagenda.domain.Topic

interface TopicLocalDataSource {

    fun getTopicsByStatus(status: Topic.Status): Single<List<Topic>>

    fun createTopic(topic: Topic): Completable

    fun updateTopicStatus(topic: Topic): Completable

}
