package me.koallann.myagenda.presentation.topics

import io.reactivex.Completable
import me.koallann.myagenda.R
import me.koallann.myagenda.data.topic.TopicRepository
import me.koallann.myagenda.data.user.UserRepository
import me.koallann.myagenda.domain.Topic
import me.koallann.myagenda.domain.User
import me.koallann.support.rxschedulers.ImmediateSchedulerProvider
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeUnit

class ClosedTopicsPresenterTest {

    @Mock
    private lateinit var topicRepository: TopicRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var view: TopicsView

    private lateinit var presenter: TopicsPresenter

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        presenter = TopicsPresenter(
            Topic.Status.CLOSED,
            topicRepository,
            userRepository,
            ImmediateSchedulerProvider(),
            TopicsErrorHandler()
        )
        presenter.attachView(view)
    }

    @After
    fun finish() {
        presenter.stop()
        presenter.detachView()
    }

    @Test
    fun `Should not confirm reopening when the topic is being updated `() {
        val topic = Topic(title = "Cookie or cracker?", status = Topic.Status.CLOSED)

        `when`(topicRepository.updateTopic(topic.copy(status = Topic.Status.OPEN)))
            .thenReturn(Completable.timer(1, TimeUnit.SECONDS))

        presenter.reopenTopic(topic)
        presenter.onClickReopenTopic(topic)

        verify(view, never()).onConfirmReopenTopic(topic)
        verify(view, never()).onConfirmCloseTopic(topic)
    }

    @Test
    fun `Should confirm reopening when the topic is not being updated `() {
        val topic = Topic(title = "Cookie or cracker?", status = Topic.Status.CLOSED)

        presenter.onClickReopenTopic(topic)

        verify(view).onConfirmReopenTopic(topic)
        verify(view, never()).onConfirmCloseTopic(topic)
    }

    @Test
    fun `Should not switch status to OPEN neither notify the view when an error occurs on topic reopening`() {
        val topic = Topic(title = "Cookie or cracker?", status = Topic.Status.CLOSED)
        val error = Throwable("Error!")

        `when`(topicRepository.updateTopic(topic.copy(status = Topic.Status.OPEN)))
            .thenReturn(Completable.error(error))

        presenter.reopenTopic(topic)

        assert(topic.status == Topic.Status.CLOSED)
        verify(view, never()).onTopicReopened(topic)
        verify(view, never()).onTopicClosed(topic)
        verify(view).showMessage(R.string.msg_topic_error)
    }

    @Test
    fun `Should switch status to OPEN and notify the view when the topic is reopened `() {
        val topic = Topic(title = "Cookie or cracker?", status = Topic.Status.CLOSED)

        `when`(topicRepository.updateTopic(topic.copy(status = Topic.Status.OPEN)))
            .thenReturn(Completable.complete())

        presenter.reopenTopic(topic)

        assert(topic.status == Topic.Status.OPEN)
        verify(view).onTopicReopened(topic)
        verify(view, never()).onTopicClosed(topic)
    }

}
