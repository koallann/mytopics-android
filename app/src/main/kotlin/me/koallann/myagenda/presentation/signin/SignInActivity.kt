package me.koallann.myagenda.presentation.signin

import android.os.Bundle
import android.view.View
import br.com.ilhasoft.support.validation.Validator
import me.koallann.myagenda.R
import me.koallann.myagenda.data.user.UserRepositoryImpl
import me.koallann.myagenda.databinding.ActivitySigninBinding
import me.koallann.myagenda.domain.Credentials
import me.koallann.myagenda.local.user.UserDaoClient
import me.koallann.myagenda.presentation.forgotpassword.ForgotPasswordActivity
import me.koallann.myagenda.presentation.home.HomeActivity
import me.koallann.myagenda.presentation.signup.SignUpActivity
import me.koallann.support.rxschedulers.StandardSchedulerProvider
import me.koallann.support.ui.BaseActivity

class  SignInActivity : BaseActivity(), SignInView {

    private val binding: ActivitySigninBinding by lazy {
        ActivitySigninBinding.inflate(layoutInflater)
    }
    private val validator: Validator by lazy {
        Validator(binding)
    }
    private val presenter: SignInPresenter by lazy {
        SignInPresenter(
            UserRepositoryImpl(UserDaoClient(this)),
            StandardSchedulerProvider(),
            SignInErrorHandler()
        )
    }

    override fun getContentView(): View = binding.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
        presenter.onCheckSignedUser()
        setupUI()
    }

    override fun onDestroy() {
        presenter.stop()
        presenter.detachView()
        super.onDestroy()
    }

    override fun showLoading() {
        setFormEnabled(false)
    }

    override fun dismissLoading() {
        setFormEnabled(true)
    }

    override fun validateCredentialsFields(): Boolean = validator.validate()

    override fun navigateToSignUp() {
        startActivity(SignUpActivity.createIntent(this))
    }

    override fun navigateToForgotPassword() {
        startActivity(ForgotPasswordActivity.createIntent(this))
    }

    override fun navigateToHome() {
        startActivity(HomeActivity.createIntent(this))
    }

    private fun setupUI() {
        binding.also {
            it.presenter = presenter
            it.credentials = Credentials()
        }
    }

    private fun setFormEnabled(enabled: Boolean) {
        binding.apply {
            email.isEnabled = enabled
            password.isEnabled = enabled
            signin.isEnabled = enabled
            signin.setText(
                if (enabled) R.string.label_signin
                else R.string.label_loading
            )
        }
    }

}
