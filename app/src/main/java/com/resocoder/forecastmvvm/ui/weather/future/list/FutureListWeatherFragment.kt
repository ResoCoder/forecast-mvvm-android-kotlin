package com.resocoder.forecastmvvm.ui.weather.future.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.resocoder.forecastmvvm.data.db.LocalDateConverter
import com.resocoder.forecastmvvm.data.db.unitlocalized.future.list.UnitSpecificSimpleFutureWeatherEntry
import com.resocoder.forecastmvvm.databinding.FutureListWeatherFragmentBinding
import com.resocoder.forecastmvvm.databinding.ItemFutureWeatherBinding
import com.resocoder.forecastmvvm.ui.base.ScopedFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.viewbinding.GroupieViewHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import org.threeten.bp.LocalDate

class FutureListWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: FutureListWeatherViewModelFactory by instance()

    private var _binding: FutureListWeatherFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FutureListWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FutureListWeatherFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(FutureListWeatherViewModel::class.java)
        bindUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun bindUI() = launch(Dispatchers.Main) {
        val futureWeatherEntries = viewModel.weatherEntries.await()
        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(viewLifecycleOwner, Observer { location ->
            if (location == null) return@Observer

            updateLocation(location.name)
        })

        futureWeatherEntries.observe(viewLifecycleOwner, Observer { weatherEntries ->
            if (weatherEntries == null) return@Observer

            binding.groupLoading.visibility = View.GONE
            updateDateToNextWeek()
            initRecyclerView(weatherEntries.toFutureWeatherItems())
        })
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToNextWeek() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Next Week"
    }

    private fun List<UnitSpecificSimpleFutureWeatherEntry>.toFutureWeatherItems(): List<FutureWeatherItem> {
        return this.map {
            FutureWeatherItem(it)
        }
    }

    private fun initRecyclerView(items: List<FutureWeatherItem>) {
        val groupAdapter = GroupAdapter<GroupieViewHolder<ItemFutureWeatherBinding>>().apply {
            addAll(items)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@FutureListWeatherFragment.context)
            adapter = groupAdapter
        }

        groupAdapter.setOnItemClickListener { item, view ->
            (item as? FutureWeatherItem)?.let {
                showWeatherDetail(it.weatherEntry.date, view)
            }
        }
    }

    private fun showWeatherDetail(date: LocalDate, view: View) {
        val dateString = LocalDateConverter.dateToString(date)!!
        val actionDetail = FutureListWeatherFragmentDirections.actionDetail(dateString)
        Navigation.findNavController(view).navigate(actionDetail)
    }
}
