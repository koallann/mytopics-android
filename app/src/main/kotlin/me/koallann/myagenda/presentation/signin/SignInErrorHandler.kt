package me.koallann.myagenda.presentation.signin

import androidx.room.EmptyResultSetException
import me.koallann.myagenda.R
import me.koallann.support.handlers.ErrorHandler
import me.koallann.support.mvp.BasicView

class SignInErrorHandler : ErrorHandler {

    override fun showMessageForError(view: BasicView, error: Throwable) {
        val messageRes = when (error) {
            is EmptyResultSetException -> R.string.msg_invalid_credentials
            is IllegalArgumentException -> R.string.msg_invalid_credentials
            else -> R.string.app_name
        }
        view.showMessage(messageRes)
    }

}
