package me.koallann.mytopics.presentation.signup

import android.database.sqlite.SQLiteConstraintException
import me.koallann.mytopics.R
import me.koallann.support.handlers.ErrorHandler
import me.koallann.support.mvp.BasicView

class SignUpErrorHandler : ErrorHandler {

    override fun showMessageForError(view: BasicView, error: Throwable) {
        val messageRes = when (error) {
            is SQLiteConstraintException -> R.string.msg_email_already_used
            else -> R.string.msg_cannot_signup
        }
        view.showMessage(messageRes)
    }

}
