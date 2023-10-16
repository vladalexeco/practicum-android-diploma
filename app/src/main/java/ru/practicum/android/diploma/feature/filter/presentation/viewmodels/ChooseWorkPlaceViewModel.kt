package ru.practicum.android.diploma.feature.filter.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.util.DataTransmitter
import ru.practicum.android.diploma.feature.filter.domain.model.Area
import ru.practicum.android.diploma.feature.filter.domain.usecase.GetAreasUseCase

class ChooseWorkPlaceViewModel(
    private val getAreasUseCase: GetAreasUseCase
) : ViewModel() {

    private var _hasRegionsLiveData = MutableLiveData<Boolean>()
    val hasRegionsLiveData: LiveData<Boolean> = _hasRegionsLiveData

    private var _dataCountry = MutableLiveData<String>()
    val dataCountry: LiveData<String> = _dataCountry

    private var _dataArea = MutableLiveData<String>()
    val dataArea: LiveData<String> = _dataArea

    fun initScreenData() {
        setCountryData()
        setAreaData()
    }

    fun checkCountryHasRegions() {
        viewModelScope.launch {
            getAreasUseCase(DataTransmitter.getCountry()!!.id)
                .collect {
                    val areaList: List<Area>? = it.data
                    if (areaList != null) {
                        _hasRegionsLiveData.postValue(areaList.isNotEmpty())
                    }
                }
        }
    }

    private fun setCountryData() {
        val country = DataTransmitter.getCountry()
        _dataCountry.postValue(country?.name ?: "")
    }

    private fun setAreaData() {
        val area = DataTransmitter.getAreaPlain()
        _dataArea.postValue(area?.name ?: "")
    }

    fun onCountryCleared() {
        DataTransmitter.postCountry(null)
        setCountryData()
    }

    fun onAreaCleared() {
        DataTransmitter.postAreaPlain(null)
        setAreaData()
    }

}