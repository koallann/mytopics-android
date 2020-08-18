package me.koallann.myagenda.presentation.topics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import me.koallann.myagenda.R
import me.koallann.myagenda.data.topic.TopicRepositoryImpl
import me.koallann.myagenda.data.user.UserRepositoryImpl
import me.koallann.myagenda.databinding.FragmentTopicsBinding
import me.koallann.myagenda.databinding.ItemTopicBinding
import me.koallann.myagenda.domain.Topic
import me.koallann.myagenda.domain.User
import me.koallann.myagenda.local.topic.TopicDaoClient
import me.koallann.myagenda.local.user.UserDaoClient
import me.koallann.support.rxschedulers.StandardSchedulerProvider
import me.koallann.support.ui.BaseFragment
import me.koallann.support.ui.list.AutoRecyclerAdapter

class TopicsFragment : BaseFragment(), TopicsView {

    companion object {
        private const val EXTRA_STATUS_FILTER = "status_filter"

        fun newInstance(
            filter: Topic.Status,
            onUpdateTopic: ((Topic) -> Unit)? = null
        ) = TopicsFragment().apply {
            arguments = Bundle().apply { putString(EXTRA_STATUS_FILTER, filter.toString()) }
            this.onUpdateTopic = onUpdateTopic
        }
    }

    private val binding: FragmentTopicsBinding by lazy {
        FragmentTopicsBinding.inflate(layoutInflater)
    }
    private var signedUser: User? = null
    private val onCreateViewHolder: (
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ) -> TopicViewHolder = { layoutInflater, parent, _ ->
        TopicViewHolder(
            ItemTopicBinding.inflate(layoutInflater, parent, false),
            presenter,
            signedUser
        )
    }
    private val topicsAdapter: AutoRecyclerAdapter<Topic, TopicViewHolder> by lazy {
        AutoRecyclerAdapter(onCreateViewHolder).apply {
            setHasStableIds(true)
        }
    }
    private val presenter: TopicsPresenter by lazy {
        TopicsPresenter(
            getStatusFilter(),
            TopicRepositoryImpl(TopicDaoClient(requireContext())),
            UserRepositoryImpl(UserDaoClient(requireContext())),
            StandardSchedulerProvider(),
            TopicsErrorHandler()
        )
    }

    val onNewTopic: (Topic) -> Unit = { topic ->
        if (!topicsAdapter.contains(topic)) {
            topicsAdapter.add(topic)
            binding.topics.also {
                it.scrollToPosition(topicsAdapter.size() - 1)
            }
        }
    }
    private var onUpdateTopic: ((Topic) -> Unit)? = null

    override fun getContentView(): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
        presenter.onLoadUser()
        setupLayout()
        presenter.onLoadTopics()
    }

    override fun onDestroy() {
        presenter.stop()
        presenter.detachView()
        super.onDestroy()
    }

    override fun onUserLoaded(user: User) {
        signedUser = user
    }

    override fun addTopics(topics: List<Topic>) {
        topicsAdapter.addAll(topics)
    }

    override fun collapseTopic(topic: Topic) {
        getViewHolderForTopic(topic)?.setCollapsed(true)
    }

    override fun uncollapseTopic(topic: Topic) {
        getViewHolderForTopic(topic)?.setCollapsed(false)
    }

    override fun onConfirmCloseTopic(topic: Topic) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.label_close)
            .setMessage(R.string.msg_close_topic)
            .setPositiveButton(R.string.label_ok) { dialog, _ ->
                dialog.dismiss()
                presenter.closeTopic(topic)
            }
            .setNegativeButton(R.string.label_cancel) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun onTopicClosed(topic: Topic) {
        onUpdateTopic?.invoke(topic)
        topicsAdapter.remove(topic)
        showMessage(R.string.msg_topic_closed)
    }

    override fun onConfirmReopenTopic(topic: Topic) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.label_reopen)
            .setMessage(R.string.msg_reopen_topic)
            .setPositiveButton(R.string.label_ok) { dialog, _ ->
                dialog.dismiss()
                presenter.reopenTopic(topic)
            }
            .setNegativeButton(R.string.label_cancel) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun onTopicReopened(topic: Topic) {
        onUpdateTopic?.invoke(topic)
        topicsAdapter.remove(topic)
        showMessage(R.string.msg_topic_reopened)
    }

    private fun getStatusFilter(): Topic.Status {
        val extra = arguments?.getString(EXTRA_STATUS_FILTER) ?: ""
        return try {
            Topic.Status.valueOf(extra)
        } catch (e: IllegalArgumentException) {
            Topic.Status.UNKNOWN
        }
    }

    private fun setupLayout() {
        binding.topics.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = topicsAdapter
        }
    }

    private fun getViewHolderForTopic(topic: Topic): TopicViewHolder? {
        val index = topicsAdapter.indexOf(topic)
        if (index == -1) {
            return null
        }
        return binding.topics.findViewHolderForAdapterPosition(index) as TopicViewHolder
    }

}
