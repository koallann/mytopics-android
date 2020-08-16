package me.koallann.myagenda.presentation.forgotpassword

import io.reactivex.disposables.CompositeDisposable
import me.koallann.myagenda.data.user.UserRepository
import me.koallann.support.extensions.addTo
import me.koallann.support.extensions.fromIoToUiThread
import me.koallann.support.extensions.setLoadingView
import me.koallann.support.handlers.ErrorHandler
import me.koallann.support.mvp.Presenter
import me.koallann.support.rxschedulers.SchedulerProvider

class ForgotPasswordPresenter(
    private val userRepository: UserRepository,
    private val schedulerProvider: SchedulerProvider,
    private val errorHandler: ErrorHandler
) : Presenter<ForgotPasswordView>(ForgotPasswordView::class.java) {

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun stop() {
        super.stop()
        disposables.clear()
    }

    fun onClickSendRecoveryEmail(toEmail: String) {
        if (view?.validateEmailField() != true) {
            return
        }
        userRepository.sendRecoveryEmail(toEmail)
            .fromIoToUiThread(schedulerProvider)
            .setLoadingView(view)
            .subscribe(
                { view?.onRecoveryEmailSent() },
                { throwable ->
                    view?.let { errorHandler.showMessageForError(it, throwable) }
                }
            )
            .addTo(disposables)
    }

}
