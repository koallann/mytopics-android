package me.koallann.mytopics.presentation.topics

import me.koallann.mytopics.R
import me.koallann.support.handlers.ErrorHandler
import me.koallann.support.mvp.BasicView

class TopicsErrorHandler : ErrorHandler {

    override fun showMessageForError(view: BasicView, error: Throwable) {
        view.showMessage(R.string.msg_topic_error)
    }

}
