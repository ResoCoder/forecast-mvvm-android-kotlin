package com.resocoder.forecastmvvm.data.provider

import com.resocoder.forecastmvvm.data.db.entity.WeatherLocation


interface LocationProvider {
    suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean
    suspend fun getPreferredLocationString(): String
}