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

    //todo удалить все три свойства, когда будет добавлена модель Vacancy
    private val email = "support@yandex.ru"
    private val phoneNumber = "78002509639"
    private val vacancy = "Вакансия"

    fun onContactEmailClicked() {
        emailUseCase.invoke(email)
    }

    fun onContactPhoneClicked() {
        callUseCase.invoke(phoneNumber)
    }

    fun onShareVacancyClicked() {
        sharingUseCase.invoke(vacancy)
    }
}