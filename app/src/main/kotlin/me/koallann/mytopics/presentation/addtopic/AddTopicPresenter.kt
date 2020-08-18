package me.koallann.mytopics.presentation.addtopic

import io.reactivex.disposables.CompositeDisposable
import me.koallann.mytopics.R
import me.koallann.mytopics.data.topic.TopicRepository
import me.koallann.mytopics.data.user.UserRepository
import me.koallann.mytopics.domain.Topic
import me.koallann.support.extensions.addTo
import me.koallann.support.extensions.fromIoToUiThread
import me.koallann.support.extensions.setLoadingView
import me.koallann.support.handlers.ErrorHandler
import me.koallann.support.mvp.Presenter
import me.koallann.support.rxschedulers.SchedulerProvider

class AddTopicPresenter(
    private val topicRepository: TopicRepository,
    private val userRepository: UserRepository,
    private val schedulerProvider: SchedulerProvider,
    private val errorHandler: ErrorHandler
) : Presenter<AddTopicView>(AddTopicView::class.java) {

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun stop() {
        super.stop()
        disposables.clear()
    }

    fun onClickAddTopic(topic: Topic) {
        if (view?.validateTopicFields() != true) {
            return
        }
        val signedUser = userRepository.getSignedUser()
        if (signedUser == null) {
            view?.showMessage(R.string.msg_no_user_signed)
            return
        }
        topic.author = signedUser
        topic.status = Topic.Status.OPEN

        topicRepository.createTopic(topic)
            .fromIoToUiThread(schedulerProvider)
            .setLoadingView(view)
            .subscribe(
                { view?.onTopicAdded(topic) },
                { throwable -> view?.let { errorHandler.showMessageForError(it, throwable) } }
            )
            .addTo(disposables)
    }

}
