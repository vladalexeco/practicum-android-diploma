package ru.practicum.android.diploma.feature.favourite.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.feature.favourite.data.db.AppDatabase
import ru.practicum.android.diploma.feature.favourite.data.model.toVacancyFull
import ru.practicum.android.diploma.feature.favourite.data.model.toVacancyFullEntity

import ru.practicum.android.diploma.feature.favourite.domain.repository.FavoriteRepository
import ru.practicum.android.diploma.feature.search.domain.models.VacancyFull

class FavoriteRepositoryImpl(private val appDatabase: AppDatabase): FavoriteRepository {
    override suspend fun insertVacancy(vacancyFull: VacancyFull) {
        appDatabase.getVacancyDao().insertVacancy(vacancyFull.toVacancyFullEntity())
    }

    override suspend fun deleteVacancy(vacancyFull: VacancyFull) {
        appDatabase.getVacancyDao().deleteVacancy(vacancyFull.toVacancyFullEntity())

    }

    override fun getAllVacancy(): Flow<List<VacancyFull>> = flow {
        val listVacancies =appDatabase.getVacancyDao().getAllVacancy()
        emit(listVacancies.map { it.toVacancyFull() })
    }


    override fun getAllVacancyIds(): Flow<List<String>> = flow{
        val listId= appDatabase.getVacancyDao().getAllVacancyIds()
        emit(listId)
    }

    override suspend fun getVacancyById(id: String): VacancyFull? {
        val vacancyEntity = appDatabase.getVacancyDao().getVacancyById(id)
        return vacancyEntity?.toVacancyFull()
    }
}