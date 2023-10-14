package ru.practicum.android.diploma.feature.similar_vacancies.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.core.util.Resource
import ru.practicum.android.diploma.feature.search.domain.VacanciesResponse
import ru.practicum.android.diploma.feature.search.domain.VacancyRepository

class GetSimilarVacanciesUseCase(private val repository: VacancyRepository) {

    fun getSimilarVacancies(expression: String): Flow<Pair<VacanciesResponse?, Int?>> {
        return repository.getSimilarVacancies(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.errorCode)
                }
            }
        }
    }
}