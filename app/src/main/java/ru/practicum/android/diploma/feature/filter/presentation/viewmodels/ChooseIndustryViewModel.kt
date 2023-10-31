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
import ru.practicum.android.diploma.feature.filter.presentation.states.LiveDataResource

class ChooseIndustryViewModel(
    private val industriesUseCase: GetIndustriesUseCase,
    private val resources: Resources
) : ViewModel() {

    private var _dataIndustry = MutableLiveData<LiveDataResource>()
    val dataIndustry: LiveData<LiveDataResource> = _dataIndustry

    private var industries = arrayListOf<Industry>()
    private lateinit var filteredIndustries: List<Industry>

    init {
        initIndustriesData()
    }

    private var previousIndustryClicked: Industry? = null
    private var previousIndustryPositionInFullList = -1

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
            _dataIndustry.postValue(
                LiveDataResource.IndustryStateStorage(
                    data = IndustriesState.DisplayIndustries(
                        filteredIndustries
                    )
                )
            )
        } else {
            when (result.networkError!!) {
                NetworkError.BAD_CONNECTION -> _dataIndustry.postValue(
                    LiveDataResource.IndustryStateStorage(
                        data = getBadConnectionErrorState()
                    )
                )
                NetworkError.SERVER_ERROR -> _dataIndustry.postValue(
                    LiveDataResource.IndustryStateStorage(
                        data = getServerErrorState()
                    )
                )
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

    fun onIndustryClicked(
        industryClickedPosition: Int,
        industryClicked: Industry,
        notifyPreviousItemChanged: (Int) -> Unit,
    ) {
        var previousIndustryClickedPosition = -1
        if (previousIndustryClicked != null) {
            for (i in filteredIndustries.indices) {
                if (filteredIndustries[i].id == previousIndustryClicked!!.id) {
                    previousIndustryClickedPosition = i
                    notifyPreviousItemChanged(previousIndustryClickedPosition)
                }
            }
        }

        var industryPositionInFullList = -1
        for (i in industries.indices) {
            if (industries[i].id == industryClicked.id) industryPositionInFullList = i
        }

        industries[industryPositionInFullList].isChecked = industryClicked.isChecked
        if (previousIndustryPositionInFullList != -1) industries[previousIndustryPositionInFullList].isChecked =
            false

        if (previousIndustryClickedPosition != industryClickedPosition) {
            previousIndustryClicked = industryClicked
            previousIndustryPositionInFullList = industryPositionInFullList
        } else {
            previousIndustryClicked = null
            previousIndustryPositionInFullList = -1
        }
        _dataIndustry.postValue(LiveDataResource.IndustryStorage(data = industryClicked))
    }

    fun onIndustryTextChanged(filterText: String?) {
        filterIndustries(filterText)
    }

    private fun filterIndustries(filterText: String?) {
        if (filterText.isNullOrEmpty()) {
            filteredIndustries = industries
            _dataIndustry.postValue(
                LiveDataResource.IndustryStateStorage(
                    data = IndustriesState.DisplayIndustries(
                        filteredIndustries
                    )
                )
            )
        } else {
            filteredIndustries = (industries.filter {
                it.name.contains(filterText, true)
            })
            if (filteredIndustries.isNotEmpty()) {
                _dataIndustry.postValue(
                    LiveDataResource.IndustryStateStorage(
                        data = IndustriesState.DisplayIndustries(
                            filteredIndustries
                        )
                    )
                )
            } else {
                _dataIndustry.postValue(
                    LiveDataResource.IndustryStateStorage(
                        data = IndustriesState.Error(
                            resources.getString(R.string.filter_message_no_industry),
                            R.drawable.search_placeholder_nothing_found
                        )
                    )
                )
            }
        }
    }
}
