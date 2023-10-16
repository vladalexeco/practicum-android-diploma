package ru.practicum.android.diploma.feature.filter.data.network.dto.model

import ru.practicum.android.diploma.feature.filter.domain.model.AreaPlain

data class AreaPlainDto(
    val id: String,
    val name: String,
    val parent_id: String? = null
)

fun AreaPlainDto.mapToAreaPlain(): AreaPlain = AreaPlain(id = id, name = name, parent_id = parent_id)