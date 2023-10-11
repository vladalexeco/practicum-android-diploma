package ru.practicum.android.diploma.feature.favourite.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.feature.search.domain.models.VacancyFull


@Database(
    version = 1,
    entities = [
        VacancyFullEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getVacancyDao(): FavoriteVacancyDao
}