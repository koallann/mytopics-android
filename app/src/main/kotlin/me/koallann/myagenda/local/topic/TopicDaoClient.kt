package me.koallann.myagenda.local.topic

import android.content.Context
import io.reactivex.Completable
import io.reactivex.Single
import me.koallann.myagenda.data.topic.TopicLocalDataSource
import me.koallann.myagenda.domain.Topic
import me.koallann.myagenda.domain.User
import me.koallann.myagenda.local.AppDatabase

class TopicDaoClient(context: Context) : TopicLocalDataSource {

    private val topicDao: TopicDao = AppDatabase.getInstance(context).getTopicDao()

    override fun getTopicsByUser(user: User): Single<List<Topic>> {
        return topicDao.findByUser(user.id)
            .map { list -> list.map { it.toDomain() } }
    }

    override fun createTopic(topic: Topic): Completable {
        return topicDao.insert(
            TopicEntity().apply {
                title = topic.title
                description = topic.description
                status = topic.status
                userId = topic.author.id
            }
        )
    }

    override fun updateTopicStatus(topic: Topic): Completable {
        return topicDao.updateStatus(topic.id, topic.status)
    }

}
