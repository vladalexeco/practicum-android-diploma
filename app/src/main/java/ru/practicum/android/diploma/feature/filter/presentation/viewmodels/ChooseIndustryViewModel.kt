package ru.practicum.android.diploma.feature.filter.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.feature.filter.domain.model.Industry
import ru.practicum.android.diploma.feature.filter.domain.usecase.GetIndustriesUseCase
import ru.practicum.android.diploma.feature.filter.domain.util.DataResponse
import ru.practicum.android.diploma.feature.filter.domain.util.NetworkError
import ru.practicum.android.diploma.feature.filter.presentation.states.IndustriesState

class ChooseIndustryViewModel(private val industriesUseCase: GetIndustriesUseCase) : ViewModel() {

    private val industriesStateLiveData = MutableLiveData<IndustriesState>()
    fun observeIndustriesState(): LiveData<IndustriesState> = industriesStateLiveData

    init {
        initScreen()
    }

    private fun initScreen() {
        viewModelScope.launch {
            industriesUseCase.invoke().collect { result ->
                processResult(result)
            }
        }
    }

    //todo Добавить strings из ресурсов
    private suspend fun processResult(result: DataResponse<Industry>) {
        if (result.data != null) {
            industriesStateLiveData.value =
                IndustriesState.DisplayIndustries(getFullIndustriesList(result.data))
        }
        else {
            when (result.networkError!!) {
                NetworkError.BAD_CONNECTION -> industriesStateLiveData.value =
                    IndustriesState.Error("Проверьте подключение к интернету")

                NetworkError.SERVER_ERROR -> industriesStateLiveData.value =
                    IndustriesState.Error("Ошибка сервера")
            }
        }
    }

    private suspend fun getFullIndustriesList(nestedIndustriesList: List<Industry>): ArrayList<Industry> =
        withContext(Dispatchers.Default) {
            val extendedIndustriesList: ArrayList<Industry> = arrayListOf()
            for (industry in nestedIndustriesList) {
                extendedIndustriesList.add(industry.copy(industries = null))
                if (industry.industries != null) extendedIndustriesList.addAll(industry.industries)
            }
            extendedIndustriesList.sortBy { it.name }
            extendedIndustriesList
        }

    fun onIndustryClicked(industry: Industry) {
        //todo
    }
}
