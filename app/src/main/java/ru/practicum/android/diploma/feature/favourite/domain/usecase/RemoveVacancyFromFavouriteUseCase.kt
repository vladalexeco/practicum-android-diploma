package ru.practicum.android.diploma.feature.favourite.domain.usecase


import ru.practicum.android.diploma.feature.favourite.data.model.VacancyFullEntity
import ru.practicum.android.diploma.feature.favourite.domain.repository.FavoriteRepository
import ru.practicum.android.diploma.feature.search.domain.models.VacancyFull

class RemoveVacancyFromFavouriteUseCase(private val favoriteRepository: FavoriteRepository) {

    suspend fun removeVacancy(vacancyFull: VacancyFull){
        favoriteRepository.deleteVacancy(vacancyFull)
    }
}