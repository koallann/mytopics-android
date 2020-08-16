package me.koallann.myagenda.presentation.signin

import io.reactivex.disposables.CompositeDisposable
import me.koallann.myagenda.data.user.UserRepository
import me.koallann.myagenda.domain.Credentials
import me.koallann.support.extensions.addTo
import me.koallann.support.extensions.fromIoToUiThread
import me.koallann.support.extensions.setLoadingView
import me.koallann.support.handlers.ErrorHandler
import me.koallann.support.mvp.Presenter
import me.koallann.support.rxschedulers.SchedulerProvider

class SignInPresenter(
    private val userRepository: UserRepository,
    private val schedulerProvider: SchedulerProvider,
    private val errorHandler: ErrorHandler
) : Presenter<SignInView>(SignInView::class.java) {

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun stop() {
        super.stop()
        disposables.clear()
    }

    fun onCheckSignedUser() {
        if (userRepository.hasUserSigned()) {
            view?.navigateToHome()
        }
    }

    fun onClickSignIn(credentials: Credentials) {
        if (view?.validateCredentialsFields() != true) {
            return
        }
        userRepository.signInUser(credentials)
            .fromIoToUiThread(schedulerProvider)
            .setLoadingView(view)
            .subscribe(
                {
                    view?.navigateToHome()
                },
                { throwable ->
                    view?.let { errorHandler.showMessageForError(it, throwable) }
                }
            )
            .addTo(disposables)
    }

    fun onClickSignUp() {
        view?.navigateToSignUp()
    }

    fun onClickForgotPassword() {
        view?.navigateToForgotPassword()
    }

}
