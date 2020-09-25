package me.koallann.mytopics.presentation.profile

import io.reactivex.disposables.CompositeDisposable
import me.koallann.mytopics.R
import me.koallann.mytopics.data.user.UserRepository
import me.koallann.support.extensions.addTo
import me.koallann.support.extensions.fromIoToUiThread
import me.koallann.support.extensions.setLoadingView
import me.koallann.support.mvp.Presenter
import me.koallann.support.rxschedulers.SchedulerProvider

class ProfilePresenter(
    private val userRepository: UserRepository,
    private val schedulerProvider: SchedulerProvider
) : Presenter<ProfileView>(ProfileView::class.java) {

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun detachView() {
        disposables.clear()
        super.detachView()
    }

    fun onLoadUser() {
        userRepository.getSignedUser()?.let { view?.onUserLoaded(it) }
    }

    fun onClickSignOut() {
        view?.onConfirmSignOut()
    }

    fun signOutUser() {
        userRepository.signOutUser()
            .fromIoToUiThread(schedulerProvider)
            .setLoadingView(view)
            .doOnSubscribe { view?.showMessage(R.string.msg_exiting) }
            .subscribe { view?.navigateToLogin() }
            .addTo(disposables)
    }

}
