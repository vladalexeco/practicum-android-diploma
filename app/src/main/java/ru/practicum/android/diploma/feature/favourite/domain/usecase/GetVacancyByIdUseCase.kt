package ru.practicum.android.diploma.feature.favourite.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.coroutineScope
import ru.practicum.android.diploma.feature.favourite.domain.repository.FavoriteRepository
import ru.practicum.android.diploma.feature.search.domain.models.VacancyFull

class GetVacancyByIdUseCase(private val favoriteRepository: FavoriteRepository) {

    suspend fun getVacancyById(vacancyId: String): VacancyFull? {
         return  favoriteRepository.getVacancyById(vacancyId)
    }
}