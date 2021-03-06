package me.koallann.mytopics.presentation.signup

import me.koallann.support.mvp.BasicView

interface SignUpView : BasicView {

    fun validateUserFields(): Boolean

    fun navigateToSignIn()

}
