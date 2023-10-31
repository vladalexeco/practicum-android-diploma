package ru.practicum.android.diploma.feature.similar_vacancies.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.util.STATUS_CODE_NO_NETWORK_CONNECTION
import ru.practicum.android.diploma.core.util.STATUS_CODE_SERVER_ERROR
import ru.practicum.android.diploma.feature.similar_vacancies.domain.GetSimilarVacanciesUseCase
import ru.practicum.android.diploma.feature.similar_vacancies.presentation.SimilarSearchState

class SimilarVacanciesViewModel(private val getSimilarVacanciesUseCase: GetSimilarVacanciesUseCase) :
    ViewModel() {

    private val _stateLiveData = MutableLiveData<SimilarSearchState>()
    val stateLiveData: LiveData<SimilarSearchState> = _stateLiveData

    fun searchSimilarVacancies(vacancyId: String) {
        renderState(SimilarSearchState.Loading)

        viewModelScope.launch(Dispatchers.IO) {
            getSimilarVacanciesUseCase
                .getSimilarVacancies(vacancyId)
                .collect { pair ->
                    when {
                        pair.second == STATUS_CODE_NO_NETWORK_CONNECTION -> renderState(
                            SimilarSearchState.Error
                        )

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
}