package com.pb.apod.common


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers


interface RxSchedulers {

    val io:Scheduler

    val ui:Scheduler

    companion object{
        operator fun invoke():RxSchedulers{
            return object :RxSchedulers{
                override val io: Scheduler
                    get() = Schedulers.io()
                override val ui: Scheduler
                    get() = AndroidSchedulers.mainThread()

            }
        }
    }
}