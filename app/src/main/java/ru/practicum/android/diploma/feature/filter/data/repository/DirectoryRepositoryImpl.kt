package ru.practicum.android.diploma.feature.filter.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.feature.filter.domain.util.NetworkError
import ru.practicum.android.diploma.feature.filter.domain.util.Resource
import ru.practicum.android.diploma.feature.filter.data.network.NetworkDirectoryClient
import ru.practicum.android.diploma.feature.filter.data.network.dto.IndustrySearchResponse
import ru.practicum.android.diploma.feature.filter.data.network.dto.Request
import ru.practicum.android.diploma.feature.filter.data.network.dto.mapToIndustry
import ru.practicum.android.diploma.feature.filter.domain.api.DirectoryRepository
import ru.practicum.android.diploma.feature.filter.domain.model.Industry

class DirectoryRepositoryImpl(
    private val networkDirectoryClient: NetworkDirectoryClient
) : DirectoryRepository  {
    override fun getIndustries(): Flow<Resource<List<Industry>>> = flow {
        val response =networkDirectoryClient.doRequest(Request.INDUSTRY_REQUEST)

        when(response.resultCode) {

            -1 -> {
                emit(Resource.Error(networkError = NetworkError.BAD_CONNECTION))
            }

            200 -> {
                emit(Resource.Success((response as IndustrySearchResponse).industries.map { industryDto ->
                    industryDto.mapToIndustry()
                }))
            }

            else -> {
                emit(Resource.Error(networkError = NetworkError.UNKNOWN_ERROR))
            }
        }
    }
}