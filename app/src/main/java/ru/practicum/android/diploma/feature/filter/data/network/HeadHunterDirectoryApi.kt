package ru.practicum.android.diploma.feature.filter.data.network

import retrofit2.http.GET
import ru.practicum.android.diploma.feature.filter.data.network.dto.IndustrySearchResponse

interface HeadHunterDirectoryApi {

    @GET("/industries/")
    suspend fun getIndustries(): IndustrySearchResponse
}