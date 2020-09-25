package me.koallann.mytopics.data_db.topic

import android.content.Context
import io.reactivex.Completable
import io.reactivex.Single
import me.koallann.mytopics.data.topic.TopicLocalDataSource
import me.koallann.mytopics.domain.Topic
import me.koallann.mytopics.data_db.AppDatabase
import java.util.concurrent.TimeUnit

class TopicDaoClient(context: Context) : TopicLocalDataSource {

    private val topicDao: TopicDao = AppDatabase.getInstance(context).getTopicDao()

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
            .andThen(topicDao.updateStatus(topic.id, topic.status))
    }

}
