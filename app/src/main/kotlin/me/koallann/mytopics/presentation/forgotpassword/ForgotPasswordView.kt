package me.koallann.mytopics.presentation.forgotpassword

import me.koallann.support.mvp.BasicView

interface ForgotPasswordView : BasicView {

    fun validateEmailField(): Boolean

    fun onRecoveryEmailSent()

}
