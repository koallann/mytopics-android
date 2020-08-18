package me.koallann.mytopics.presentation.signin

import me.koallann.support.mvp.BasicView

interface SignInView : BasicView {

    fun validateCredentialsFields(): Boolean

    fun navigateToSignUp()

    fun navigateToForgotPassword()

    fun navigateToHome()

}
