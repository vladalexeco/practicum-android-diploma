package ru.practicum.android.diploma.feature.favourite.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.feature.favourite.data.model.VacancyFullEntity
import ru.practicum.android.diploma.feature.search.domain.models.VacancyFull

interface FavoriteRepository {


    suspend fun insertVacancy(vacancyFull: VacancyFull)


    suspend fun deleteVacancy(vacancyFull: VacancyFull)


    fun getAllVacancy(): Flow<List<VacancyFull>>


    fun getAllVacancyIds(): Flow<List<String>>

}