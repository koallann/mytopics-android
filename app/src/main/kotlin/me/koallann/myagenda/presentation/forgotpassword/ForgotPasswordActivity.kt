package me.koallann.myagenda.presentation.forgotpassword

import android.content.Context
import android.content.Intent
import android.view.View
import me.koallann.myagenda.databinding.ActivityForgotPasswordBinding
import me.koallann.support.mvp.BasicView
import me.koallann.support.ui.BaseActivity

class ForgotPasswordActivity : BaseActivity(), BasicView {

    companion object {
        fun createIntent(context: Context): Intent =
            Intent(context, ForgotPasswordActivity::class.java)
    }

    private val binding: ActivityForgotPasswordBinding by lazy {
        ActivityForgotPasswordBinding.inflate(layoutInflater)
    }

    override fun getContentView(): View = binding.root

}
