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
import ru.practicum.android.diploma.feature.filter.domain.model.Industry
import ru.practicum.android.diploma.feature.filter.domain.usecase.GetIndustriesUseCase
import ru.practicum.android.diploma.feature.filter.domain.util.DataResponse
import ru.practicum.android.diploma.feature.filter.domain.util.NetworkError
import ru.practicum.android.diploma.feature.filter.presentation.states.IndustriesState

class ChooseIndustryViewModel(
    private val industriesUseCase: GetIndustriesUseCase,
    private val resources: Resources
) : ViewModel() {

    private var _dataIndustry = MutableLiveData<Industry>()
    fun dataIndustry(): LiveData<Industry> = _dataIndustry

    private val industriesStateLiveData = MutableLiveData<IndustriesState>()
    fun observeIndustriesState(): LiveData<IndustriesState> = industriesStateLiveData

    private var industries = arrayListOf<Industry>()
    private lateinit var filteredIndustries: List<Industry>

    init {
        initIndustriesData()
    }

    private var previousIndustryClicked: Industry? = null

    private fun initIndustriesData() {
        viewModelScope.launch {
            industriesUseCase.invoke().collect { result ->
                processResult(result)
            }
        }
    }

    private suspend fun processResult(result: DataResponse<Industry>) {
        if (result.data != null) {
            industries.apply {
                clear()
                addAll(getFullIndustriesList(result.data))
            }
            filteredIndustries = industries
            industriesStateLiveData.value =
                IndustriesState.DisplayIndustries(filteredIndustries)
        } else {
            industriesStateLiveData.value = when (result.networkError!!) {
                NetworkError.BAD_CONNECTION -> getBadConnectionErrorState()
                NetworkError.SERVER_ERROR -> getServerErrorState()
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

    fun onIndustryClicked(industryClicked: Industry, previousIndustryClicked: Industry?) {
        val industryPosition = industries.indexOf(industryClicked)
        val previousIndustryPosition =
            if (previousIndustryClicked != null) industries.indexOf(previousIndustryClicked) else -1

        industries[industryPosition] = industryClicked
        if (previousIndustryPosition != -1) industries[previousIndustryPosition].isChecked = false

        _dataIndustry.postValue(industryClicked)
    }

    fun onIndustryTextChanged(filterText: String?) {
        filterIndustries(filterText)
    }

    private fun filterIndustries(filterText: String?) {
        if (filterText.isNullOrEmpty()) {
            filteredIndustries = industries
            industriesStateLiveData.value = IndustriesState.DisplayIndustries(filteredIndustries)
        } else {
            val filteredIndustries = (industries.filter {
                it.name.contains(filterText, true)
            })
            if (filteredIndustries.isNotEmpty()) {
                industriesStateLiveData.value =
                    IndustriesState.DisplayIndustries(filteredIndustries)
            } else {
                industriesStateLiveData.value =
                    IndustriesState.Error(
                        resources.getString(R.string.filter_message_no_industry),
                        R.drawable.search_placeholder_nothing_found
                    )
            }
        }
    }
}
