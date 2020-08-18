package me.koallann.myagenda.presentation.forgotpassword

import me.koallann.myagenda.R
import me.koallann.support.handlers.ErrorHandler
import me.koallann.support.mvp.BasicView

class ForgotPasswordErrorHandler : ErrorHandler {

    override fun showMessageForError(view: BasicView, error: Throwable) {
        view.showMessage(
            when (error) {
                is IllegalArgumentException -> R.string.msg_email_not_found
                else -> R.string.msg_cannot_recover_email
            }
        )
    }

}
