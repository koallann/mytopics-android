package me.koallann.support.extensions

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import me.koallann.support.mvp.BasicView
import me.koallann.support.rxschedulers.SchedulerProvider

/**
 * Thread switching extensions.
 */

fun <T> Observable<T>.fromIoToUiThread(schedulerProvider: SchedulerProvider): Observable<T> {
    return compose { upstream ->
        upstream
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}

fun <T> Flowable<T>.fromIoToUiThread(schedulerProvider: SchedulerProvider): Flowable<T> {
    return compose { upstream ->
        upstream
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}

fun <T> Single<T>.fromIoToUiThread(schedulerProvider: SchedulerProvider): Single<T> {
    return compose { upstream ->
        upstream
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}

fun Completable.fromIoToUiThread(schedulerProvider: SchedulerProvider): Completable {
    return compose { upstream ->
        upstream
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}

/**
 * View loading extensions.
 */

fun <T> Observable<T>.setLoadingView(view: BasicView?): Observable<T> {
    return compose { upstream ->
        upstream
            .doOnSubscribe { view?.showLoading() }
            .doFinally { view?.dismissLoading() }
    }
}

fun <T> Flowable<T>.setLoadingView(view: BasicView?): Flowable<T> {
    return compose { upstream ->
        upstream
            .doOnSubscribe { view?.showLoading() }
            .doFinally { view?.dismissLoading() }
    }
}

fun <T> Single<T>.setLoadingView(view: BasicView?): Single<T> {
    return compose { upstream ->
        upstream
            .doOnSubscribe { view?.showLoading() }
            .doFinally { view?.dismissLoading() }
    }
}

fun Completable.setLoadingView(view: BasicView?): Completable {
    return compose { upstream ->
        upstream
            .doOnSubscribe { view?.showLoading() }
            .doFinally { view?.dismissLoading() }
    }
}
