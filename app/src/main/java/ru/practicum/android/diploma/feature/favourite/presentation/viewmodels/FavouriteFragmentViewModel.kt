package ru.practicum.android.diploma.feature.favourite.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.feature.favourite.domain.usecase.GetAllVacancyUseCase
import ru.practicum.android.diploma.feature.favourite.presentation.FavoriteVacancyState
import ru.practicum.android.diploma.feature.search.domain.models.VacancyFull

class FavouriteFragmentViewModel(private val getAllVacancyUseCase: GetAllVacancyUseCase) : ViewModel() {

    private var _pagingData = MutableLiveData<PagingData<VacancyFull>>()
    val pagingData: LiveData<PagingData<VacancyFull>> = _pagingData

    fun getAllVacancies() {
        viewModelScope.launch {
            getAllVacancyUseCase.getAllVacancy().cachedIn(viewModelScope).collect() {
                _pagingData.value = it
            }
        }
    }

}