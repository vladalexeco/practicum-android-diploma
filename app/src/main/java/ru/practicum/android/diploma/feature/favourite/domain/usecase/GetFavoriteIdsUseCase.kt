package ru.practicum.android.diploma.feature.favourite.domain.usecase


import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.feature.favourite.domain.repository.FavoriteRepository

class GetFavoriteIdsUseCase(private val favoriteRepository: FavoriteRepository) {
    operator fun invoke(): Flow<List<String>>{
        return favoriteRepository.getAllVacancyIds()
    }
}