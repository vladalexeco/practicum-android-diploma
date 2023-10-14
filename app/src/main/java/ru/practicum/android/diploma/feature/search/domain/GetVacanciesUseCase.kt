package ru.practicum.android.diploma.feature.search.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.core.util.Resource

class GetVacanciesUseCase(private val repository: VacancyRepository) {
    fun getVacancies(
        expression: String,
        pages: Int,
        perPage: Int,
        page: Int
    ): Flow<Pair<VacanciesResponse?, Int?>> {
        return repository.getVacancies(expression, pages, perPage, page).map { result ->
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