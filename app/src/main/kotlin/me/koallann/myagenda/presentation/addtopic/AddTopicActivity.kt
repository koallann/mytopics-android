package me.koallann.myagenda.presentation.addtopic

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import br.com.ilhasoft.support.validation.Validator
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
        setupUI()
    }

    override fun onDestroy() {
        presenter.stop()
        presenter.detachView()
        super.onDestroy()
    }

    override fun validateTopicFields(): Boolean = validator.validate()

    override fun onTopicAdded(topic: Topic) {
        showMessage("onTopicAdded")
    }

    private fun setupUI() {
        binding.also {
            it.presenter = presenter
            it.topic = Topic()
        }
    }

}
