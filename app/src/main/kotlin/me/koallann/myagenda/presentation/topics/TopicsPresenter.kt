package me.koallann.myagenda.presentation.topics

import io.reactivex.disposables.CompositeDisposable
import me.koallann.myagenda.data.topic.TopicRepository
import me.koallann.myagenda.data.user.UserRepository
import me.koallann.myagenda.domain.Topic
import me.koallann.support.extensions.addTo
import me.koallann.support.extensions.fromIoToUiThread
import me.koallann.support.extensions.setLoadingView
import me.koallann.support.handlers.ErrorHandler
import me.koallann.support.mvp.Presenter
import me.koallann.support.rxschedulers.SchedulerProvider

class TopicsPresenter(
    private val topicRepository: TopicRepository,
    private val userRepository: UserRepository,
    private val schedulerProvider: SchedulerProvider,
    private val errorHandler: ErrorHandler
) : Presenter<TopicsView>(TopicsView::class.java) {

    private val disposables: CompositeDisposable = CompositeDisposable()
    private var lastSelectedTopic: Topic? = null

    override fun stop() {
        super.stop()
        disposables.clear()
    }

    fun onLoadUser() {
        userRepository.getSignedUser()?.let { view?.onUserLoaded(it) }
    }

    fun onLoadTopics(filter: Topic.Status) {
        topicRepository.getTopicsByStatus(filter)
            .fromIoToUiThread(schedulerProvider)
            .setLoadingView(view)
            .subscribe(
                { view?.addTopics(it) },
                { throwable -> view?.let { errorHandler.showMessageForError(it, throwable) } }
            )
            .addTo(disposables)
    }

    fun onClickTopic(topic: Topic) {
        if (topic == lastSelectedTopic) {
            view?.uncollapseTopic(topic)
            lastSelectedTopic = null
            return
        }
        lastSelectedTopic?.let { view?.uncollapseTopic(it) }
        lastSelectedTopic = topic
        view?.collapseTopic(topic)
    }

    fun onClickCloseTopic(topic: Topic) {
        view?.onConfirmCloseTopic(topic)
    }

    fun closeTopic(topic: Topic) {
        topic.status = Topic.Status.CLOSED

        topicRepository.updateTopic(topic)
            .fromIoToUiThread(schedulerProvider)
            .setLoadingView(view)
            .subscribe(
                { view?.onTopicClosed(topic) },
                { throwable -> view?.let { errorHandler.showMessageForError(it, throwable) } }
            )
            .addTo(disposables)
    }

    fun onClickReopenTopic(topic: Topic) {
        view?.onConfirmReopenTopic(topic)
    }

    fun reopenTopic(topic: Topic) {
        topic.status = Topic.Status.OPEN

        topicRepository.updateTopic(topic)
            .fromIoToUiThread(schedulerProvider)
            .setLoadingView(view)
            .subscribe(
                { view?.onTopicReopened(topic) },
                { throwable -> view?.let { errorHandler.showMessageForError(it, throwable) } }
            )
            .addTo(disposables)
    }

}
