package me.koallann.myagenda.presentation.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import me.koallann.myagenda.domain.Topic
import me.koallann.myagenda.presentation.topics.TopicsFragment

class HomePagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val openTopicsFragment: TopicsFragment by lazy {
        TopicsFragment.newInstance(Topic.Status.OPEN) {
            closedTopicsFragment.onNewTopic(it)
        }
    }
    private val closedTopicsFragment: TopicsFragment by lazy {
        TopicsFragment.newInstance(Topic.Status.CLOSED) {
            openTopicsFragment.onNewTopic(it)
        }
    }
    val onNewTopic: (Topic) -> Unit = {
        if (it.status == Topic.Status.OPEN) {
            openTopicsFragment.onNewTopic(it)
        }
    }

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> openTopicsFragment
            1 -> closedTopicsFragment
            else -> Fragment()
        }
    }

}
