package me.koallann.myagenda.presentation.addtopic

import me.koallann.myagenda.domain.Topic
import me.koallann.support.mvp.BasicView

interface AddTopicView : BasicView {

    fun validateTopicFields(): Boolean

    fun onTopicAdded(topic: Topic)

}
