package ru.practicum.android.diploma.feature.details.domain.usecases

import ru.practicum.android.diploma.feature.details.domain.api.ExternalNavigator

class MakeCallUseCase(private val externalNavigator: ExternalNavigator) {

    private fun makeCall(phoneNumber: String) {
        externalNavigator.makeCall(phoneNumber)
    }

    operator fun invoke(phoneNumber: String) {
        makeCall(phoneNumber)
    }
}