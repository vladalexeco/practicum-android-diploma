package ru.practicum.android.diploma.feature.favourite.presentation

import ru.practicum.android.diploma.feature.favourite.data.model.VacancyFullEntity
import ru.practicum.android.diploma.feature.search.domain.models.VacancyFull

sealed class FavoriteVacancyState {
    data object Empty : FavoriteVacancyState()
    data class VacancyLoaded(val vacancy: List<VacancyFull>) : FavoriteVacancyState()
}