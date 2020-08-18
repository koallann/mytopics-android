package me.koallann.myagenda.presentation.topics

import io.reactivex.disposables.CompositeDisposable
import me.koallann.myagenda.R
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
    private val filter: Topic.Status,
    private val topicRepository: TopicRepository,
    private val userRepository: UserRepository,
    private val schedulerProvider: SchedulerProvider,
    private val errorHandler: ErrorHandler
) : Presenter<TopicsView>(TopicsView::class.java) {

    private val disposables: CompositeDisposable = CompositeDisposable()
    private var previousClickedTopic: Topic? = null

    override fun stop() {
        super.stop()
        disposables.clear()
    }

    fun onLoadUser() {
        userRepository.getSignedUser()?.let { view?.onUserLoaded(it) }
    }

    fun onLoadTopics() {
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
        if (topic == previousClickedTopic) {
            view?.uncollapseTopic(topic)
            previousClickedTopic = null
            return
        }
        previousClickedTopic?.let { view?.uncollapseTopic(it) }
        previousClickedTopic = topic
        view?.collapseTopic(topic)
    }

    fun onClickCloseTopic(topic: Topic) {
        if (filter != Topic.Status.OPEN) {
            return
        }
        if (!isTopicUpdating(topic)) {
            view?.onConfirmCloseTopic(topic)
        }
    }

    fun closeTopic(topic: Topic) {
        topicsBeingUpdated.add(topic)

        topicRepository.updateTopic(topic.copy(status = Topic.Status.CLOSED))
            .fromIoToUiThread(schedulerProvider)
            .doOnSubscribe { view?.showMessage(R.string.label_closing_topic) }
            .doFinally { removeTopicFromUpdating(topic) }
            .subscribe(
                {
                    topic.status = Topic.Status.CLOSED
                    view?.onTopicClosed(topic)
                },
                { throwable -> view?.let { errorHandler.showMessageForError(it, throwable) } }
            )
            .addTo(disposables)
    }

    fun onClickReopenTopic(topic: Topic) {
        if (filter != Topic.Status.CLOSED) {
            return
        }
        if (!isTopicUpdating(topic)) {
            view?.onConfirmReopenTopic(topic)
        }
    }

    fun reopenTopic(topic: Topic) {
        topicsBeingUpdated.add(topic)

        topicRepository.updateTopic(topic.copy(status = Topic.Status.OPEN))
            .fromIoToUiThread(schedulerProvider)
            .doOnSubscribe { view?.showMessage(R.string.label_reopening_topic) }
            .doFinally { removeTopicFromUpdating(topic) }
            .subscribe(
                {
                    topic.status = Topic.Status.OPEN
                    view?.onTopicReopened(topic)
                },
                { throwable -> view?.let { errorHandler.showMessageForError(it, throwable) } }
            )
            .addTo(disposables)
    }

    // naive handling of topics being updated

    private var topicsBeingUpdated: MutableList<Topic> = arrayListOf()

    private fun isTopicUpdating(topic: Topic): Boolean {
        return topicsBeingUpdated.any { it.id == topic.id }
    }

    private fun removeTopicFromUpdating(topic: Topic) = synchronized(this) {
        for (i in topicsBeingUpdated.indices) {
            if (topicsBeingUpdated[i].id == topic.id) {
                topicsBeingUpdated.removeAt(i)
                break
            }
        }
    }

}
