package ru.practicum.android.diploma.feature.filter.data.network.dto.model

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.feature.filter.domain.model.Area

data class AreaDto(
    val id: String,
    @SerializedName("parent_id") val parentId: String?,
    val name: String,
    val areas: List<AreaDto>
)

fun AreaDto.mapToArea(): Area {
    val mappedIndustries: List<Area> = areas.map { it.mapToArea() }
    return Area(id, parentId, name, mappedIndustries)
}