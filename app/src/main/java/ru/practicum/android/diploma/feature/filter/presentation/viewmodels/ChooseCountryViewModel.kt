package ru.practicum.android.diploma.feature.filter.presentation.viewmodels

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.feature.filter.domain.model.Country
import ru.practicum.android.diploma.feature.filter.domain.usecase.GetCountriesUseCase
import ru.practicum.android.diploma.feature.filter.domain.util.DataResponse
import ru.practicum.android.diploma.feature.filter.domain.util.NetworkError
import ru.practicum.android.diploma.feature.filter.presentation.states.CountriesState
import ru.practicum.android.diploma.feature.filter.presentation.states.LiveDataResource

class ChooseCountryViewModel(
    private val countryUseCase: GetCountriesUseCase,
    private val resources: Resources
) : ViewModel() {

    private var _dataCountry = MutableLiveData<LiveDataResource>()
    val dataCountry: LiveData<LiveDataResource> = _dataCountry

    init {
        initIndustries()
    }

    private fun initIndustries() {
        viewModelScope.launch {
            countryUseCase.invoke().collect { result ->
                processResult(result)
            }
        }
    }

    private fun processResult(result: DataResponse<Country>) {
        if (result.data != null) {
            val countries: ArrayList<Country> = arrayListOf()
            countries.addAll(result.data)
            _dataCountry.postValue(
                LiveDataResource.CountryStateStorage(
                    data = CountriesState.DisplayCountries(
                        countries
                    )
                )
            )
        }
        else {
            when (result.networkError!!) {
                NetworkError.BAD_CONNECTION -> _dataCountry.postValue(
                    LiveDataResource.CountryStateStorage(
                        data = CountriesState.Error(
                            resources.getString(R.string.message_no_internet),
                            R.drawable.search_placeholder_internet_problem
                        )
                    )
                )
                NetworkError.SERVER_ERROR -> _dataCountry.postValue(
                    LiveDataResource.CountryStateStorage(
                        data = CountriesState.Error(
                            resources.getString(R.string.message_server_error),
                            R.drawable.search_placeholder_server_not_responding
                        )
                    )
                )
            }
        }
    }

    fun onCountryClicked(country: Country) {
        _dataCountry.postValue(LiveDataResource.CountryStorage(data = country))
    }
}