package me.koallann.myagenda.presentation.profile

import io.reactivex.Completable
import me.koallann.myagenda.data.user.UserRepository
import me.koallann.myagenda.domain.User
import me.koallann.support.rxschedulers.ImmediateSchedulerProvider
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class ProfilePresenterTest {

    @Mock
    private lateinit var userRepository: UserRepository
    @Mock
    private lateinit var view: ProfileView

    private lateinit var presenter: ProfilePresenter

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        presenter = ProfilePresenter(userRepository, ImmediateSchedulerProvider())
        presenter.attachView(view)
    }

    @After
    fun finish() {
        presenter.stop()
        presenter.detachView()
    }

    @Test
    fun `Should notify the view when there is signed user`() {
        val user = User(name = "John Doe", email = "john.doe")

        `when`(userRepository.getSignedUser()).thenReturn(user)

        presenter.onLoadUser()

        verify(view, times(1)).onUserLoaded(user)
    }

    @Test
    fun `Should call the view to confirm on click to sign out`() {
        presenter.onClickSignOut()

        verify(view, times(1)).onConfirmSignOut()
    }

    @Test
    fun `Should navigate to sign in after sign out`() {
        `when`(userRepository.signOutUser()).thenReturn(Completable.complete())

        presenter.signOutUser()

        verify(view, times(1)).navigateToLogin()
    }

}
