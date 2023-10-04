package ru.practicum.android.diploma.feature.search.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.core.util.Resource
import ru.practicum.android.diploma.feature.search.data.network.SearchRequest
import ru.practicum.android.diploma.feature.search.data.network.VacanciesDtoResponse
import ru.practicum.android.diploma.feature.search.domain.VacancyRepository
import ru.practicum.android.diploma.feature.search.domain.models.VacancyFull
import ru.practicum.android.diploma.feature.search.domain.models.VacancyShort

class VacancyRepositoryImpl(private val networkClient: NetworkClient): VacancyRepository {
    override fun getVacancies(expression: String): Flow<Resource<List<VacancyShort>>> = flow {
        val response = networkClient.getVacancies(SearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(errorCode = -1))
            }
            200 -> {
                with (response as VacanciesDtoResponse) {
                    val data = .map { it.toTrack() }
                    emit(Resource.Success(data))
                }
            }
            else -> {
                emit(Resource.Error(errorCode = -2))
            }
        }
    }

    override fun getVacancy(expression: String): Flow<Resource<VacancyFull>> {
        TODO("Not yet implemented")
    }

    override fun getSimilarVacancies(expression: String): Flow<Resource<List<VacancyShort>>> {
        TODO("Not yet implemented")
    }
}