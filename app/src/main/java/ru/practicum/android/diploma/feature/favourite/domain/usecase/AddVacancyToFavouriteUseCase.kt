package ru.practicum.android.diploma.feature.favourite.domain.usecase

import ru.practicum.android.diploma.feature.favourite.domain.repository.FavoriteRepository
import ru.practicum.android.diploma.feature.search.domain.models.VacancyFull


class AddVacancyToFavouriteUseCase(private val favoriteRepository: FavoriteRepository) {
    suspend operator fun invoke(vacancyFull: VacancyFull) {
        favoriteRepository.insertVacancy(vacancyFull)
    }
}