package ru.practicum.android.diploma.feature.filter.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.util.DataTransmitter
import ru.practicum.android.diploma.feature.filter.domain.model.AreaPlain
import ru.practicum.android.diploma.feature.filter.domain.usecase.GetAreaPlainUseCase


class ChooseWorkPlaceViewModel(
    private val getAreaPlainUseCase: GetAreaPlainUseCase
) : ViewModel() {

    private var _dataAreaPlain = MutableLiveData<AreaPlain?>()
    val dataAreaPlain: LiveData<AreaPlain?> = _dataAreaPlain

    private var _dataCountry = MutableLiveData<String>()
    val dataCountry: LiveData<String> = _dataCountry

    private var _dataArea = MutableLiveData<String>()
    val dataArea: LiveData<String> = _dataArea

    fun initScreenData() {
        setCountryData()
        setAreaData()
    }

    fun getAreaPlain(areaId: String) {
        viewModelScope.launch {
            getAreaPlainUseCase(areaId).collect { result ->

                if (result.first != null) {
                    _dataAreaPlain.postValue(result.first)
                } else {
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