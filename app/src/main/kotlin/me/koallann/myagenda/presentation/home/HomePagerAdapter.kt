package me.koallann.myagenda.presentation.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import me.koallann.myagenda.domain.Topic
import me.koallann.myagenda.presentation.topics.TopicsFragment

class HomePagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TopicsFragment.newInstance(Topic.Status.OPEN)
            1 -> TopicsFragment.newInstance(Topic.Status.CLOSED)
            else -> Fragment()
        }
    }

}
