package com.resocoder.forecastmvvm.utils

fun formatConditonUrl(conditionUrl: String): String {
    val split = conditionUrl.split("64x64")
    return "https:${split[0]}128x128${split[1]}"
}