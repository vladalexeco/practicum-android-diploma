package ru.practicum.android.diploma.feature.filter.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.util.DataTransmitter
import ru.practicum.android.diploma.feature.filter.domain.usecase.GetAreaPlainUseCase
import ru.practicum.android.diploma.feature.filter.presentation.states.LiveDataResource

class ChooseWorkPlaceViewModel(
    private val getAreaPlainUseCase: GetAreaPlainUseCase
) : ViewModel() {

    private var _dataWorkplace = MutableLiveData<LiveDataResource>()
    val dataWorkplace: MutableLiveData<LiveDataResource> = _dataWorkplace

    fun initData() {
        setCountryData()
        setAreaData()
    }

    fun getAreaPlain(areaId: String) {
        viewModelScope.launch {
            getAreaPlainUseCase(areaId).collect { result ->
                if (result.first != null) _dataWorkplace.postValue(
                    LiveDataResource.AreaPlainStorage(
                        data = result.first
                    )
                )
            }
        }
    }

    private fun setCountryData() {
        val country = DataTransmitter.getCountry()
        _dataWorkplace.value = LiveDataResource.CountryNameStorage(data = country?.name ?: "")
    }

    private fun setAreaData() {
        val area = DataTransmitter.getAreaPlain()
        _dataWorkplace.value = LiveDataResource.AreaNameStorage(data = area?.name ?: "")
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