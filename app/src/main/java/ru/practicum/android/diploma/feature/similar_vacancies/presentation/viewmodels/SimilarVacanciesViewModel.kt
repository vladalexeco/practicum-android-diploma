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
                            pair.second == STATUS_CODE_NO_NETWORK_CONNECTION || pair.second == STATUS_CODE_BAD_REQUEST -> renderState(SimilarSearchState.Error)
                            pair.second == STATUS_CODE_SERVER_ERROR -> renderState(SimilarSearchState.ServerError)
                            pair.first?.items.isNullOrEmpty() -> renderState(SimilarSearchState.Empty)
                            else -> renderState(SimilarSearchState.Content(response = pair.first!!))
                        }
                    }
            }
    }

    private fun renderState(state: SimilarSearchState) {
        _stateLiveData.postValue(state)
    }

    companion object {
        const val STATUS_CODE_SERVER_ERROR = 500
        const val STATUS_CODE_BAD_REQUEST = 400
        const val STATUS_CODE_NO_NETWORK_CONNECTION = -1
    }
}