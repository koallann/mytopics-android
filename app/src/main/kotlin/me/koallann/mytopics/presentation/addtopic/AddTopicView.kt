package me.koallann.mytopics.presentation.addtopic

import me.koallann.mytopics.domain.Topic
import me.koallann.support.mvp.BasicView

interface AddTopicView : BasicView {

    fun validateTopicFields(): Boolean

    fun onTopicAdded(topic: Topic)

}
