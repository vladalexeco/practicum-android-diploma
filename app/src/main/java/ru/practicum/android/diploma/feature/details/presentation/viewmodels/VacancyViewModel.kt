package ru.practicum.android.diploma.feature.details.presentation.viewmodels

import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.feature.details.domain.usecases.MakeCallUseCase
import ru.practicum.android.diploma.feature.details.domain.usecases.SendEmailUseCase
import ru.practicum.android.diploma.feature.details.domain.usecases.ShareVacancyUseCase

class VacancyViewModel(
    private val emailUseCase: SendEmailUseCase,
    private val callUseCase: MakeCallUseCase,
    private val sharingUseCase: ShareVacancyUseCase
) : ViewModel() {

    private val email = "chernov.i.u@gmail.com"
    private val phoneNumber = "79092831220"
    private val vacancy = "Вакансия"

    fun onContactEmailClicked() {
        emailUseCase.invoke(email)
    }

    fun onContactPhoneClicked() {
        callUseCase.invoke(phoneNumber)
    }

    //todo implement as required per TA when model is implemented
    fun onShareVacancyClicked() {
        sharingUseCase.invoke(vacancy)
    }
}