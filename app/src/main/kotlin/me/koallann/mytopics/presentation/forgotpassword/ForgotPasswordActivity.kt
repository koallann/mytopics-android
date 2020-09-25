package me.koallann.mytopics.presentation.forgotpassword

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import br.com.ilhasoft.support.validation.Validator
import me.koallann.mytopics.R
import me.koallann.mytopics.data.user.UserRepositoryImpl
import me.koallann.mytopics.databinding.ActivityForgotPasswordBinding
import me.koallann.mytopics.data_db.user.UserDaoClient
import me.koallann.support.rxschedulers.StandardSchedulerProvider
import me.koallann.support.ui.BaseActivity

class ForgotPasswordActivity : BaseActivity(), ForgotPasswordView {

    companion object {
        fun createIntent(context: Context): Intent =
            Intent(context, ForgotPasswordActivity::class.java)
    }

    private val binding: ActivityForgotPasswordBinding by lazy {
        ActivityForgotPasswordBinding.inflate(layoutInflater)
    }
    private val validator: Validator by lazy {
        Validator(binding)
    }
    private val presenter: ForgotPasswordPresenter by lazy {
        ForgotPasswordPresenter(
            UserRepositoryImpl(UserDaoClient(this)),
            StandardSchedulerProvider(),
            ForgotPasswordErrorHandler()
        )
    }

    override fun getContentView(): View = binding.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
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

    override fun validateEmailField(): Boolean = validator.validate()

    override fun onRecoveryEmailSent() {
        Toast.makeText(this, R.string.msg_recovery_email_sent, Toast.LENGTH_LONG).show()
        finish()
    }

    private fun setupLayout() {
        supportActionBar?.setTitle(R.string.label_recover_account)
        binding.also {
            it.presenter = presenter
            it.toEmail = ""
        }
    }

    private fun setFormEnabled(enabled: Boolean) {
        binding.apply {
            email.isEnabled = enabled
            sendEmail.isEnabled = enabled
            sendEmail.setText(
                if (enabled) R.string.label_send_email
                else R.string.label_loading
            )
        }
    }

}
