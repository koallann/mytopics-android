package me.koallann.myagenda.shared.mvp

import androidx.annotation.StringRes

interface BasicView {

    fun showLoading()

    fun dismissLoading()

    fun showMessage(message: String)

    fun showMessage(@StringRes messageRes: Int)

}
