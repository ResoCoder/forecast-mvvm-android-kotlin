package com.resocoder.forecastmvvm.data.provider

import com.resocoder.forecastmvvm.internal.UnitSystem


interface UnitProvider {
    fun getUnitSystem(): UnitSystem
}