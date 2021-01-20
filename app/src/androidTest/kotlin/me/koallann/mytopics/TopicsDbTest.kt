package me.koallann.mytopics

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import me.koallann.mytopics.data.topic.TopicRepository
import me.koallann.mytopics.data.topic.TopicRepositoryImpl
import me.koallann.mytopics.data_db.AppDatabase
import me.koallann.mytopics.data_db.topic.TopicDaoClient
import me.koallann.mytopics.data_db.topic.TopicEntity
import me.koallann.mytopics.data_db.user.UserEntity
import me.koallann.mytopics.domain.Topic
import me.koallann.mytopics.domain.User
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.IllegalArgumentException

@RunWith(AndroidJUnit4::class)
class TopicsDbTest {

    private lateinit var database: AppDatabase
    private lateinit var repository: TopicRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        repository = TopicRepositoryImpl(TopicDaoClient(database.getTopicDao()))
    }

    @After
    fun finish() {
        database.clearAllTables()
    }

    @Test
    fun shouldNotLoadTopicsWhenThereAreNoTopics() {
        repository.getTopicsByStatus(Topic.Status.OPEN)
            .test()
            .also { it.awaitTerminalEvent() }
            .assertSubscribed()
            .assertComplete()
            .assertValue { it.isEmpty() }
    }

    @Test
    fun shouldLoadTopicsWhenThereAreTopics() {
        val user = User(1, "John Doe", "john.doe@acme.com", null)
        val topics = listOf(
            Topic(1, "Topic #1", "...", "...", Topic.Status.OPEN, user),
            Topic(2, "Topic #2", "...", "...", Topic.Status.OPEN, user),
            Topic(3, "Topic #3", "...", "...", Topic.Status.CLOSED, user)
        )

        UserEntity().apply {
            name = user.name
            email = user.email
            password = "123456"
        }.also {
            database.getUserDao().insert(it).subscribe()
        }
        topics.map {
            TopicEntity().apply {
                title = it.title
                briefDescription = it.briefDescription
                details = it.details
                status = it.status
                userId = 1
            }
        }.also {
            database.getTopicDao().insert(it).subscribe()
        }

        repository.getTopicsByStatus(Topic.Status.OPEN)
            .test()
            .also { it.awaitTerminalEvent() }
            .assertSubscribed()
            .assertComplete()
            .assertValue { it == topics.slice(0..1) }
    }

    @Test
    fun shouldNotCreateTopicWithInvalidUser() {
        val user = User(1, "John Doe", "john.doe@acme.com", null)
        val topic = Topic(1, "Topic #1", "...", "...", Topic.Status.OPEN, user)

        repository.createTopic(topic)
            .test()
            .also { it.awaitTerminalEvent() }
            .assertSubscribed()
            .assertNotComplete()
            .assertError { it is SQLiteConstraintException }
    }

    @Test
    fun shouldCreateTopicWithValidUser() {
        val user = User(1, "John Doe", "john.doe@acme.com", null)
        val topic = Topic(1, "Topic #1", "...", "...", Topic.Status.OPEN, user)

        UserEntity().apply {
            name = user.name
            email = user.email
            password = "123456"
        }.also {
            database.getUserDao().insert(it).subscribe()
        }

        repository.createTopic(topic)
            .test()
            .also { it.awaitTerminalEvent() }
            .assertSubscribed()
            .assertComplete()
            .assertNoValues()
    }

    @Test
    fun shouldNotUpdateInvalidTopic() {
        val user = User(1, "John Doe", "john.doe@acme.com", null)
        val topic = Topic(1, "Topic #1", "...", "...", Topic.Status.OPEN, user)

        repository.updateTopic(topic)
            .test()
            .also { it.awaitTerminalEvent() }
            .assertSubscribed()
            .assertNotComplete()
            .assertError { it is IllegalArgumentException }
    }

    @Test
    fun shouldUpdateValidTopic() {
        val user = User(1, "John Doe", "john.doe@acme.com", null)
        val topic = Topic(1, "Topic #1", "...", "...", Topic.Status.OPEN, user)

        UserEntity().apply {
            name = user.name
            email = user.email
            password = "123456"
        }.also {
            database.getUserDao().insert(it).subscribe()
        }
        TopicEntity().apply {
            title = topic.title
            briefDescription = topic.briefDescription
            details = topic.details
            status = topic.status
            userId = user.id
        }.also {
            database.getTopicDao().insert(it).subscribe()
        }

        repository.updateTopic(topic.copy(status = Topic.Status.CLOSED))
            .test()
            .also { it.awaitTerminalEvent() }
            .assertSubscribed()
            .assertComplete()
    }

}
