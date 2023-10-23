package ru.practicum.android.diploma.feature.details.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.util.DataTransmitter
import ru.practicum.android.diploma.feature.details.domain.usecases.MakeCallUseCase
import ru.practicum.android.diploma.feature.details.domain.usecases.SendEmailUseCase
import ru.practicum.android.diploma.feature.details.domain.usecases.ShareVacancyUseCase
import ru.practicum.android.diploma.feature.details.presentation.DataState
import ru.practicum.android.diploma.feature.search.domain.GetVacancyUseCase
import ru.practicum.android.diploma.feature.search.domain.models.VacancyFull

class VacancyViewModel(
    private val emailUseCase: SendEmailUseCase,
    private val callUseCase: MakeCallUseCase,
    private val sharingUseCase: ShareVacancyUseCase,
    private val getVacancyUseCase: GetVacancyUseCase
) : ViewModel() {

    private var _dataState = MutableLiveData<DataState>()
    val dataState: LiveData<DataState> = _dataState

    fun onContactEmailClicked(email: String) {
        emailUseCase.invoke(email)
    }

    fun onContactPhoneClicked(phoneNumber: String) {
        callUseCase.invoke(phoneNumber)
    }

    fun onShareVacancyClicked(vacancy: String) {
        sharingUseCase.invoke(vacancy)
    }

    fun getDetailedVacancyData() {

        _dataState.postValue(DataState.Loading)

        viewModelScope.launch {
            getVacancyUseCase.getVacancy(DataTransmitter.getId()).collect { serverResponse ->

                if (serverResponse.first != null) {
                    val vacancyFull: VacancyFull = serverResponse.first!!.vacancy
                    _dataState.postValue(DataState.DataReceived(data = vacancyFull))
                }

                if (serverResponse.second != null) {
                    val responseCode: Int = serverResponse.second!!
                    _dataState.postValue(DataState.Failed(codeResponse = responseCode))
                }

            }
        }

    }
}