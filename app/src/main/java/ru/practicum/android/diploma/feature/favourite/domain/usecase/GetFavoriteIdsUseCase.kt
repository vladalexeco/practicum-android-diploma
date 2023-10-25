package ru.practicum.android.diploma.feature.favourite.domain.usecase


import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.feature.favourite.domain.repository.FavoriteRepository

class GetFavoriteIdsUseCase(private val favoriteRepository: FavoriteRepository) {
    fun getFavoriteIds(): Flow<List<String>> = favoriteRepository.getAllVacancyIds()
}