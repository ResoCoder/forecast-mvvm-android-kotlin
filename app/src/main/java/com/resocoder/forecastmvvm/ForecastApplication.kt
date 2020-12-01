package com.resocoder.forecastmvvm

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import com.resocoder.forecastmvvm.data.db.ForecastDatabase
import com.resocoder.forecastmvvm.data.network.*
import com.resocoder.forecastmvvm.data.provider.LocationProvider
import com.resocoder.forecastmvvm.data.provider.LocationProviderImpl
import com.resocoder.forecastmvvm.data.provider.UnitProvider
import com.resocoder.forecastmvvm.data.provider.UnitProviderImpl
import com.resocoder.forecastmvvm.data.repository.ForecastRepository
import com.resocoder.forecastmvvm.data.repository.ForecastRepositoryImpl
import com.resocoder.forecastmvvm.ui.weather.current.CurrentWeatherViewModelFactory
import com.resocoder.forecastmvvm.ui.weather.future.detail.FutureDetailWeatherViewModelFactory
import com.resocoder.forecastmvvm.ui.weather.future.list.FutureListWeatherViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*
import org.threeten.bp.LocalDate

class ForecastApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@ForecastApplication))

        // database
        bind() from singleton { ForecastDatabase(instance()) }
        bind() from singleton { instance<ForecastDatabase>().currentWeatherDao() }
        bind() from singleton { instance<ForecastDatabase>().futureWeatherDao() }
        bind() from singleton { instance<ForecastDatabase>().weatherLocationDao() }

        // network
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { WeatherApiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }

        // location
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(), instance()) }

        // repository
        bind<ForecastRepository>() with singleton {
            ForecastRepositoryImpl(instance(), instance(), instance(), instance(), instance())
        }

        // provider
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }

        // view model factory
        bind() from provider { CurrentWeatherViewModelFactory(instance(), instance()) }
        bind() from provider { FutureListWeatherViewModelFactory(instance(), instance()) }
        bind() from factory { detailDate: LocalDate ->
            FutureDetailWeatherViewModelFactory(detailDate, instance(), instance())
        }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }
}