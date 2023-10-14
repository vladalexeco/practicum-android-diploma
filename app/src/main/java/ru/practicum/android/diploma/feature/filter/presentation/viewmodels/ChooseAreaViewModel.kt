package ru.practicum.android.diploma.feature.filter.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.core.util.DataTransmitter
import ru.practicum.android.diploma.feature.filter.domain.model.Area
import ru.practicum.android.diploma.feature.filter.domain.usecase.GetAllAreasUseCase
import ru.practicum.android.diploma.feature.filter.domain.usecase.GetAreasUseCase
import ru.practicum.android.diploma.feature.filter.domain.util.DataResponse
import ru.practicum.android.diploma.feature.filter.domain.util.NetworkError
import ru.practicum.android.diploma.feature.filter.presentation.states.AreasState

class  ChooseAreaViewModel(
    private val areasUseCase: GetAreasUseCase,
    private val areasAllUseCase: GetAllAreasUseCase
) : ViewModel() {

    private var _dataArea = MutableLiveData<Area>()
    val dataArea: LiveData<Area> = _dataArea

    private val areasStateLiveData = MutableLiveData<AreasState>()
    fun observeAreasState(): LiveData<AreasState> = areasStateLiveData

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

                        val dataResponse: DataResponse<Area> = DataResponse(data = totalAreas, networkError = null)

                        processResult(dataResponse)

                    }

                }
            }

        }
    }

    private suspend fun processResult(result: DataResponse<Area>) {

        if (result.data != null) {
            areasStateLiveData.value =
                AreasState.DisplayAreas(getAreasList(result.data))
        }
        else {
            when (result.networkError!!) {
                NetworkError.BAD_CONNECTION -> areasStateLiveData.value =
                    AreasState.Error("Проверьте подключение к интернету")
                NetworkError.SERVER_ERROR -> areasStateLiveData.value =
                    AreasState.Error("Ошибка сервера")
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

    fun onAreaClicked(area: Area) {
        _dataArea.postValue(area)
    }
}