package com.resocoder.forecastmvvm.ui.weather.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.resocoder.forecastmvvm.R
import com.resocoder.forecastmvvm.databinding.CurrentWeatherFragmentBinding
import com.resocoder.forecastmvvm.internal.glide.GlideApp
import com.resocoder.forecastmvvm.ui.base.ScopedFragment
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CurrentWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()

    private var _binding: CurrentWeatherFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CurrentWeatherFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(CurrentWeatherViewModel::class.java)

        bindUI()
    }

    private fun bindUI() = launch {
        val currentWeather = viewModel.weather.await()

        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(viewLifecycleOwner, Observer { location ->
            if (location == null) return@Observer

            updateLocation(location.name)
        })

        currentWeather.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer

            binding.groupLoading.visibility = View.GONE
            updateDateToToday()
            updateTemperatures(it.temperature, it.feelsLikeTemperature)
            updateCondition(it.conditionText)
            updatePrecipitation(it.precipitationVolume)
            updateWind(it.windDirection, it.windSpeed)
            updateVisibility(it.visibilityDistance)

            GlideApp.with(this@CurrentWeatherFragment)
                .load("https:${it.conditionIconUrl}")
                .into(binding.imageViewConditionIcon)
        })
    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isMetricUnit) metric else imperial
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToToday() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }

    private fun updateTemperatures(temperature: Double, feelsLike: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        binding.textViewTemperature.text =
            getString(R.string.temperature_template, temperature, unitAbbreviation)
        binding.textViewFeelsLikeTemperature.text =
            getString(R.string.feels_like_template, feelsLike, unitAbbreviation)
    }

    private fun updateCondition(condition: String) {
        binding.textViewCondition.text = condition
    }

    private fun updatePrecipitation(precipitationVolume: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("mm", "in")
        binding.textViewPrecipitation.text =
            getString(R.string.precipitation_template, precipitationVolume, unitAbbreviation)
    }

    private fun updateWind(windDirection: String, windSpeed: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("kph", "mph")
        binding.textViewWind.text =
            getString(R.string.wind_template, windDirection, windSpeed, unitAbbreviation)
    }

    private fun updateVisibility(visibilityDistance: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("km", "mi.")
        binding.textViewVisibility.text =
            getString(R.string.visibility_template, visibilityDistance, unitAbbreviation)
    }
}
