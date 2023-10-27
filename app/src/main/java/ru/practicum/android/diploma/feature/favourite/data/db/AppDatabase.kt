package ru.practicum.android.diploma.feature.favourite.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.practicum.android.diploma.feature.favourite.data.model.VacancyFullEntity

@Database(
    version = 1,
    entities = [VacancyFullEntity::class]
)
@TypeConverters(DatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getVacancyDao(): FavoriteVacancyDao
}