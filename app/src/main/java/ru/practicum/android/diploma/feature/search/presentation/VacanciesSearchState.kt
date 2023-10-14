package ru.practicum.android.diploma.feature.search.presentation

import ru.practicum.android.diploma.feature.search.domain.VacanciesResponse

sealed interface VacanciesSearchState{
    object Loading : VacanciesSearchState

    data class Content(
        val response: VacanciesResponse
    ) : VacanciesSearchState

    data class Error(
        val response: VacanciesResponse? = null
    ) : VacanciesSearchState

    data class Empty(
        val response: VacanciesResponse? = null
    ) : VacanciesSearchState

    data class ClearScreen(
        val response: VacanciesResponse? = null
    ) : VacanciesSearchState
}