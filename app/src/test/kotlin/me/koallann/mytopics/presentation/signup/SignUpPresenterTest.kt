package me.koallann.mytopics.presentation.signup

import android.database.sqlite.SQLiteConstraintException
import io.reactivex.Single
import me.koallann.mytopics.R
import me.koallann.mytopics.data.user.UserRepository
import me.koallann.mytopics.domain.User
import me.koallann.support.handlers.ErrorHandler
import me.koallann.support.rxschedulers.ImmediateSchedulerProvider
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class SignUpPresenterTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var view: SignUpView

    private lateinit var errorHandler: ErrorHandler
    private lateinit var presenter: SignUpPresenter

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        errorHandler = SignUpErrorHandler()
        presenter = SignUpPresenter(userRepository, ImmediateSchedulerProvider(), errorHandler)
        presenter.attachView(view)
    }

    @After
    fun finish() {
        presenter.stop()
        presenter.detachView()
    }

    @Test
    fun `Should not try to sign up if the user fields are invalid`() {
        val user = User(name = "John Doe", email = "john.doe@acme.com")

        `when`(view.validateUserFields()).thenReturn(false)

        presenter.onClickSignUp(user)

        verify(userRepository, never()).signUpUser(user)
        verify(view, never()).navigateToSignIn()
    }

    @Test
    fun `Should try to sign up if the user fields fields are valid`() {
        val user = User(name = "John Doe", email = "john.doe@acme.com", secret = User.Secret("123456"))

        `when`(view.validateUserFields()).thenReturn(true)
        `when`(userRepository.signUpUser(user)).thenReturn(Single.just(User()))

        presenter.onClickSignUp(user)

        verify(userRepository, only()).signUpUser(user)
    }

    @Test
    fun `Should show a specific message when the given email is already in use`() {
        val user = User(name = "John Doe", email = "john.doe@acme.com", secret = User.Secret("123456"))
        val error = SQLiteConstraintException("This email is already in use")

        `when`(view.validateUserFields()).thenReturn(true)
        `when`(userRepository.signUpUser(user)).thenReturn(Single.error(error))

        presenter.onClickSignUp(user)

        verify(view).showMessage(R.string.msg_email_already_used)
        verify(view, never()).navigateToSignIn()
    }

    @Test
    fun `Should show a default message when an unknown error occurs`() {
        val user = User(name = "John Doe", email = "john.doe@acme.com", secret = User.Secret("123456"))
        val error = Throwable("Unknown error")

        `when`(view.validateUserFields()).thenReturn(true)
        `when`(userRepository.signUpUser(user)).thenReturn(Single.error(error))

        presenter.onClickSignUp(user)

        verify(view).showMessage(R.string.msg_cannot_signup)
        verify(view, never()).navigateToSignIn()
    }

    @Test
    fun `Should sign up if the user fields are okay`() {
        val user = User(name = "John Doe", email = "john.doe@acme.com", secret = User.Secret("123456"))

        `when`(view.validateUserFields()).thenReturn(true)
        `when`(userRepository.signUpUser(user)).thenReturn(Single.just(User(name = user.name, email = user.email)))

        presenter.onClickSignUp(user)

        verify(view, times(1)).navigateToSignIn()
    }

}
