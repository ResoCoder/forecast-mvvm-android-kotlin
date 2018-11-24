package com.resocoder.forecastmvvm.ui.weather.current

import androidx.lifecycle.ViewModel;
import com.resocoder.forecastmvvm.data.repository.ForecastRepository
import com.resocoder.forecastmvvm.internal.UnitSystem
import com.resocoder.forecastmvvm.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository
) : ViewModel() {
    private val unitSystem = UnitSystem.METRIC //get from settings later

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(isMetric)
    }
}
