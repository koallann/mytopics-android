package me.koallann.myagenda.presentation.topics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import me.koallann.myagenda.data.topic.TopicRepositoryImpl
import me.koallann.myagenda.databinding.FragmentTopicsBinding
import me.koallann.myagenda.databinding.ItemTopicBinding
import me.koallann.myagenda.domain.Topic
import me.koallann.myagenda.local.topic.TopicDaoClient
import me.koallann.support.rxschedulers.StandardSchedulerProvider
import me.koallann.support.ui.BaseFragment
import me.koallann.support.ui.list.AutoRecyclerAdapter
import java.lang.IllegalArgumentException

class TopicsFragment : BaseFragment(), TopicsView {

    companion object {
        private const val EXTRA_STATUS_FILTER = "status_filter"

        fun newInstance(filter: Topic.Status) = TopicsFragment().apply {
            arguments = Bundle().apply { putString(EXTRA_STATUS_FILTER, filter.toString()) }
        }
    }

    private val binding: FragmentTopicsBinding by lazy {
        FragmentTopicsBinding.inflate(layoutInflater)
    }
    private val onCreateViewHolder: (
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ) -> TopicViewHolder = { layoutInflater, parent, _ ->
        TopicViewHolder(ItemTopicBinding.inflate(layoutInflater, parent, false))
    }
    private val topicsAdapter: AutoRecyclerAdapter<Topic, TopicViewHolder> by lazy {
        AutoRecyclerAdapter(onCreateViewHolder).apply {
            setHasStableIds(true)
        }
    }
    private val presenter: TopicsPresenter by lazy {
        TopicsPresenter(
            TopicRepositoryImpl(TopicDaoClient(requireContext())),
            StandardSchedulerProvider(),
            TopicsErrorHandler()
        )
    }

    override fun getContentView(): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
        setupUI()
        presenter.onLoadTopics(getStatusFilter())
    }

    override fun onDestroy() {
        presenter.stop()
        presenter.detachView()
        super.onDestroy()
    }

    override fun addTopics(topics: List<Topic>) {
        topicsAdapter.addAll(topics)
    }

    private fun getStatusFilter(): Topic.Status {
        val extra = arguments?.getString(EXTRA_STATUS_FILTER) ?: ""
        return try {
            Topic.Status.valueOf(extra)
        } catch (e: IllegalArgumentException) {
            Topic.Status.UNKNOWN
        }
    }

    private fun setupUI() {
        binding.topics.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = topicsAdapter
        }
    }

}
