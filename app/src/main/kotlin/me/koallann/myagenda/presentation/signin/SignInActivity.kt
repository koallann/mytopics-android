package me.koallann.myagenda.presentation.signin

import android.view.View
import me.koallann.myagenda.databinding.ActivitySigninBinding
import me.koallann.support.ui.BaseActivity

class SignInActivity : BaseActivity() {

    private val binding: ActivitySigninBinding by lazy {
        ActivitySigninBinding.inflate(layoutInflater)
    }

    override fun getContentView(): View = binding.root

}
