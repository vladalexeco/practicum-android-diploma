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

}