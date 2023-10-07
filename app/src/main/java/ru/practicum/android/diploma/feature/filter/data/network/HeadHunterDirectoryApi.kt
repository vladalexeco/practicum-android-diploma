package ru.practicum.android.diploma.feature.filter.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import ru.practicum.android.diploma.feature.filter.data.network.dto.model.CountryDto
import ru.practicum.android.diploma.feature.filter.data.network.dto.model.IndustryDto
import ru.practicum.android.diploma.feature.filter.data.network.dto.response.AreaResponse

interface HeadHunterDirectoryApi {

    @GET("/industries")
    suspend fun getIndustries(): List<IndustryDto>

    @GET("/areas/countries")
    suspend fun getCountries(): List<CountryDto>

    @GET("/areas/{area_id}")
    suspend fun getAreas(
        @Path("area_id") areaId: String
    ): AreaResponse

}