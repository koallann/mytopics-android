package me.koallann.myagenda.presentation.signin

import androidx.room.EmptyResultSetException
import io.reactivex.Single
import me.koallann.myagenda.R
import me.koallann.myagenda.data.user.UserRepository
import me.koallann.myagenda.domain.Credentials
import me.koallann.myagenda.domain.User
import me.koallann.support.handlers.ErrorHandler
import me.koallann.support.rxschedulers.ImmediateSchedulerProvider
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class SignInPresenterTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var view: SignInView

    private lateinit var errorHandler: ErrorHandler
    private lateinit var presenter: SignInPresenter

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        errorHandler = SignInErrorHandler()
        presenter = SignInPresenter(userRepository, ImmediateSchedulerProvider(), errorHandler)
        presenter.attachView(view)
    }

    @After
    fun finish() {
        presenter.stop()
        presenter.detachView()
    }

    @Test
    fun `Should navigate to sign up`() {
        presenter.onClickSignUp()

        verify(view, only()).navigateToSignUp()
    }

    @Test
    fun `Should navigate to forgot password`() {
        presenter.onClickForgotPassword()

        verify(view, only()).navigateToForgotPassword()
    }

    @Test
    fun `Should not try to sign in if the credentials fields are invalid`() {
        val credentials = Credentials("john.doe@acme.com", "")

        `when`(view.validateCredentialsFields()).thenReturn(false)

        presenter.onClickSignIn(credentials)

        verify(userRepository, never()).signInUser(credentials)
        verify(view, never()).navigateToHome()
    }

    @Test
    fun `Should try to sign in if the credentials fields are valid`() {
        val credentials = Credentials("john.doe@acme.com", "123456")

        `when`(view.validateCredentialsFields()).thenReturn(true)
        `when`(userRepository.signInUser(credentials)).thenReturn(Single.just(User()))

        presenter.onClickSignIn(credentials)

        verify(userRepository, only()).signInUser(credentials)
    }

    @Test
    fun `Should show a specific message when the user doesn't exists`() {
        val credentials = Credentials("john.doe@acme.com", "123456")
        val error = EmptyResultSetException("User doesn't exists")

        `when`(view.validateCredentialsFields()).thenReturn(true)
        `when`(userRepository.signInUser(credentials)).thenReturn(Single.error(error))

        presenter.onClickSignIn(credentials)

        verify(view).showMessage(R.string.msg_wrong_credentials)
        verify(view, never()).navigateToHome()
    }

    @Test
    fun `Should show a specific message when the credentials are wrong`() {
        val credentials = Credentials("john.doe@acme.com", "123456")
        val error = IllegalArgumentException("Wrong password")

        `when`(view.validateCredentialsFields()).thenReturn(true)
        `when`(userRepository.signInUser(credentials)).thenReturn(Single.error(error))

        presenter.onClickSignIn(credentials)

        verify(view).showMessage(R.string.msg_wrong_credentials)
        verify(view, never()).navigateToHome()
    }

    @Test
    fun `Should show a default message when an unknown error occurs`() {
        val credentials = Credentials("john.doe@acme.com", "123456")
        val error = Throwable("Unknown error")

        `when`(view.validateCredentialsFields()).thenReturn(true)
        `when`(userRepository.signInUser(credentials)).thenReturn(Single.error(error))

        presenter.onClickSignIn(credentials)

        verify(view).showMessage(R.string.msg_cannot_signin)
        verify(view, never()).navigateToHome()
    }

    @Test
    fun `Should navigate to home if the credentials are okay`() {
        val credentials = Credentials("john.doe@acme.com", "654321")
        val user = User("John Doe", "john.doe@acme.org")

        `when`(view.validateCredentialsFields()).thenReturn(true)
        `when`(userRepository.signInUser(credentials)).thenReturn(Single.just(user))

        presenter.onClickSignIn(credentials)

        verify(view, times(1)).navigateToHome()
    }

}
