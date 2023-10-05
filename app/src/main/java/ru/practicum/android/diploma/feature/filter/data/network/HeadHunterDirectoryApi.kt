package ru.practicum.android.diploma.feature.filter.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import ru.practicum.android.diploma.feature.filter.data.network.dto.response.AreaResponse
import ru.practicum.android.diploma.feature.filter.data.network.dto.response.CountryResponse
import ru.practicum.android.diploma.feature.filter.data.network.dto.response.IndustryResponse

interface HeadHunterDirectoryApi {

    @GET("/industries")
    suspend fun getIndustries(): IndustryResponse

    @GET("/areas/countries")
    suspend fun getCountries(): CountryResponse

    @GET("/areas/{area_id}")
    suspend fun getAreas(
        @Path("area_id") areaId: String
    ): AreaResponse

}