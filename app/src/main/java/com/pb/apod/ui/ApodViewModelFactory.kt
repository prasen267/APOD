package com.pb.apod.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pb.apod.common.RxSchedulers
import com.pb.apod.data.repository.ApodRepository

@Suppress("UNCHECKED_CAST")
class ApodViewModelFactory(
    private val repository: ApodRepository,
   private val rxSchedulers: RxSchedulers
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ApodViewModel(repository, rxSchedulers) as T
    }
}