package com.pb.apod.data.api

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor(
    context: Context
) : Interceptor {

    private val applicationContent = context.applicationContext
    override fun intercept(chain: Interceptor.Chain): Response {
        if(!isInternetAvailabe()){
            throw NoInternetException(" Please Connect to a active Data connection and restart!")
        }
        return chain.proceed(chain.request())
    }

    private fun isInternetAvailabe(): Boolean {
        val connectivityManager=
            (applicationContent).getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.activeNetworkInfo.also {
            return it!=null && it.isConnected
        }
    }
}