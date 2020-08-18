package me.koallann.mytopics.presentation.profile

import me.koallann.mytopics.domain.User
import me.koallann.support.mvp.BasicView

interface ProfileView : BasicView {

    fun onUserLoaded(user: User)

    fun onConfirmSignOut()

    fun navigateToLogin()

}
