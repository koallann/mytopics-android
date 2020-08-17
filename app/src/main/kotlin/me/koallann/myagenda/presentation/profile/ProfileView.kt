package me.koallann.myagenda.presentation.profile

import me.koallann.myagenda.domain.User
import me.koallann.support.mvp.BasicView

interface ProfileView : BasicView {

    fun onUserLoaded(user: User)

    fun onConfirmSignOut()

    fun navigateToLogin()

}
