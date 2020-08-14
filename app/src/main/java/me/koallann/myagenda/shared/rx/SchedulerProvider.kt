package me.koallann.myagenda.shared.rx

import io.reactivex.rxjava3.core.Scheduler

interface SchedulerProvider {

    fun io(): Scheduler

    fun ui(): Scheduler

}
