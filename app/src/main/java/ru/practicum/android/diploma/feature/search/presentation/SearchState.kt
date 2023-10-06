package ru.practicum.android.diploma.feature.search.presentation

import ru.practicum.android.diploma.feature.search.domain.VacanciesResponse

sealed interface SearchState{
    object Loading : SearchState

    data class Content(
        val response: VacanciesResponse
    ) : SearchState

    data class Error(
        val response: VacanciesResponse? = null
    ) : SearchState

    data class Empty(
        val response: VacanciesResponse? = null
    ) : SearchState

    data class ClearScreen(
        val response: VacanciesResponse? = null
    ) : SearchState
}