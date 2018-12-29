package com.resocoder.forecastmvvm.data.network.response

import com.google.gson.annotations.SerializedName
import com.resocoder.forecastmvvm.data.db.entity.FutureWeatherEntry

data class ForecastDaysContainer(
    @SerializedName("forecastday")
    val entries: List<FutureWeatherEntry>
)