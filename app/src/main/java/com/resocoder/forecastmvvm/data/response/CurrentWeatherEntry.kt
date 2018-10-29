package com.resocoder.forecastmvvm.data.response

import com.google.gson.annotations.SerializedName

data class CurrentWeatherEntry(
    @SerializedName("last_updated_epoch")
    val lastUpdatedEpoch: Int,
    @SerializedName("last_updated")
    val lastUpdated: String,
    @SerializedName("temp_c")
    val tempC: Int,
    @SerializedName("temp_f")
    val tempF: Double,
    @SerializedName("is_day")
    val isDay: Int,
    val condition: Condition,
    @SerializedName("wind_mph")
    val windMph: Int,
    @SerializedName("wind_kph")
    val windKph: Int,
    @SerializedName("wind_degree")
    val windDegree: Int,
    @SerializedName("wind_dir")
    val windDir: String,
    @SerializedName("pressure_mb")
    val pressureMb: Int,
    @SerializedName("pressure_in")
    val pressureIn: Double,
    @SerializedName("precip_mm")
    val precipMm: Int,
    @SerializedName("precip_in")
    val precipIn: Int,
    val humidity: Int,
    val cloud: Int,
    @SerializedName("feelslike_c")
    val feelslikeC: Int,
    @SerializedName("feelslike_f")
    val feelslikeF: Double,
    @SerializedName("vis_km")
    val visKm: Int,
    @SerializedName("vis_miles")
    val visMiles: Int
)