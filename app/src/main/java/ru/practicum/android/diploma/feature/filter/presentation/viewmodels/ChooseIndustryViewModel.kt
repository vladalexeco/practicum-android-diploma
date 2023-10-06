package ru.practicum.android.diploma.feature.filter.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.feature.filter.domain.model.Industry
import ru.practicum.android.diploma.feature.filter.domain.model.IndustryAreaModel
import ru.practicum.android.diploma.feature.filter.domain.usecase.GetIndustriesUseCase
import ru.practicum.android.diploma.feature.filter.domain.util.DataResponse
import ru.practicum.android.diploma.feature.filter.presentation.states.IndustriesState

class ChooseIndustryViewModel(private val industriesUseCase: GetIndustriesUseCase) : ViewModel() {

    private val industriesStateLiveData = MutableLiveData<IndustriesState>()
    fun observeIndustriesState(): LiveData<IndustriesState> = industriesStateLiveData

    //todo Удалить, когда будет реализован поиск отраслей
    private val industriesMock = arrayListOf(
        Industry("1", "Отрасль 1", arrayListOf(
            Industry("1.1", "Отрасль 1.1"),
            Industry("1.2", "Отрасль 1.2"),
            Industry("1.3", "Отрасль 1.3"),
            Industry("1.4", "Отрасль 1.4"),
            Industry("1.5", "Отрасль 1.5")
        )),
        Industry("2", "Отрасль 2", arrayListOf(
            Industry("2.1", "Отрасль 2.1"),
            Industry("2.2", "Отрасль 2.2"),
            Industry("2.3", "Отрасль 2.3"),
            Industry("2.4", "Отрасль 2.4"),
            Industry("2.5", "Отрасль 2.5")
        ))
    )

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
    //todo Убрать mock-обработку результата, вытащить код из комментария, когда будет реализован поиск отраслей
    private suspend fun processResult(result: DataResponse<Industry>) {

        industriesStateLiveData.value =
            IndustriesState.DisplayIndustries(getFullIndustriesList(industriesMock))

        /*
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
        */
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

    fun onIndustryClicked(industry: IndustryAreaModel) {
        //todo
    }
}