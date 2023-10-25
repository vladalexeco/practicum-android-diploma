package ru.practicum.android.diploma.core.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.practicum.android.diploma.feature.favourite.data.db.AppDatabase
import ru.practicum.android.diploma.feature.favourite.data.repository.FavoriteRepositoryImpl
import ru.practicum.android.diploma.feature.favourite.domain.repository.FavoriteRepository
import ru.practicum.android.diploma.feature.favourite.domain.usecase.GetAllVacancyUseCase
import ru.practicum.android.diploma.feature.favourite.domain.usecase.GetFavoriteIdsUseCase
import ru.practicum.android.diploma.feature.favourite.presentation.viewmodels.FavouriteFragmentViewModel

val favoriteModule = module {
    viewModelOf(::FavouriteFragmentViewModel)
    singleOf(::GetAllVacancyUseCase)
    singleOf(::GetFavoriteIdsUseCase)
    singleOf(::FavoriteRepositoryImpl) bind FavoriteRepository::class
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "vacancy_database")
            .fallbackToDestructiveMigration()
            .build()
    }
}