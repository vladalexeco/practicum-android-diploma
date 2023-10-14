package ru.practicum.android.diploma.feature.similar_vacancies.presentation

import ru.practicum.android.diploma.feature.search.domain.VacanciesResponse

sealed interface SimilarSearchState {
    object Loading : SimilarSearchState

    data class Content(
        val response: VacanciesResponse
    ) : SimilarSearchState

    data class Error(
        val response: VacanciesResponse? = null
    ) : SimilarSearchState

    data class Empty(
        val response: VacanciesResponse? = null
    ) : SimilarSearchState
}