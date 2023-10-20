package ru.practicum.android.diploma.feature.filter.presentation.states

import ru.practicum.android.diploma.feature.filter.domain.model.Area

sealed interface AreasState {
    class Error(val errorText: String, val drawableId: Int): AreasState
    class DisplayAreas(val areas: List<Area>): AreasState
}