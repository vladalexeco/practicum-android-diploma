package ru.practicum.android.diploma.feature.search.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.core.util.Resource
import ru.practicum.android.diploma.feature.search.domain.models.VacancyFull
import ru.practicum.android.diploma.feature.search.domain.models.VacancyShort

interface VacancyRepository {
    fun getVacancies(expression: String): Flow<Resource<List<VacancyShort>>>
    fun getVacancy(expression: String): Flow<Resource<VacancyFull>>
    fun getSimilarVacancies(expression: String): Flow<Resource<List<VacancyShort>>>
}