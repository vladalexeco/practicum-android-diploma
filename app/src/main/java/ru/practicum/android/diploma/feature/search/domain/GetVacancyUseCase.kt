package ru.practicum.android.diploma.feature.search.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.core.util.Resource

class GetVacancyUseCase(private val repository: VacancyRepository) {

    fun getVacancy(expression: String): Flow<Pair<VacancyResponse?, Int?>> {
        return repository.getVacancy(expression).map { result ->
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