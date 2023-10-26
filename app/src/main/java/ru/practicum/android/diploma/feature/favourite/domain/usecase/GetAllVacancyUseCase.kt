package ru.practicum.android.diploma.feature.favourite.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.feature.favourite.domain.repository.FavoriteRepository
import ru.practicum.android.diploma.feature.search.domain.models.VacancyFull


class GetAllVacancyUseCase(private val favoriteRepository: FavoriteRepository) {
    operator fun invoke(): Flow<List<VacancyFull>> {
        return favoriteRepository.getAllVacancy()
    }
}
