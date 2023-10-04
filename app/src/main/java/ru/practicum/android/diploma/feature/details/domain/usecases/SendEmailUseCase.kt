package ru.practicum.android.diploma.feature.details.domain.usecases

import ru.practicum.android.diploma.feature.details.domain.api.ExternalNavigator

class SendEmailUseCase(private val externalNavigator: ExternalNavigator) {

    operator fun invoke(email: String) {
        externalNavigator.sendEmail(email)
    }
}