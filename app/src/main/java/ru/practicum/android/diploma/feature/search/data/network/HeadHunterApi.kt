package ru.practicum.android.diploma.feature.search.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.core.util.token

interface HeadHunterApi {

    @Headers("Authorization: Bearer $token",
        "HH-User-Agent: HeadHunterApi (maximus616161@gmail.com)")

    // Данна функция является тестовой и не имеет фильтрации
    @GET("/vacancies?describe_arguments=true")
    suspend fun getVacancies(@Query("text") text: String): VacanciesResponse

    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancy(@Path("vacancy_id") id: String): VacancyResponse

    @GET("/vacancies/{vacancy_id}/similar_vacancies")
    suspend fun getSimilarVacancies(@Path("vacancy_id") id: String): VacanciesResponse
}