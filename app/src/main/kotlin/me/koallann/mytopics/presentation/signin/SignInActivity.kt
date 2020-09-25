package me.koallann.mytopics.presentation.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import br.com.ilhasoft.support.validation.Validator
import me.koallann.mytopics.R
import me.koallann.mytopics.data.user.UserRepositoryImpl
import me.koallann.mytopics.databinding.ActivitySigninBinding
import me.koallann.mytopics.domain.Credentials
import me.koallann.mytopics.data_db.user.UserDaoClient
import me.koallann.mytopics.presentation.forgotpassword.ForgotPasswordActivity
import me.koallann.mytopics.presentation.home.HomeActivity
import me.koallann.mytopics.presentation.signup.SignUpActivity
import me.koallann.support.rxschedulers.StandardSchedulerProvider
import me.koallann.mytopics.presentation.base.BaseActivity

class  SignInActivity : BaseActivity(), SignInView {

    companion object {
        fun createIntent(context: Context) = Intent(context, SignInActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

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
        setupLayout()
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

    private fun setupLayout() {
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
