package ru.practicum.android.diploma.core.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.feature.favourite.data.AppDatabase

val favoriteModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "vacancy_database.db")
            .build()
    }

}