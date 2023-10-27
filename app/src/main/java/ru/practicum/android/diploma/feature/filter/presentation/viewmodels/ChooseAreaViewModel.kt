package ru.practicum.android.diploma.feature.filter.presentation.viewmodels

import android.content.res.Resources
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
import ru.practicum.android.diploma.feature.filter.presentation.states.IndustriesState

class ChooseAreaViewModel(
    private val areasUseCase: GetAreasUseCase,
    private val areasAllUseCase: GetAllAreasUseCase,
    private val resources: Resources
) : ViewModel() {

    private var _areaData = MutableLiveData<Area>()
    fun areaData(): LiveData<Area> = _areaData

    private val areasStateLiveData = MutableLiveData<AreasState>()
    fun observeAreasState(): LiveData<AreasState> = areasStateLiveData

    private var areas = arrayListOf<Area>()
    private var filteredAreas: List<Area>? = null

    init {
        initAreaData()
    }

    private var previousAreaClicked: Area? = null
    private var previousAreaPositionInFullList = -1

    private fun initAreaData() {
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
                        data = data?.filter { area ->
                            area.name != resources.getString(R.string.filter_message_another_regions)
                        }
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
            if (areas.isNotEmpty()) {
                filteredAreas = areas
                areasStateLiveData.value =
                    AreasState.DisplayAreas(filteredAreas!!)
            } else {
                areasStateLiveData.value =
                    AreasState.Error(
                        resources.getString(R.string.filter_message_failed_to_get_list),
                        R.drawable.areas_placeholder_can_not_receive_list
                    )
            }
        } else {
            when (result.networkError!!) {
                NetworkError.BAD_CONNECTION -> getBadConnectionErrorState()
                NetworkError.SERVER_ERROR -> getServerErrorState()
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

    private fun getBadConnectionErrorState(): IndustriesState.Error {
        return IndustriesState.Error(
            resources.getString(R.string.message_no_internet),
            R.drawable.search_placeholder_internet_problem
        )
    }

    private fun getServerErrorState(): IndustriesState.Error {
        return IndustriesState.Error(
            resources.getString(R.string.message_server_error),
            R.drawable.search_placeholder_server_not_responding
        )
    }

    private fun getAreasRecursively(extendedAreasList: ArrayList<Area>, area: Area) {
        extendedAreasList.add(area)
        if (area.areas.isNotEmpty()) {
            for (nestedArea in area.areas) {
                getAreasRecursively(extendedAreasList, nestedArea)
            }
        }
    }

    fun onAreaClicked(
        areaClickedPosition: Int,
        areaClicked: Area,
        notifyPreviousItemChanged: (Int) -> Unit
    ) {
        var previousAreaClickedPosition = -1
        if (previousAreaClicked != null) {
            for (i in filteredAreas!!.indices) {
                if (filteredAreas!![i].id == previousAreaClicked!!.id) {
                    previousAreaClickedPosition = i
                    notifyPreviousItemChanged(previousAreaClickedPosition)
                }
            }
        }

        var industryPositionInFullList = -1
        for (i in areas.indices) {
            if (areas[i].id == areaClicked.id) industryPositionInFullList = i
        }

        areas[industryPositionInFullList].isChecked = areaClicked.isChecked
        if (previousAreaPositionInFullList != -1) areas[previousAreaPositionInFullList].isChecked =
            false

        if (previousAreaClickedPosition != areaClickedPosition) {
            previousAreaClicked = areaClicked
            previousAreaPositionInFullList = industryPositionInFullList
        } else {
            previousAreaClicked = null
            previousAreaPositionInFullList = -1
        }
        _areaData.postValue(areaClicked)
    }

    fun onAreaTextChanged(filterText: String) {
        filterAreas(filterText)
    }

    private fun filterAreas(filterText: String?) {
        if (filteredAreas == null) {
            areasStateLiveData.value = AreasState.Error(
                resources.getString(R.string.filter_message_failed_to_get_list),
                R.drawable.areas_placeholder_can_not_receive_list
            )
            return
        }
        if (filterText.isNullOrEmpty()) {
            filteredAreas = areas
            areasStateLiveData.value = AreasState.DisplayAreas(filteredAreas!!)
        } else {
            filteredAreas = areas.filter {
                it.name.contains(filterText, true)
            }
            if (filteredAreas!!.isNotEmpty()) {
                areasStateLiveData.value = AreasState.DisplayAreas(filteredAreas!!)
            } else {
                areasStateLiveData.value = AreasState.Error(
                    resources.getString(R.string.filter_message_no_region),
                    R.drawable.search_placeholder_nothing_found
                )
            }
        }
    }
}