package ru.practicum.android.diploma.feature.search.data.network

import ru.practicum.android.diploma.feature.search.data.dtomodels.VacancyDtoFull
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.core.util.token

interface HeadHunterApi {

    @Headers(
        "Authorization: Bearer $token",
        "HH-User-Agent: HeadHunterApi (maximus616161@gmail.com)"
    )

    @GET("/vacancies?describe_arguments=true")
    suspend fun getVacancies(
        @Query("text") text: String,
        @QueryMap options: Map<String, Int>,
        @Query("area") area: String?,
        @Query("area") region: String?,
        @Query("industry") industry: String?,
        @Query("salary") salary: Int?,
        @Query("only_with_salary") withSalary: Boolean
    ): VacanciesDtoResponse

    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancy(@Path("vacancy_id") id: String): VacancyDtoFull

    @GET("/vacancies/{vacancy_id}/similar_vacancies")
    suspend fun getSimilarVacancies(@Path("vacancy_id") id: String): VacanciesDtoResponse
}