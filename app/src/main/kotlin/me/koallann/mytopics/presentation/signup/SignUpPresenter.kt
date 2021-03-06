package me.koallann.mytopics.presentation.signup

import io.reactivex.disposables.CompositeDisposable
import me.koallann.mytopics.data.user.UserRepository
import me.koallann.mytopics.domain.User
import me.koallann.support.extensions.addTo
import me.koallann.support.extensions.fromIoToUiThread
import me.koallann.support.extensions.setLoadingView
import me.koallann.support.handlers.ErrorHandler
import me.koallann.support.mvp.Presenter
import me.koallann.support.rxschedulers.SchedulerProvider

class SignUpPresenter(
    private val userRepository: UserRepository,
    private val schedulerProvider: SchedulerProvider,
    private val errorHandler: ErrorHandler
) : Presenter<SignUpView>(SignUpView::class.java) {

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun detachView() {
        disposables.clear()
        super.detachView()
    }

    fun onClickSignUp(user: User) {
        if (view?.validateUserFields() != true) {
            return
        }
        userRepository.signUpUser(user)
            .fromIoToUiThread(schedulerProvider)
            .setLoadingView(view)
            .subscribe(
                {
                    view?.navigateToSignIn()
                },
                { throwable ->
                    view?.let { errorHandler.showMessageForError(it, throwable) }
                }
            )
            .addTo(disposables)
    }

}
