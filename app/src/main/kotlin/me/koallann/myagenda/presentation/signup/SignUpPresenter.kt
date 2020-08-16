package me.koallann.myagenda.presentation.signup

import me.koallann.myagenda.domain.User
import me.koallann.support.mvp.Presenter

class SignUpPresenter : Presenter<SignUpView>(SignUpView::class.java) {

    fun onClickSignUp(user: User) {
        if (view?.validateUserFields() != true) {
            return
        }
    }

}
