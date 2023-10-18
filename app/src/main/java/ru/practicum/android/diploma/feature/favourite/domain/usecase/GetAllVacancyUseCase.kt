package ru.practicum.android.diploma.feature.favourite.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.feature.favourite.data.model.VacancyFullEntity
import ru.practicum.android.diploma.feature.favourite.domain.repository.FavoriteRepository
import ru.practicum.android.diploma.feature.search.domain.models.VacancyFull


class GetAllVacancyUseCase(private val favoriteRepository: FavoriteRepository) {

    fun getAllVacancy(): Flow<PagingData<VacancyFull>> {
        return favoriteRepository.getAllVacancy()
    }
}
