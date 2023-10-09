package ru.practicum.android.diploma.feature.similar_vacancies.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.util.DataTransmitter
import ru.practicum.android.diploma.feature.similar_vacancies.domain.GetSimilarVacanciesUseCase
import ru.practicum.android.diploma.feature.search.presentation.SearchState

class SimilarVacanciesViewModel(private val getSimilarVacanciesUseCase: GetSimilarVacanciesUseCase): ViewModel() {

    private val _stateLiveData = MutableLiveData<SearchState>()
    val stateLiveData : LiveData<SearchState> = _stateLiveData

    init {
        searchSimilarVacancies(DataTransmitter.getId())
    }

    private fun searchSimilarVacancies(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(SearchState.Loading)

            viewModelScope.launch (Dispatchers.IO) {
                getSimilarVacanciesUseCase
                    .getSimilarVacancies(newSearchText)
                    .collect { pair ->
                        when {
                            pair.second != null -> {
                                renderState(
                                    SearchState.Error()
                                )
                            }

                            pair.first?.items.isNullOrEmpty() -> {
                                renderState(
                                    SearchState.Empty()
                                )
                            }

                            else -> {
                                renderState(
                                    SearchState.Content(
                                        response = pair.first!!
                                    )
                                )

                            }
                        }
                    }
            }
        }
    }

    private fun renderState(state: SearchState) {
        _stateLiveData.postValue(state)
    }
}