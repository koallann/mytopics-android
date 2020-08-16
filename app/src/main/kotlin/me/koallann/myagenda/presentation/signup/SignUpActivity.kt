package me.koallann.myagenda.presentation.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import br.com.ilhasoft.support.validation.Validator
import me.koallann.myagenda.databinding.ActivitySignupBinding
import me.koallann.myagenda.domain.User
import me.koallann.support.ui.BaseActivity

class SignUpActivity : BaseActivity(), SignUpView {

    companion object {
        fun createIntent(context: Context) = Intent(context, SignUpActivity::class.java)
    }

    private val binding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }
    private val validator: Validator by lazy {
        Validator(binding)
    }
    private val presenter: SignUpPresenter by lazy {
        SignUpPresenter()
    }

    override fun getContentView(): View = binding.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
        setupUI()
    }

    override fun onDestroy() {
        presenter.stop()
        presenter.detachView()
        super.onDestroy()
    }

    override fun validateUserFields(): Boolean = validator.validate()

    override fun navigateToHome() {
        TODO("Not yet implemented")
    }

    private fun setupUI() {
        binding.also {
            it.presenter = presenter
            it.user = User(secret = User.Secret())
        }
    }

}
