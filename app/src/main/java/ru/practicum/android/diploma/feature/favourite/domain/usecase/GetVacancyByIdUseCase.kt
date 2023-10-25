package ru.practicum.android.diploma.feature.favourite.domain.usecase

import ru.practicum.android.diploma.feature.favourite.domain.repository.FavoriteRepository
import ru.practicum.android.diploma.feature.search.domain.models.VacancyFull

class GetVacancyByIdUseCase(private val favoriteRepository: FavoriteRepository) {
    suspend fun getVacancyById(vacancyId: String): VacancyFull? =
        favoriteRepository.getVacancyById(vacancyId)
}