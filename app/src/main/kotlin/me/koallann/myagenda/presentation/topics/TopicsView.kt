package me.koallann.myagenda.presentation.topics

import me.koallann.myagenda.domain.Topic
import me.koallann.support.mvp.BasicView

interface TopicsView : BasicView {

    fun addTopics(topics: List<Topic>)

}
