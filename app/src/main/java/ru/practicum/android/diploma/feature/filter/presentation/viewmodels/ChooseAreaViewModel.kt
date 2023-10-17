package ru.practicum.android.diploma.feature.filter.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.util.DataTransmitter
import ru.practicum.android.diploma.feature.filter.domain.model.Area
import ru.practicum.android.diploma.feature.filter.domain.usecase.GetAllAreasUseCase
import ru.practicum.android.diploma.feature.filter.domain.usecase.GetAreasUseCase
import ru.practicum.android.diploma.feature.filter.domain.util.DataResponse
import ru.practicum.android.diploma.feature.filter.domain.util.NetworkError
import ru.practicum.android.diploma.feature.filter.presentation.states.AreasState

class ChooseAreaViewModel(
    private val areasUseCase: GetAreasUseCase,
    private val areasAllUseCase: GetAllAreasUseCase
) : ViewModel() {

    private var _dataArea = MutableLiveData<Area>()
    val dataArea: LiveData<Area> = _dataArea

    private val areasStateLiveData = MutableLiveData<AreasState>()
    fun observeAreasState(): LiveData<AreasState> = areasStateLiveData

    private var areas = arrayListOf<Area>()
    private lateinit var filteredAreas: List<Area>

    init {
        initScreen()
    }

    private fun initScreen() {
        viewModelScope.launch {
            if (DataTransmitter.getCountry() != null) {
                areasUseCase.invoke(DataTransmitter.getCountry()!!.id).collect { result ->
                    processResult(result)
                }
            } else {
                areasAllUseCase.invoke().collect { result ->

                    val networkError: NetworkError? = result.networkError

                    if (networkError != null) {
                        processResult(result)
                    } else {

                        var data: List<Area>? = result.data

                        data = data?.filter { area -> area.name != "Другие регионы" }

                        val totalAreas: ArrayList<Area> = ArrayList()

                        if (data != null) {
                            for (country in data) {
                                country.areas.forEach { area ->
                                    totalAreas.add(area)
                                }
                            }
                        }

                        val dataResponse: DataResponse<Area> =
                            DataResponse(data = totalAreas, networkError = null)

                        processResult(dataResponse)

                    }

                }
            }

        }
    }

    private suspend fun processResult(result: DataResponse<Area>) {
        if (result.data != null) {
            areas.apply {
                clear()
                addAll(getAreasList(result.data))
            }
            filteredAreas = areas
            areasStateLiveData.value =
                AreasState.DisplayAreas(filteredAreas)
        } else {
            when (result.networkError!!) {
                NetworkError.BAD_CONNECTION -> areasStateLiveData.value =
                    AreasState.Error(
                        "Нет интернета",
                        R.drawable.search_placeholder_internet_problem
                    )

                NetworkError.SERVER_ERROR -> areasStateLiveData.value =
                    AreasState.Error(
                        "Ошибка сервера",
                        R.drawable.search_placeholder_server_not_responding
                    )
            }
        }
    }

    private suspend fun getAreasList(nestedAreasList: List<Area>): ArrayList<Area> =
        withContext(Dispatchers.Default) {
            val extendedAreasList: ArrayList<Area> = arrayListOf()
            for (area in nestedAreasList) {
                getAreasRecursively(extendedAreasList, area)
            }
            extendedAreasList.sortBy { it.name }
            extendedAreasList
        }

    private fun getAreasRecursively(extendedAreasList: ArrayList<Area>, area: Area) {
        extendedAreasList.add(area)
        if (area.areas.isNotEmpty()) {
            for (nestedArea in area.areas) {
                getAreasRecursively(extendedAreasList, nestedArea)
            }
        }
    }

    fun onAreaClicked(areaClicked: Area, previousAreaClicked: Area?) {
        val areaPosition = areas.indexOf(areaClicked)
        val previousAreaPosition = if (previousAreaClicked!= null) areas.indexOf(previousAreaClicked) else -1

        areas[areaPosition] = areaClicked
        if (previousAreaPosition != -1) areas[previousAreaPosition].isChecked = false

        _dataArea.postValue(areaClicked)
    }

    fun onAreaTextChanged(filterText: String) {
        filterAreas(filterText)
    }

    private fun filterAreas(filterText: String?) {
        if (filterText.isNullOrEmpty()) {
            filteredAreas = areas
            areasStateLiveData.value = AreasState.DisplayAreas(filteredAreas)
        } else {
            filteredAreas = areas.filter {
                it.name.contains(filterText, true)
            }
            if (filteredAreas.isNotEmpty()) {
                areasStateLiveData.value = AreasState.DisplayAreas(filteredAreas)
            } else {
                areasStateLiveData.value = AreasState.Error(
                    "Такого региона нет",
                    R.drawable.search_placeholder_nothing_found
                )
            }
        }
    }
}