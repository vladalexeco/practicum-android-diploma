package ru.practicum.android.diploma.feature.details.domain.usecases

import ru.practicum.android.diploma.feature.details.domain.api.ExternalNavigator

class ShareVacancyUseCase(private val externalNavigator: ExternalNavigator) {
    operator fun invoke(vacancy: String) { externalNavigator.shareVacancy(vacancy) }
}