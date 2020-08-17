package me.koallann.myagenda.presentation.addtopic

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import br.com.ilhasoft.support.validation.Validator
import me.koallann.myagenda.R
import me.koallann.myagenda.data.topic.TopicRepositoryImpl
import me.koallann.myagenda.data.user.UserRepositoryImpl
import me.koallann.myagenda.databinding.ActivityAddTopicBinding
import me.koallann.myagenda.domain.Topic
import me.koallann.myagenda.local.topic.TopicDaoClient
import me.koallann.myagenda.local.user.UserDaoClient
import me.koallann.support.rxschedulers.StandardSchedulerProvider
import me.koallann.support.ui.BaseActivity

class AddTopicActivity : BaseActivity(), AddTopicView {

    companion object {
        const val REQUEST_CODE = 1000
        const val RESULT_EXTRA_TOPIC = "topic"

        fun createIntent(context: Context) = Intent(context, AddTopicActivity::class.java)
    }

    private val binding: ActivityAddTopicBinding by lazy {
        ActivityAddTopicBinding.inflate(layoutInflater)
    }
    private val validator: Validator by lazy {
        Validator(binding)
    }
    private val presenter: AddTopicPresenter by lazy {
        AddTopicPresenter(
            TopicRepositoryImpl(TopicDaoClient(this)),
            UserRepositoryImpl(UserDaoClient(this)),
            StandardSchedulerProvider(),
            AddTopicErrorHandler()
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

    override fun validateTopicFields(): Boolean = validator.validate()

    override fun onTopicAdded(topic: Topic) {
        setResult(
            Activity.RESULT_OK,
            Intent().apply { putExtra(RESULT_EXTRA_TOPIC, topic) }
        )
        Toast.makeText(this, R.string.msg_topic_added, Toast.LENGTH_LONG).show()
        finish()
    }

    private fun setupLayout() {
        binding.also {
            it.presenter = presenter
            it.topic = Topic()
        }
    }

    private fun setFormEnabled(enabled: Boolean) {
        binding.apply {
            title.isEnabled = enabled
            briefDescription.isEnabled = enabled
            details.isEnabled = enabled
            add.isEnabled = enabled
            add.setText(
                if (enabled) R.string.label_add
                else R.string.label_loading
            )
        }
    }

}
