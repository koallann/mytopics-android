package me.koallann.myagenda.presentation.forgotpassword

import io.reactivex.Completable
import me.koallann.myagenda.R
import me.koallann.myagenda.data.user.UserRepository
import me.koallann.support.handlers.ErrorHandler
import me.koallann.support.rxschedulers.ImmediateSchedulerProvider
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class ForgotPasswordPresenterTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var view: ForgotPasswordView

    private lateinit var errorHandler: ErrorHandler
    private lateinit var presenter: ForgotPasswordPresenter

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        errorHandler = ForgotPasswordErrorHandler()
        presenter = ForgotPasswordPresenter(userRepository, ImmediateSchedulerProvider(), errorHandler)
        presenter.attachView(view)
    }

    @After
    fun finish() {
        presenter.stop()
        presenter.detachView()
    }

    @Test
    fun `Should not try to send recovery email if the recipient email field is invalid`() {
        val toEmail = "john.doe@"

        `when`(view.validateEmailField()).thenReturn(false)

        presenter.onClickSendRecoveryEmail(toEmail)

        verify(userRepository, never()).sendRecoveryEmail(toEmail)
        verify(view, never()).onRecoveryEmailSent()
    }

    @Test
    fun `Should try to send recovery email if the recipient email is valid`() {
        val toEmail = "john.doe@acme.com"

        `when`(view.validateEmailField()).thenReturn(true)
        `when`(userRepository.sendRecoveryEmail(toEmail)).thenReturn(Completable.complete())

        presenter.onClickSendRecoveryEmail(toEmail)

        verify(userRepository, only()).sendRecoveryEmail(toEmail)
        verify(view, times(1)).onRecoveryEmailSent()
    }

    @Test
    fun `Should show a specific message when the email doesn't exists`() {
        val toEmail = "john.doe@acme.com"
        val error = IllegalArgumentException("User doesn't exists")

        `when`(view.validateEmailField()).thenReturn(true)
        `when`(userRepository.sendRecoveryEmail(toEmail)).thenReturn(Completable.error(error))

        presenter.onClickSendRecoveryEmail(toEmail)

        verify(view).showMessage(R.string.msg_email_not_found)
    }

    @Test
    fun `Should show a default message when an unknown error occurs`() {
        val toEmail = "john.doe@acme.com"
        val error = Throwable("Unknown error")

        `when`(view.validateEmailField()).thenReturn(true)
        `when`(userRepository.sendRecoveryEmail(toEmail)).thenReturn(Completable.error(error))

        presenter.onClickSendRecoveryEmail(toEmail)

        verify(view).showMessage(R.string.msg_cannot_recover_email)
    }

}
