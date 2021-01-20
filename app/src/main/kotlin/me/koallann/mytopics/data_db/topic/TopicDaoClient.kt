package me.koallann.mytopics.data_db.topic

import io.reactivex.Completable
import io.reactivex.Single
import me.koallann.mytopics.data.topic.TopicLocalDataSource
import me.koallann.mytopics.domain.Topic
import java.util.concurrent.TimeUnit

class TopicDaoClient(private val topicDao: TopicDao) : TopicLocalDataSource {

    override fun getTopicsByStatus(status: Topic.Status): Single<List<Topic>> {
        return Completable.timer(2, TimeUnit.SECONDS)
            .andThen(topicDao.findByStatus(status))
            .map { list -> list.map { it.toDomain() } }
    }

    override fun createTopic(topic: Topic): Completable {
        return Completable.timer(2, TimeUnit.SECONDS)
            .andThen(topicDao.insert(
                TopicEntity().apply {
                    title = topic.title
                    briefDescription = topic.briefDescription
                    details = topic.details
                    status = topic.status
                    userId = topic.author.id
                }
            ))
    }

    override fun updateTopicStatus(topic: Topic): Completable {
        return Completable.timer(2, TimeUnit.SECONDS)
            .andThen(Completable.fromSingle(
                topicDao.updateStatus(topic.id, topic.status).doOnSuccess {
                    if (it != 1) {
                        throw IllegalArgumentException("Invalid topic id")
                    }
                }
            ))
    }

}
