package ru.practicum.android.diploma.feature.search.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.core.util.Resource
import ru.practicum.android.diploma.core.util.toVacanciesResponse
import ru.practicum.android.diploma.core.util.toVacancyResponse
import ru.practicum.android.diploma.feature.search.data.network.SearchRequest
import ru.practicum.android.diploma.feature.search.data.network.VacanciesDtoResponse
import ru.practicum.android.diploma.feature.search.data.network.VacancyDtoResponse
import ru.practicum.android.diploma.feature.search.domain.VacanciesResponse
import ru.practicum.android.diploma.feature.search.domain.VacancyRepository
import ru.practicum.android.diploma.feature.search.domain.VacancyResponse

class VacancyRepositoryImpl(private val networkClient: NetworkClient): VacancyRepository {
    override fun getVacancies(expression: String): Flow<Resource<VacanciesResponse>> = flow {
        val response = networkClient.getVacancies(SearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(errorCode = -1))
            }
            200 -> {
                with (response as VacanciesDtoResponse) {
                    val data = response.toVacanciesResponse()
                    emit(Resource.Success(data))
                }
            }
            else -> {
                emit(Resource.Error(errorCode = -2))
            }
        }
    }

    override fun getVacancy(expression: String): Flow<Resource<VacancyResponse>> = flow {
        val response = networkClient.getVacancy(SearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(errorCode = -1))
            }
            200 -> {
                with (response as VacancyDtoResponse) {
                    val data = response.toVacancyResponse()
                    emit(Resource.Success(data))
                }
            }
            else -> {
                emit(Resource.Error(errorCode = -2))
            }
        }
    }

    override fun getSimilarVacancies(expression: String): Flow<Resource<VacanciesResponse>> = flow {
        val response = networkClient.getSimilarVacancies(SearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(errorCode = -1))
            }
            200 -> {
                with (response as VacanciesDtoResponse) {
                    val data = response.toVacanciesResponse()
                    emit(Resource.Success(data))
                }
            }
            else -> {
                emit(Resource.Error(errorCode = -2))
            }
        }
    }
}