package com.resocoder.forecastmvvm.ui.weather.future.list

import android.view.View
import com.resocoder.forecastmvvm.R
import com.resocoder.forecastmvvm.data.db.unitlocalized.future.list.MetricSimpleFutureWeatherEntry
import com.resocoder.forecastmvvm.data.db.unitlocalized.future.list.UnitSpecificSimpleFutureWeatherEntry
import com.resocoder.forecastmvvm.databinding.ItemFutureWeatherBinding
import com.resocoder.forecastmvvm.internal.glide.GlideApp
import com.xwray.groupie.viewbinding.BindableItem
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class FutureWeatherItem(
    val weatherEntry: UnitSpecificSimpleFutureWeatherEntry,
) : BindableItem<ItemFutureWeatherBinding>() {
    override fun initializeViewBinding(view: View): ItemFutureWeatherBinding {
        return ItemFutureWeatherBinding.bind(view)
    }

    override fun bind(viewBinding: ItemFutureWeatherBinding, position: Int) {
        viewBinding.apply {
            textViewCondition.text = weatherEntry.conditionText
            updateDate()
            updateTemperature()
            updateConditionImage()
        }
    }

    override fun getLayout() = R.layout.item_future_weather

    private fun ItemFutureWeatherBinding.updateDate() {
        val dtFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        textViewDate.text = weatherEntry.date.format(dtFormatter)
    }

    private fun ItemFutureWeatherBinding.updateTemperature() {
        val unitAbbreviation = if (weatherEntry is MetricSimpleFutureWeatherEntry) "°C" else "°F"
        textViewTemperature.text = root.context.getString(
            R.string.temperature_template,
            weatherEntry.avgTemperature,
            unitAbbreviation
        )
    }

    private fun ItemFutureWeatherBinding.updateConditionImage() {
        GlideApp.with(root)
            .load("https:" + weatherEntry.conditionIconUrl)
            .into(imageViewConditionIcon)
    }
}