package me.koallann.mytopics.presentation.addtopic

import me.koallann.mytopics.R
import me.koallann.support.handlers.ErrorHandler
import me.koallann.support.mvp.BasicView

class AddTopicErrorHandler : ErrorHandler {

    override fun showMessageForError(view: BasicView, error: Throwable) {
        view.showMessage(R.string.msg_cannot_add_topic)
    }

}
