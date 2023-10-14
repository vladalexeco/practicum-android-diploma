package ru.practicum.android.diploma.feature.similar_vacancies.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.util.DataTransmitter
import ru.practicum.android.diploma.feature.similar_vacancies.domain.GetSimilarVacanciesUseCase
import ru.practicum.android.diploma.feature.similar_vacancies.presentation.SimilarSearchState

class SimilarVacanciesViewModel(private val getSimilarVacanciesUseCase: GetSimilarVacanciesUseCase): ViewModel() {

    private val _stateLiveData = MutableLiveData<SimilarSearchState>()
    val stateLiveData : LiveData<SimilarSearchState> = _stateLiveData

    init {
        searchSimilarVacancies()
    }

    private fun searchSimilarVacancies() {
            renderState(SimilarSearchState.Loading)

            viewModelScope.launch (Dispatchers.IO) {
                getSimilarVacanciesUseCase
                    .getSimilarVacancies(DataTransmitter.getId())
                    .collect { pair ->
                        when {
                            pair.second == -1 || pair.second == 400 -> {
                                renderState(
                                    SimilarSearchState.Error
                                )
                            }

                            pair.second == 500 -> {
                                renderState(
                                    SimilarSearchState.ServerError
                                )
                            }

                            pair.first?.items.isNullOrEmpty() -> {
                                renderState(
                                    SimilarSearchState.Empty
                                )
                            }

                            else -> {
                                renderState(
                                    SimilarSearchState.Content(
                                        response = pair.first!!
                                    )
                                )

                            }
                        }
                    }
            }

    }

    private fun renderState(state: SimilarSearchState) {
        _stateLiveData.postValue(state)
    }
}