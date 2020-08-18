package me.koallann.support.rxschedulers

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 * For testing purposes.
 */
class ImmediateSchedulerProvider : SchedulerProvider {

    override fun io(): Scheduler = Schedulers.trampoline()

    override fun ui(): Scheduler = Schedulers.trampoline()

}
