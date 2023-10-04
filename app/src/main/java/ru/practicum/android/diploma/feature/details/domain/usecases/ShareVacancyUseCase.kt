package ru.practicum.android.diploma.feature.details.domain.usecases

import ru.practicum.android.diploma.feature.details.domain.api.ExternalNavigator

class ShareVacancyUseCase(private val externalNavigator: ExternalNavigator) {

    private fun shareVacancy(vacancy: String) {
        externalNavigator.shareVacancy(vacancy)
    }

    operator fun invoke(vacancy: String) {
        shareVacancy(vacancy)
    }
}