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


    /*    return Single.just(
            ApodResponse("1995-11-11",
                "Recently two new types of lightning have been verified: red sprites and blue jets.  These atmospheric discharges occur very high in the Earth's atmosphere - much higher than the familiar form of lightning. Red sprites appear red in color and go from the tops of clouds to as high as the ionosphere - an ionized layer 90 kilometers above the Earth's surface. They last only a small fraction of a second.  The existence of red sprites has been suggested previously, but only in 1994 were aircraft flown above massive thunderstorms with the high speed video equipment necessary to verify these spectacular events. Scientists are unsure of the cause and nature of red sprites.",
                "https://apod.nasa.gov/apod/image/redsprite.gif",
                "image",
                "v1",
                "Tommorow's picture",
                "https://apod.nasa.gov/apod/image/redsprite.gif")
        )*/
    }
        // fetches and returns an Apod by date.
    fun getApodByDate(date:String):Single<ApodResponse>{
        return apodService.getApodByDate(date).map {it.copy()}
    }

}