package ru.practicum.android.diploma.feature.search.data

import ru.practicum.android.diploma.feature.filter.domain.model.FilterSettings
import ru.practicum.android.diploma.feature.search.data.network.SearchRequest

interface NetworkClient {
    suspend fun getVacancies(dto: SearchRequest, pages: Int, perPage: Int, page : Int, filterSettings: FilterSettings) : Response
    suspend fun getVacancy(dto: Any) : Response
    suspend fun getSimilarVacancies(dto: Any) : Response
}