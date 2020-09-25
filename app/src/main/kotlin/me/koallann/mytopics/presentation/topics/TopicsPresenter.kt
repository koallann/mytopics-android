package me.koallann.mytopics.presentation.topics

import androidx.annotation.StringRes
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

class TopicsPresenter(
    private val filter: Topic.Status,
    private val topicRepository: TopicRepository,
    private val userRepository: UserRepository,
    private val schedulerProvider: SchedulerProvider,
    private val errorHandler: ErrorHandler
) : Presenter<TopicsView>(TopicsView::class.java) {

    private val disposables: CompositeDisposable = CompositeDisposable()
    private var previousClickedTopic: Topic? = null

    override fun detachView() {
        disposables.clear()
        super.detachView()
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
        updateTopic(topic, Topic.Status.CLOSED, R.string.label_closing_topic) {
            view?.onTopicClosed(topic)
        }
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
        updateTopic(topic, Topic.Status.OPEN, R.string.label_reopening_topic) {
            view?.onTopicReopened(topic)
        }
    }

    private fun updateTopic(
        topic: Topic,
        toStatus: Topic.Status,
        @StringRes loadingMessageRes: Int,
        onUpdated: () -> Unit
    ) {
        topicRepository.updateTopic(topic.copy(status = toStatus))
            .fromIoToUiThread(schedulerProvider)
            .doOnSubscribe {
                addTopicToUpdating(topic)
                view?.showMessage(loadingMessageRes)
            }
            .doFinally {
                removeTopicFromUpdating(topic)
            }
            .subscribe(
                {
                    topic.status = toStatus
                    onUpdated()
                },
                { throwable -> view?.let { errorHandler.showMessageForError(it, throwable) } }
            )
            .addTo(disposables)
    }

    private var topicsBeingUpdated: MutableMap<Int, Boolean> = hashMapOf()

    private fun isTopicUpdating(topic: Topic): Boolean {
        return topicsBeingUpdated.contains(topic.id)
    }

    private fun addTopicToUpdating(topic: Topic) {
        topicsBeingUpdated[topic.id] = true
    }

    private fun removeTopicFromUpdating(topic: Topic) {
        topicsBeingUpdated.remove(topic.id)
    }

}
