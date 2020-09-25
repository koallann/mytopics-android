package me.koallann.mytopics.presentation.topics

import io.reactivex.Completable
import me.koallann.mytopics.R
import me.koallann.mytopics.data.topic.TopicRepository
import me.koallann.mytopics.data.user.UserRepository
import me.koallann.mytopics.domain.Topic
import me.koallann.support.rxschedulers.ImmediateSchedulerProvider
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeUnit

class OpenTopicsPresenterTest {

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
            Topic.Status.OPEN,
            topicRepository,
            userRepository,
            ImmediateSchedulerProvider(),
            TopicsErrorHandler()
        )
        presenter.attachView(view)
    }

    @After
    fun finish() {
        presenter.detachView()
    }

    @Test
    fun `Should not confirm closing when the topic is being updated `() {
        val topic = Topic(title = "Cookie or cracker?", status = Topic.Status.OPEN)

        `when`(topicRepository.updateTopic(topic.copy(status = Topic.Status.CLOSED)))
            .thenReturn(Completable.timer(1, TimeUnit.SECONDS))

        presenter.closeTopic(topic)
        presenter.onClickCloseTopic(topic)

        verify(view, never()).onConfirmCloseTopic(topic)
        verify(view, never()).onConfirmReopenTopic(topic)
    }

    @Test
    fun `Should confirm closing when the topic is not being updated `() {
        val topic = Topic(title = "Cookie or cracker?", status = Topic.Status.OPEN)

        presenter.onClickCloseTopic(topic)

        verify(view).onConfirmCloseTopic(topic)
        verify(view, never()).onConfirmReopenTopic(topic)
    }

    @Test
    fun `Should not switch status to CLOSED neither notify the view when an error occurs on topic closing`() {
        val topic = Topic(title = "Cookie or cracker?", status = Topic.Status.OPEN)
        val error = Throwable("Error!")

        `when`(topicRepository.updateTopic(topic.copy(status = Topic.Status.CLOSED)))
            .thenReturn(Completable.error(error))

        presenter.closeTopic(topic)

        assert(topic.status == Topic.Status.OPEN)
        verify(view, never()).onTopicClosed(topic)
        verify(view, never()).onTopicReopened(topic)
        verify(view).showMessage(R.string.msg_topic_error)
    }

    @Test
    fun `Should switch status to CLOSED and notify the view when the topic is closed `() {
        val topic = Topic(title = "Cookie or cracker?", status = Topic.Status.OPEN)

        `when`(topicRepository.updateTopic(topic.copy(status = Topic.Status.CLOSED)))
            .thenReturn(Completable.complete())

        presenter.closeTopic(topic)

        assert(topic.status == Topic.Status.CLOSED)
        verify(view).onTopicClosed(topic)
        verify(view, never()).onTopicReopened(topic)
    }

}
