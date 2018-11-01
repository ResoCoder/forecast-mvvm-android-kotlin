package com.resocoder.forecastmvvm.data.db.entity

import com.google.gson.annotations.SerializedName

data class Location(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    @SerializedName("tz_id")
    val tzId: String,
    @SerializedName("localtime_epoch")
    val localtimeEpoch: Int,
    val localtime: String
)