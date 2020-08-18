package me.koallann.mytopics.presentation.topics

import android.text.TextUtils
import androidx.databinding.ObservableBoolean
import me.koallann.mytopics.databinding.ItemTopicBinding
import me.koallann.mytopics.domain.Topic
import me.koallann.mytopics.domain.User
import me.koallann.support.ui.list.ViewHolder

class TopicViewHolder(
    private val binding: ItemTopicBinding,
    private val presenter: TopicsPresenter,
    private val signedUser: User?
) : ViewHolder<Topic>(binding.root) {

    private val collapsed: ObservableBoolean = ObservableBoolean(false)

    override fun onBind(item: Topic) {
        binding.also {
            it.presenter = presenter
            it.topic = item
            it.signedUser = signedUser
            it.collapsed = collapsed
            it.executePendingBindings()
        }
    }

    fun setCollapsed(value: Boolean) {
        collapsed.set(value)
        binding.also {
            it.briefDescription.maxLines = if (value) Integer.MAX_VALUE else 2
            it.briefDescription.ellipsize = if (value) null else TextUtils.TruncateAt.END
        }
    }

}
