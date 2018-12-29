package com.resocoder.forecastmvvm.data.network.response

import com.google.gson.annotations.SerializedName
import com.resocoder.forecastmvvm.data.db.entity.WeatherLocation

data class FutureWeatherResponse(
    @SerializedName("forecast")
    val futureWeatherEntries: ForecastDaysContainer,
    val location: WeatherLocation
)