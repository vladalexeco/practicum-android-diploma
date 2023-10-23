package ru.practicum.android.diploma.feature.search.data

interface NetworkClient {
    suspend fun getVacancies(dto: Any) : Response
    suspend fun getVacancy(dto: Any) : Response
    suspend fun getSimilarVacancies(dto: Any) : Response
}