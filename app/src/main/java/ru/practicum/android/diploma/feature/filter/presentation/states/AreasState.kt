package ru.practicum.android.diploma.feature.filter.presentation.states

import ru.practicum.android.diploma.feature.filter.domain.model.Area

sealed interface AreasState {
    class Error(val errorText: String): AreasState
    class DisplayAreas(val areas: ArrayList<Area>): AreasState
}