package ru.practicum.android.diploma.feature.similar_vacancies.presentation

import ru.practicum.android.diploma.feature.search.domain.VacanciesResponse

sealed interface SimilarSearchState {
    object Loading : SimilarSearchState

    object ServerError : SimilarSearchState

    data class Content(
        val response: VacanciesResponse
    ) : SimilarSearchState

    object Error : SimilarSearchState

    object Empty : SimilarSearchState
}