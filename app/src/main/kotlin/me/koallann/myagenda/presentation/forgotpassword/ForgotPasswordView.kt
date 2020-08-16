package me.koallann.myagenda.presentation.forgotpassword

import me.koallann.support.mvp.BasicView

interface ForgotPasswordView : BasicView {

    fun validateEmailField(): Boolean

    fun onRecoveryEmailSent()

}
