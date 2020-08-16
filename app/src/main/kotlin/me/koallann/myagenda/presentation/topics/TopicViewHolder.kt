package me.koallann.myagenda.presentation.topics

import me.koallann.myagenda.databinding.ItemTopicBinding
import me.koallann.myagenda.domain.Topic
import me.koallann.support.ui.list.ViewHolder

class TopicViewHolder(private val binding: ItemTopicBinding) : ViewHolder<Topic>(binding.root) {

    override fun onBind(item: Topic) {
        binding.also {
            it.topic = item
            it.executePendingBindings()
        }
    }

}
