package ru.practicum.android.diploma.feature.favourite.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteVacancyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancyFullEntity: VacancyFullEntity)

    @Delete
    suspend fun deleteVacancy(vacancyFullEntity: VacancyFullEntity)

    @Query("SELECT * FROM vacancy_table")
    fun getAllVacancy(): Flow<List<VacancyFullEntity>>

    @Query("SELECT vacancyId FROM vacancy_table")
    suspend fun getAllVacancyIds(): List<Int>
}