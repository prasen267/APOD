package com.pb.apod

import android.app.Application
import com.pb.apod.common.RxSchedulers
import com.pb.apod.data.api.ApodService
import com.pb.apod.data.api.NetworkConnectionInterceptor
import com.pb.apod.data.repository.ApodRepository
import com.pb.apod.ui.ApodViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class ApodApplication:Application(),KodeinAware {
    override val kodein= Kodein.lazy {
        import(androidXModule(this@ApodApplication))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { ApodService(instance()) }
        bind() from singleton { RxSchedulers() }
        bind() from singleton { ApodRepository(instance()) }
        bind() from provider { ApodViewModelFactory(instance(),instance()) }
    }
}