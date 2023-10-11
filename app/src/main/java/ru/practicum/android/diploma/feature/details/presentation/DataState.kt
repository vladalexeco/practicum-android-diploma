package ru.practicum.android.diploma.feature.details.presentation

import ru.practicum.android.diploma.feature.search.domain.models.VacancyFull

sealed class DataState {
    object Loading : DataState()
    class Failed(val codeResponse: Int) : DataState()
    class DataReceived(val data: VacancyFull) : DataState()
}
