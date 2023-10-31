package ru.practicum.android.diploma.feature.search.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.core.util.Resource
import ru.practicum.android.diploma.feature.filter.domain.model.FilterSettings

interface VacancyRepository {
    fun getVacancies(
        expression: String,
        pages: Int,
        perPage: Int,
        page: Int,
        filterSettings: FilterSettings
    ): Flow<Resource<VacanciesResponse>>

    fun getVacancy(expression: String): Flow<Resource<VacancyResponse>>
    fun getSimilarVacancies(expression: String): Flow<Resource<VacanciesResponse>>
}