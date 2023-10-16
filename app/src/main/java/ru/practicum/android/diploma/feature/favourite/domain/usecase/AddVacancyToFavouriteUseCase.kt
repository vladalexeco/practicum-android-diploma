package ru.practicum.android.diploma.feature.favourite.domain.usecase

import ru.practicum.android.diploma.feature.favourite.data.model.VacancyFullEntity
import ru.practicum.android.diploma.feature.favourite.domain.repository.FavoriteRepository
import ru.practicum.android.diploma.feature.search.domain.models.VacancyFull


class AddVacancyToFavouriteUseCase(private val favoriteRepository: FavoriteRepository) {

    suspend fun addVacancy(vacancyFull: VacancyFull){
        favoriteRepository.insertVacancy(vacancyFull)
    }
}