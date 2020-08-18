package me.koallann.myagenda.presentation.topics

import me.koallann.myagenda.domain.Topic
import me.koallann.myagenda.domain.User
import me.koallann.support.mvp.BasicView

interface TopicsView : BasicView {

    fun onUserLoaded(user: User)

    fun addTopics(topics: List<Topic>)

    fun collapseTopic(topic: Topic)

    fun uncollapseTopic(topic: Topic)

    fun onConfirmCloseTopic(topic: Topic)

    fun onTopicClosed(topic: Topic)

    fun onConfirmReopenTopic(topic: Topic)

    fun onTopicReopened(topic: Topic)

}
