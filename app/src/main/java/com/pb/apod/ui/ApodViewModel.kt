package com.pb.apod.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pb.apod.common.EspressoIdlingResource
import com.pb.apod.common.NetworkState
import com.pb.apod.common.RxSchedulers
import com.pb.apod.data.api.NoInternetException
import com.pb.apod.data.model.ApodResponse
import com.pb.apod.data.repository.ApodRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable

class ApodViewModel(
    private val repository: ApodRepository,
    private val rxSchedulers: RxSchedulers
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val _apod = MutableLiveData<ApodResponse>()
    val apod: LiveData<ApodResponse> = _apod
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState> = _networkState
    val _errorState = MutableLiveData<String>()
    val errorState: LiveData<String> = _errorState

    init {
        _apod.postValue(ApodResponse("", "", "", "", "", "", ""))
        getApod()
    }

    /*
    * fetches a single APOD
    * */
    fun getApod() {
        //resourceIdling for espresso tests.
        EspressoIdlingResource.increment()
        _networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(

            repository.getAPOD().subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .subscribe({

                    _apod.postValue(it)
                    EspressoIdlingResource.decrement()
                    _networkState.postValue(NetworkState.LOADED)
                }, {
                    if (it is NoInternetException) {

                        _errorState.postValue(it.message)
                    } else {
                        _errorState.postValue(it.message)
                    }
                    _networkState.postValue(NetworkState.ERROR)
                })
        )
    }

    /*
        * fetches a single APOD by date
        * */
    fun getApodByDate(date: String) {
        EspressoIdlingResource.increment()
        _networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            repository.getApodByDate(date).subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .subscribe({
                    _apod.postValue(it)
                    EspressoIdlingResource.decrement()
                    _networkState.postValue(NetworkState.LOADED)
                }, {
                    _networkState.postValue(NetworkState.ERROR)
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}