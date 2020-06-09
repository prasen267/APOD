package com.pb.apod.data.repository

import androidx.lifecycle.LiveData
import com.pb.apod.data.api.ApodService
import com.pb.apod.data.model.ApodResponse
import io.reactivex.rxjava3.core.Single

class ApodRepository(private val apodService: ApodService) {
    lateinit var response: LiveData<ApodResponse>
    fun getAPOD(): Single<ApodResponse> {
        return apodService.getApod().map {it.copy()}
    }

    fun getApodByDate(date:String):Single<ApodResponse>{
        return apodService.getApodByDate(date).map {it.copy()}
    }

}