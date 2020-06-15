package com.pb.apod.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pb.apod.data.api.ApodService
import com.pb.apod.data.model.ApodResponse
import io.reactivex.rxjava3.core.Single

class ApodRepository(private val apodService: ApodService) {

    //returns a single Apod when the app starts up
    fun getAPOD(): Single<ApodResponse> {
        return apodService.getApod().map {it.copy()}

    }
        // fetches and returns an Apod by date.
    fun getApodByDate(date:String):Single<ApodResponse>{
        return apodService.getApodByDate(date).map {it.copy()}
    }

}