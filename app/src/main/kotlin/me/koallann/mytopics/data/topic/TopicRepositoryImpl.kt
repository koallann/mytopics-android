package me.koallann.mytopics.data.topic

import io.reactivex.Completable
import io.reactivex.Single
import me.koallann.mytopics.domain.Topic

class TopicRepositoryImpl(private val localDataSource: TopicLocalDataSource) : TopicRepository {

    override fun getTopicsByStatus(status: Topic.Status): Single<List<Topic>> {
        return localDataSource.getTopicsByStatus(status)
    }

    override fun createTopic(topic: Topic): Completable {
        return localDataSource.createTopic(topic)
    }

    override fun updateTopic(topic: Topic): Completable {
        return localDataSource.updateTopicStatus(topic)
    }

}
