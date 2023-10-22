package ru.practicum.android.diploma.feature.favourite.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.feature.favourite.domain.usecase.GetAllVacancyUseCase
import ru.practicum.android.diploma.feature.favourite.presentation.FavoriteVacancyState

class FavouriteFragmentViewModel(private val getAllVacancyUseCase: GetAllVacancyUseCase) : ViewModel() {

    private val _state = MutableStateFlow<FavoriteVacancyState>(FavoriteVacancyState.Empty)
    val state: StateFlow<FavoriteVacancyState> = _state


    init {
        getAllVacancies()
    }

    fun getAllVacancies(){
        viewModelScope.launch {
            getAllVacancyUseCase.getAllVacancy().collect { vacancy ->
                if (vacancy.isNotEmpty()) {
                    _state.value= FavoriteVacancyState.VacancyLoaded(vacancy)
                } else {
                    _state.value= FavoriteVacancyState.Empty
                }
            }
        }
    }



}