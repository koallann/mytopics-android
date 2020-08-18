package me.koallann.myagenda.presentation.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import me.koallann.myagenda.R
import me.koallann.myagenda.data.user.UserRepository
import me.koallann.myagenda.data.user.UserRepositoryImpl
import me.koallann.myagenda.databinding.ActivityProfileBinding
import me.koallann.myagenda.domain.User
import me.koallann.myagenda.local.user.UserDaoClient
import me.koallann.myagenda.presentation.signin.SignInActivity
import me.koallann.support.rxschedulers.StandardSchedulerProvider
import me.koallann.support.ui.BaseActivity

class ProfileActivity : BaseActivity(), ProfileView {

    companion object {
        fun createIntent(context: Context) = Intent(context, ProfileActivity::class.java)
    }

    private val binding: ActivityProfileBinding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }
    private val presenter: ProfilePresenter by lazy {
        ProfilePresenter(
            UserRepositoryImpl(UserDaoClient(this)),
            StandardSchedulerProvider()
        )
    }

    override fun getContentView(): View = binding.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
        setupLayout()
        presenter.onLoadUser()
    }

    override fun onDestroy() {
        presenter.stop()
        presenter.detachView()
        super.onDestroy()
    }

    override fun showLoading() {
        binding.signout.isEnabled = false
    }

    override fun dismissLoading() {
        binding.signout.isEnabled = true
    }

    override fun onUserLoaded(user: User) {
        binding.also {
            it.user = user
        }
    }

    override fun onConfirmSignOut() {
        AlertDialog.Builder(this)
            .setTitle(R.string.label_signout)
            .setMessage(R.string.msg_signout)
            .setPositiveButton(R.string.label_ok) { dialog, _ ->
                dialog.dismiss()
                presenter.signOutUser()
            }
            .setNegativeButton(R.string.label_cancel) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun navigateToLogin() {
        startActivity(SignInActivity.createIntent(this))
    }

    private fun setupLayout() {
        binding.also {
            it.presenter = presenter
        }
    }

}
