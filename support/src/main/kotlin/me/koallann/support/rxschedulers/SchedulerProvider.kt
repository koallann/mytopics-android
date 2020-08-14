package me.koallann.support.rxschedulers

import io.reactivex.rxjava3.core.Scheduler

interface SchedulerProvider {

    fun io(): Scheduler

    fun ui(): Scheduler

}
