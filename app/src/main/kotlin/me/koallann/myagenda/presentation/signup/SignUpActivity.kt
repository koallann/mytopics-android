package me.koallann.myagenda.presentation.signup

import android.content.Context
import android.content.Intent
import android.view.View
import me.koallann.myagenda.databinding.ActivitySignupBinding
import me.koallann.support.ui.BaseActivity

class SignUpActivity : BaseActivity() {

    companion object {
        fun createIntent(context: Context) = Intent(context, SignUpActivity::class.java)
    }

    private val binding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }

    override fun getContentView(): View = binding.root


}
