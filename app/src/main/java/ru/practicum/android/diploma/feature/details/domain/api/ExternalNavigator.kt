package ru.practicum.android.diploma.feature.details.domain.api

interface ExternalNavigator {
    fun sendEmail(email: String)
    fun makeCall(phoneNumber: String)
    fun shareVacancy(vacancy: String)
}