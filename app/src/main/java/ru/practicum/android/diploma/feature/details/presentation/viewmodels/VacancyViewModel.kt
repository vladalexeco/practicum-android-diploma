package ru.practicum.android.diploma.feature.details.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.feature.details.domain.usecases.MakeCallUseCase
import ru.practicum.android.diploma.feature.details.domain.usecases.SendEmailUseCase
import ru.practicum.android.diploma.feature.details.domain.usecases.ShareVacancyUseCase
import ru.practicum.android.diploma.feature.details.presentation.DataState
import ru.practicum.android.diploma.feature.favourite.domain.usecase.AddVacancyToFavouriteUseCase
import ru.practicum.android.diploma.feature.favourite.domain.usecase.GetFavoriteIdsUseCase
import ru.practicum.android.diploma.feature.favourite.domain.usecase.GetVacancyByIdUseCase
import ru.practicum.android.diploma.feature.favourite.domain.usecase.RemoveVacancyFromFavouriteUseCase
import ru.practicum.android.diploma.feature.search.domain.GetVacancyUseCase
import ru.practicum.android.diploma.feature.search.domain.models.VacancyFull

class VacancyViewModel(
    private val emailUseCase: SendEmailUseCase,
    private val callUseCase: MakeCallUseCase,
    private val sharingUseCase: ShareVacancyUseCase,
    private val getVacancyUseCase: GetVacancyUseCase,
    private val addVacancyToFavouriteUseCase: AddVacancyToFavouriteUseCase,
    private val removeVacancyFromFavouriteUseCase: RemoveVacancyFromFavouriteUseCase,
    private val getFavoriteIdsUseCase: GetFavoriteIdsUseCase,
    private val getVacancyByIdUseCase: GetVacancyByIdUseCase,
) : ViewModel() {

    var currentVacancyFull: VacancyFull? = null

    private var _dataState = MutableLiveData<DataState>()
    val dataState: LiveData<DataState> = _dataState

    private var _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    fun onContactEmailClicked(email: String) {
        emailUseCase.invoke(email)
    }

    fun onContactPhoneClicked(phoneNumber: String) {
        callUseCase.invoke(phoneNumber)
    }

    fun onShareVacancyClicked(vacancy: String) {
        sharingUseCase.invoke(vacancy)
    }

    fun getDetailedVacancyData(vacancyId: String?) {

        _dataState.postValue(DataState.Loading)

        viewModelScope.launch {
            getVacancyUseCase.getVacancy(vacancyId ?: "").collect { serverResponse ->

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

    fun onFavoriteButtonClick(vacancyFull: VacancyFull) {
        viewModelScope.launch(Dispatchers.IO) {
            val vacancyIds = getFavoriteIdsUseCase.getFavoriteIds().singleOrNull()
            val vacancyId = vacancyFull.id
            if (vacancyIds?.contains(vacancyId) == true) {
                removeVacancyFromFavouriteUseCase.removeVacancy(vacancyFull)
            } else {
                addVacancyToFavouriteUseCase.addVacancy(vacancyFull)
            }

            val updatedVacancyIds = getFavoriteIdsUseCase.getFavoriteIds().singleOrNull()
            val isFavorite = updatedVacancyIds?.contains(vacancyId) == true
            _isFavorite.postValue(isFavorite)
        }
    }

    fun checkFavoriteStatus(vacancyFull: VacancyFull) {
        viewModelScope.launch(Dispatchers.IO) {
            val vacancyIds = getFavoriteIdsUseCase.getFavoriteIds().singleOrNull()
            val vacancyId = vacancyFull.id
            val isFavorite = vacancyIds?.contains(vacancyId) == true
            _isFavorite.postValue(isFavorite)
        }
    }

    fun getVacancyById(vacancyId: String): LiveData<VacancyFull?> {
        val vacancyLiveData = MutableLiveData<VacancyFull?>()
        viewModelScope.launch(Dispatchers.IO) {
            val vacancy = getVacancyByIdUseCase.getVacancyById(vacancyId)
            vacancyLiveData.postValue(vacancy)
        }
        return vacancyLiveData
    }
}