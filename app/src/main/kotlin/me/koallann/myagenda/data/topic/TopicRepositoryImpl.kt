package me.koallann.myagenda.data.topic

import io.reactivex.Completable
import io.reactivex.Single
import me.koallann.myagenda.domain.Topic
import me.koallann.myagenda.domain.User

class TopicRepositoryImpl(private val localDataSource: TopicLocalDataSource) : TopicRepository {

    override fun getTopicsByUser(user: User): Single<List<Topic>> {
        return localDataSource.getTopicsByUser(user)
    }

    override fun createTopic(topic: Topic): Completable {
        return localDataSource.createTopic(topic)
    }

    override fun updateTopic(topic: Topic): Completable {
        return localDataSource.updateTopicStatus(topic)
    }

}
