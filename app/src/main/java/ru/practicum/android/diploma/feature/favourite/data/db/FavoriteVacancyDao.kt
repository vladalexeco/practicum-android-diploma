package ru.practicum.android.diploma.feature.favourite.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.feature.favourite.data.model.VacancyFullEntity

@Dao
interface FavoriteVacancyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancyFullEntity: VacancyFullEntity)

    @Delete
    suspend fun deleteVacancy(vacancyFullEntity: VacancyFullEntity)

    @Query("SELECT * FROM vacancy_table")
    suspend fun getAllVacancy(): List<VacancyFullEntity>

    @Query("SELECT id FROM vacancy_table")
    suspend fun getAllVacancyIds(): List<String>

    @Query("SELECT * FROM vacancy_table WHERE id = :id")
    suspend fun getVacancyById(id: String): VacancyFullEntity?
}