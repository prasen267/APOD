package com.pb.apod.data.model


import com.google.gson.annotations.SerializedName

data class ApodResponse(
    @SerializedName("date")
    var date: String = "",
    @SerializedName("explanation")
    var explanation: String = "",
    @SerializedName("hdurl")
    var hdurl: String = "",
    @SerializedName("media_type")
    var mediaType: String = "",
    @SerializedName("service_version")
    var serviceVersion: String = "",
    @SerializedName("title")
    var title: String = "",
    @SerializedName("url")
    var url: String = ""
)