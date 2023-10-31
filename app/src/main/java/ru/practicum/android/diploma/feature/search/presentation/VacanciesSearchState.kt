package ru.practicum.android.diploma.feature.search.presentation

import ru.practicum.android.diploma.feature.search.domain.VacanciesResponse

sealed interface VacanciesSearchState{
    object Loading : VacanciesSearchState

    object ServerError : VacanciesSearchState

    data class Content(
        val response: VacanciesResponse
    ) : VacanciesSearchState

    object Error : VacanciesSearchState

    object Empty : VacanciesSearchState

    object ClearScreen : VacanciesSearchState
}