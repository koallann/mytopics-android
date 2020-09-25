package me.koallann.mytopics.presentation.topics

import io.reactivex.Single
import me.koallann.mytopics.R
import me.koallann.mytopics.data.topic.TopicRepository
import me.koallann.mytopics.data.user.UserRepository
import me.koallann.mytopics.domain.Topic
import me.koallann.mytopics.domain.User
import me.koallann.support.rxschedulers.ImmediateSchedulerProvider
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class TopicsPresenterTest {

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
    fun `Should not notify the view when a signed user isn't loaded`() {
        `when`(userRepository.getSignedUser()).thenReturn(null)

        presenter.onLoadUser()

        verify(view, never()).onUserLoaded(any(User::class.java))
    }

    @Test
    fun `Should notify the view when a signed user is loaded`() {
        val user = User(name = "John Doe", email = "john.doe@acme.com")

        `when`(userRepository.getSignedUser()).thenReturn(user)

        presenter.onLoadUser()

        verify(view, times(1)).onUserLoaded(user)
    }

    @Test
    fun `Should notify the view when topics are loaded`() {
        val topics = listOf(
            Topic(title = "Cookie or cracker?"),
            Topic(title = "Important!")
        )

        `when`(topicRepository.getTopicsByStatus(Topic.Status.OPEN)).thenReturn(Single.just(topics))

        presenter.onLoadTopics()

        verify(view, times(1)).addTopics(topics)
    }

    @Test
    fun `Should show a default message when some error occurs`() {
        val error = Throwable("Unknown")

        `when`(topicRepository.getTopicsByStatus(Topic.Status.OPEN)).thenReturn(Single.error(error))

        presenter.onLoadTopics()

        verify(view).showMessage(R.string.msg_topic_error)
        verify(view, never()).addTopics(ArgumentMatchers.anyList())
    }

    @Test
    fun `Should collapse the topic when it's clicked`() {
        val topic = Topic(title = "A")

        presenter.onClickTopic(topic)

        verify(view, times(1)).collapseTopic(topic)
    }

    @Test
    fun `Should uncollapse the topic when it was the previous clicked`() {
        val topic = Topic(title = "A")

        presenter.onClickTopic(topic)
        presenter.onClickTopic(topic)

        verify(view, times(1)).collapseTopic(topic)
        verify(view, times(1)).uncollapseTopic(topic)
    }

    @Test
    fun `Should uncollapse the previous clicked and collapse the new clicked topic `() {
        val previous = Topic(title = "A")
        val new = Topic(title = "B")

        presenter.onClickTopic(previous)
        presenter.onClickTopic(new)

        verify(view, times(1)).uncollapseTopic(previous)
        verify(view, times(1)).collapseTopic(new)
    }

    @Test
    fun `Should collapse the new clicked topic and uncollapse the previous collapsed + uncollapsed`() {
        val previous = Topic(title = "A")
        val new = Topic(title = "B")

        presenter.onClickTopic(previous)
        presenter.onClickTopic(previous)
        presenter.onClickTopic(new)

        verify(view, times(1)).collapseTopic(previous)
        verify(view, times(1)).uncollapseTopic(previous)
        verify(view, times(1)).collapseTopic(new)
    }

}
