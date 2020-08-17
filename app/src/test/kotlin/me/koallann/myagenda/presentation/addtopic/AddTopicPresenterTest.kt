package me.koallann.myagenda.presentation.addtopic

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
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class AddTopicPresenterTest {

    @Mock
    private lateinit var topicRepository: TopicRepository
    @Mock
    private lateinit var userRepository: UserRepository
    @Mock
    private lateinit var view: AddTopicView

    private lateinit var presenter: AddTopicPresenter

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        presenter = AddTopicPresenter(
            topicRepository,
            userRepository,
            ImmediateSchedulerProvider(),
            AddTopicErrorHandler()
        )
        presenter.attachView(view)
    }

    @After
    fun finish() {
        presenter.stop()
        presenter.detachView()
    }

    @Test
    fun `Should not try to add topic when the topic fields are invalid`() {
        val topic = Topic(title = "Important!", briefDescription = "It's cookie or cracker?")

        `when`(view.validateTopicFields()).thenReturn(false)

        presenter.onClickAddTopic(topic)

        verify(view, never()).onTopicAdded(topic)
    }

    @Test
    fun `Should not try to add topic when there is no signed user, show a message too`() {
        val topic = Topic(title = "Important!", briefDescription = "It's cookie or cracker?")

        `when`(view.validateTopicFields()).thenReturn(true)
        `when`(userRepository.getSignedUser()).thenReturn(null)

        presenter.onClickAddTopic(topic)

        verify(view).showMessage(R.string.msg_no_user_signed)
        verify(view, never()).onTopicAdded(topic)
    }

    @Test
    fun `Should set the signed user as author and the status as open when the topic fields are valid`() {
        val topic = Topic(title = "Important!", briefDescription = "It's cookie or cracker?")
        val user = User(name = "John Doe", email = "john.doe@acme.com")

        `when`(view.validateTopicFields()).thenReturn(true)
        `when`(userRepository.getSignedUser()).thenReturn(user)
        `when`(topicRepository.createTopic(topic)).thenReturn(Completable.complete())

        presenter.onClickAddTopic(topic)

        assert(topic.author == user)
        assert(topic.status == Topic.Status.OPEN)
    }

    @Test
    fun `Should notify the view when the topic is created`() {
        val topic = Topic(title = "Important!", briefDescription = "It's cookie or cracker?")
        val user = User(name = "John Doe", email = "john.doe@acme.com")

        `when`(view.validateTopicFields()).thenReturn(true)
        `when`(userRepository.getSignedUser()).thenReturn(user)
        `when`(topicRepository.createTopic(topic)).thenReturn(Completable.complete())

        presenter.onClickAddTopic(topic)

        verify(view, times(1)).onTopicAdded(topic)
    }

}
