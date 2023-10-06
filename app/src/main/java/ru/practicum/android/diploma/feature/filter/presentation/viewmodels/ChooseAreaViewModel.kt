package ru.practicum.android.diploma.feature.filter.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.feature.filter.domain.model.Area
import ru.practicum.android.diploma.feature.filter.domain.usecase.GetAreasUseCase
import ru.practicum.android.diploma.feature.filter.domain.util.DataResponse
import ru.practicum.android.diploma.feature.filter.domain.util.NetworkError
import ru.practicum.android.diploma.feature.filter.presentation.states.AreasState

class ChooseAreaViewModel(private val areasUseCase: GetAreasUseCase) : ViewModel() {

    //todo замемнить на реальный areaId
    private val areaId = "-1"

    private val areasStateLiveData = MutableLiveData<AreasState>()
    fun observeAreasState(): LiveData<AreasState> = areasStateLiveData

    private val areas: ArrayList<Area> = arrayListOf(
        Area("1", null, "Area 1", arrayListOf(
            Area("1.1", null, "Area 1.1", arrayListOf(
                Area("1.1.1", null, "Area 1.1.1", arrayListOf()),
                Area("1.1.2", null, "Area 1.1.2", arrayListOf()),
                Area("1.1.3", null, "Area 1.1.3", arrayListOf()),
                Area("1.1.4", null, "Area 1.1.4", arrayListOf())
            )),
            Area("1.2", null, "Area 1.2", arrayListOf(
                Area("1.2.1", null, "Area 1.2.1", arrayListOf()),
                Area("1.2.2", null, "Area 1.2.2", arrayListOf()),
                Area("1.2.3", null, "Area 1.2.3", arrayListOf()),
                Area("1.2.4", null, "Area 1.2.4", arrayListOf())
            )),
        )),
        Area("2", null, "Area 2", arrayListOf(
            Area("2.1", null, "Area 2.1", arrayListOf(
                Area("2.1.1", null, "Area 2.1.1", arrayListOf()),
                Area("2.1.2", null, "Area 2.1.2", arrayListOf()),
                Area("2.1.3", null, "Area 2.1.3", arrayListOf()),
                Area("2.1.4", null, "Area 2.1.4", arrayListOf())
            )),
            Area("2.2", null, "Area 2.2", arrayListOf(
                Area("2.2.1", null, "Area 2.2.1", arrayListOf()),
                Area("2.2.2", null, "Area 2.2.2", arrayListOf()),
                Area("2.2.3", null, "Area 2.2.3", arrayListOf()),
                Area("2.2.4", null, "Area 2.2.4", arrayListOf())
            )),
        ))
    )

    init {
        initScreen()
    }

    private fun initScreen() {
        viewModelScope.launch {
            areasUseCase.invoke(areaId).collect { result ->
                processResult(result)
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
        //todo
    }
}