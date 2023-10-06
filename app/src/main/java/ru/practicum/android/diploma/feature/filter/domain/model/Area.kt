package ru.practicum.android.diploma.feature.filter.domain.model

data class Area(
    val id: String,
    val parent_id: String?,
    val name: String,
    val areas: List<Area>
)
