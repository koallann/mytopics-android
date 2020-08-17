package me.koallann.myagenda.presentation.topics

import androidx.databinding.ObservableBoolean
import me.koallann.myagenda.databinding.ItemTopicBinding
import me.koallann.myagenda.domain.Topic
import me.koallann.myagenda.domain.User
import me.koallann.support.ui.list.ViewHolder

class TopicViewHolder(
    private val binding: ItemTopicBinding,
    private val presenter: TopicsPresenter,
    private val signedUser: User?
) : ViewHolder<Topic>(binding.root) {

    val collapsed: ObservableBoolean = ObservableBoolean(false)

    override fun onBind(item: Topic) {
        binding.also {
            it.presenter = presenter
            it.topic = item
            it.signedUser = signedUser
            it.collapsed = collapsed
            it.executePendingBindings()
        }
    }

}
