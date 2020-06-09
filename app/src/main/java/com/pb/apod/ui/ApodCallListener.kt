package com.pb.apod.ui

interface ApodCallListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(error: String)
}