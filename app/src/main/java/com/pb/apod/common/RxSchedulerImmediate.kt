package com.pb.apod.common

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

class RxSchedulerImmediate:RxSchedulers {
    override val io: Scheduler
        get() = Schedulers.trampoline()
    override val ui: Scheduler
        get() = Schedulers.trampoline()
}