package ru.practicum.android.diploma.feature.filter.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.core.util.STATUS_CODE_NO_NETWORK_CONNECTION
import ru.practicum.android.diploma.core.util.STATUS_CODE_SUCCESS
import ru.practicum.android.diploma.feature.filter.domain.util.NetworkError
import ru.practicum.android.diploma.feature.filter.domain.util.Resource
import ru.practicum.android.diploma.feature.filter.data.network.NetworkDirectoryClient
import ru.practicum.android.diploma.feature.filter.data.network.dto.response.IndustryResponse
import ru.practicum.android.diploma.feature.filter.data.network.dto.Request
import ru.practicum.android.diploma.feature.filter.data.network.dto.model.mapToArea
import ru.practicum.android.diploma.feature.filter.data.network.dto.model.mapToCountry
import ru.practicum.android.diploma.feature.filter.data.network.dto.model.mapToIndustry
import ru.practicum.android.diploma.feature.filter.data.network.dto.response.AreaPlainResponse
import ru.practicum.android.diploma.feature.filter.data.network.dto.response.AreaResponse
import ru.practicum.android.diploma.feature.filter.data.network.dto.response.CountryResponse
import ru.practicum.android.diploma.feature.filter.domain.api.DirectoryRepository
import ru.practicum.android.diploma.feature.filter.domain.model.Area
import ru.practicum.android.diploma.feature.filter.domain.model.AreaPlain
import ru.practicum.android.diploma.feature.filter.domain.model.Country
import ru.practicum.android.diploma.feature.filter.domain.model.Industry

class DirectoryRepositoryImpl(
    private val networkDirectoryClient: NetworkDirectoryClient
) : DirectoryRepository {

    override fun getIndustries(): Flow<Resource<List<Industry>>> = flow {
        val response = networkDirectoryClient.doRequest(Request.IndustryRequest)
        when (response.resultCode) {
            STATUS_CODE_NO_NETWORK_CONNECTION -> emit(Resource.Error(networkError = NetworkError.BAD_CONNECTION))
            STATUS_CODE_SUCCESS -> emit(Resource.Success((response as IndustryResponse).industries.map { industryDto ->
                industryDto.mapToIndustry()
            }))

            else -> emit(Resource.Error(networkError = NetworkError.SERVER_ERROR))
        }
    }

    override fun getCountries(): Flow<Resource<List<Country>>> = flow {
        val response = networkDirectoryClient.doRequest(Request.CountryRequest)
        when (response.resultCode) {
            STATUS_CODE_NO_NETWORK_CONNECTION -> emit(Resource.Error(networkError = NetworkError.BAD_CONNECTION))
            STATUS_CODE_SUCCESS -> emit(Resource.Success((response as CountryResponse).countries.map { countryDto ->
                countryDto.mapToCountry()
            }))

            else -> emit(Resource.Error(networkError = NetworkError.SERVER_ERROR))
        }
    }

    override fun getAreas(areaId: String): Flow<Resource<List<Area>>> = flow {
        val response = networkDirectoryClient.doRequest(Request.AreaRequest(areaId))
        when (response.resultCode) {
            STATUS_CODE_NO_NETWORK_CONNECTION -> emit(Resource.Error(networkError = NetworkError.BAD_CONNECTION))
            STATUS_CODE_SUCCESS -> emit(Resource.Success((response as AreaResponse).areas.map { areaDto ->
                areaDto.mapToArea()
            }))

            else -> emit(Resource.Error(networkError = NetworkError.SERVER_ERROR))
        }
    }

    override fun getAllAreas(): Flow<Resource<List<Area>>> = flow {
        val response = networkDirectoryClient.doRequest(Request.AllAreasRequest)
        when (response.resultCode) {
            STATUS_CODE_NO_NETWORK_CONNECTION -> emit(Resource.Error(networkError = NetworkError.BAD_CONNECTION))
            STATUS_CODE_SUCCESS -> emit(Resource.Success((response as AreaResponse).areas.map { areaDto ->
                areaDto.mapToArea()
            }))

            else -> emit(Resource.Error(networkError = NetworkError.SERVER_ERROR))
        }
    }

    override fun getAreaPlain(areaId: String): Flow<Resource<AreaPlain>> = flow {
        val response = networkDirectoryClient.doRequest(Request.AreaPlainRequest(areaId))
        when (response.resultCode) {
            -1 -> emit(Resource.Error(networkError = NetworkError.BAD_CONNECTION))
            200 -> emit(
                Resource.Success(
                    data = AreaPlain(
                        id = (response as AreaPlainResponse).id,
                        parentId = response.parent_id,
                        name = response.name
                    )
                )
            )

            else -> emit(Resource.Error(networkError = NetworkError.SERVER_ERROR))
        }
    }
}