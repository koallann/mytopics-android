package me.koallann.myagenda.presentation.signin

import androidx.room.EmptyResultSetException
import me.koallann.myagenda.R
import me.koallann.support.handlers.ErrorHandler
import me.koallann.support.mvp.BasicView

class SignInErrorHandler : ErrorHandler {

    override fun showMessageForError(view: BasicView, error: Throwable) {
        val messageRes = when (error) {
            is EmptyResultSetException -> R.string.msg_wrong_credentials
            is IllegalArgumentException -> R.string.msg_wrong_credentials
            else -> R.string.msg_cannot_signin
        }
        view.showMessage(messageRes)
    }

}
