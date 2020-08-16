package me.koallann.myagenda.presentation.topics

import io.reactivex.disposables.CompositeDisposable
import me.koallann.support.handlers.ErrorHandler
import me.koallann.support.mvp.Presenter
import me.koallann.support.rxschedulers.SchedulerProvider

class TopicsPresenter(
    private val schedulerProvider: SchedulerProvider,
    private val errorHandler: ErrorHandler
) : Presenter<TopicsView>(TopicsView::class.java) {

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun stop() {
        disposables.clear()
        super.stop()
    }

}
