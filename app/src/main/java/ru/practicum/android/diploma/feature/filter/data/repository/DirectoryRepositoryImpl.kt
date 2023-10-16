package ru.practicum.android.diploma.feature.filter.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.feature.filter.domain.util.NetworkError
import ru.practicum.android.diploma.feature.filter.domain.util.Resource
import ru.practicum.android.diploma.feature.filter.data.network.NetworkDirectoryClient
import ru.practicum.android.diploma.feature.filter.data.network.dto.response.IndustryResponse
import ru.practicum.android.diploma.feature.filter.data.network.dto.Request
import ru.practicum.android.diploma.feature.filter.data.network.dto.model.mapToArea
import ru.practicum.android.diploma.feature.filter.data.network.dto.model.mapToCountry
import ru.practicum.android.diploma.feature.filter.data.network.dto.model.mapToIndustry
import ru.practicum.android.diploma.feature.filter.data.network.dto.response.AreaResponse
import ru.practicum.android.diploma.feature.filter.data.network.dto.response.CountryResponse
import ru.practicum.android.diploma.feature.filter.domain.api.DirectoryRepository
import ru.practicum.android.diploma.feature.filter.domain.model.Area
import ru.practicum.android.diploma.feature.filter.domain.model.Country
import ru.practicum.android.diploma.feature.filter.domain.model.Industry

class DirectoryRepositoryImpl(
    private val networkDirectoryClient: NetworkDirectoryClient
) : DirectoryRepository {
    override fun getIndustries(): Flow<Resource<List<Industry>>> = flow {

        val response = networkDirectoryClient.doRequest(Request.IndustryRequest)

        when (response.resultCode) {
            -1 -> emit(Resource.Error(networkError = NetworkError.BAD_CONNECTION))
            200 -> emit(Resource.Success((response as IndustryResponse).industries.map { it.mapToIndustry() }))
            else -> emit(Resource.Error(networkError = NetworkError.SERVER_ERROR))
        }
    }

    override fun getCountries(): Flow<Resource<List<Country>>> = flow {

        val response = networkDirectoryClient.doRequest(Request.CountryRequest)

        when (response.resultCode) {
            -1 -> emit(Resource.Error(networkError = NetworkError.BAD_CONNECTION))
            200 -> emit(Resource.Success((response as CountryResponse).countries.map { it.mapToCountry() }))
            else -> emit(Resource.Error(networkError = NetworkError.SERVER_ERROR))
        }
    }

    override fun getAreas(areaId: String): Flow<Resource<List<Area>>> = flow {

        val response = networkDirectoryClient.doRequest(Request.AreaRequest(areaId))

        when (response.resultCode) {
            -1 -> emit(Resource.Error(networkError = NetworkError.BAD_CONNECTION))
            200 -> emit(Resource.Success((response as AreaResponse).areas.map { it.mapToArea() }))
            else -> emit(Resource.Error(networkError = NetworkError.SERVER_ERROR))
        }
    }

    override fun getAllAreas(): Flow<Resource<List<Area>>> = flow {

        val response = networkDirectoryClient.doRequest(Request.AllAreasRequest)

        when (response.resultCode) {
            -1 -> emit(Resource.Error(networkError = NetworkError.BAD_CONNECTION))
            200 -> emit(Resource.Success((response as AreaResponse).areas.map { it.mapToArea() }))
            else -> emit(Resource.Error(networkError = NetworkError.SERVER_ERROR))
        }
    }
}