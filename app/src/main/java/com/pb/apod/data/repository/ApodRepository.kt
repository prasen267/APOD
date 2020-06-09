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


     /*  return Single.just(
           ApodResponse(
               "2016-11-05",
               "Shot in Ultra HD, this stunning video can take you on a tour of the International Space Station. A fisheye lens with sharp focus and extreme depth of field provides an immersive visual experience of life in the orbital outpost. In the 18 minute fly-through, your point of view will float serenely while you watch our fair planet go by 400 kilometers below the seven-windowed Cupola, and explore the interior of the station's habitable nodes and modules from an astronaut's perspective. The modular International Space Station is Earth's largest artificial satellite, about the size of a football field in overall length and width. Its total pressurized volume is approximately equal to that of a Boeing 747 aircraft.",
               "",
               "video",
               "v1",
               "ISS Fisheye Fly-Through",
               "https://www.youtube.com/embed/DhmdyQdu96M?rel=0"
           ))*/
    }
        // fetches and returns an Apod by date.
    fun getApodByDate(date:String):Single<ApodResponse>{
        return apodService.getApodByDate(date).map {it.copy()}
    }

}