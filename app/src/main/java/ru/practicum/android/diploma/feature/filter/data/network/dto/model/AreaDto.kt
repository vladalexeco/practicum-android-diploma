package ru.practicum.android.diploma.feature.filter.data.network.dto.model

import ru.practicum.android.diploma.feature.filter.domain.model.Area

data class AreaDto(
    val id: String,
    val parent_id: String?,
    val name: String,
    val areas: List<AreaDto>
)

fun AreaDto.mapToArea(): Area {
    val mappedIndustries: List<Area> = areas.map { it.mapToArea() }
    return Area(id, parent_id, name, mappedIndustries)
}