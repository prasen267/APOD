package com.pb.apod.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pb.apod.common.RxSchedulers
import com.pb.apod.data.model.ApodResponse
import com.pb.apod.data.repository.ApodRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable

class ApodViewModel(private val repository: ApodRepository,
                    private val rxSchedulers: RxSchedulers) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val _apod = MutableLiveData<ApodResponse>()
    val apod: LiveData<ApodResponse> = _apod

    init {
       _apod.postValue(ApodResponse("","","","","","",""))
    getApod()
    }

    fun getApod() {
        compositeDisposable.add(
            repository.getAPOD().subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .subscribe({
                    _apod.postValue(it)
                }, {

                })
        )
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}