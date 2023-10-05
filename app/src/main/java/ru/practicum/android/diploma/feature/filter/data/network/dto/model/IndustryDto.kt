package ru.practicum.android.diploma.feature.filter.data.network.dto.model

import ru.practicum.android.diploma.feature.filter.domain.model.Industry

data class IndustryDto(
    val id: String,
    val name: String,
    val industries: List<IndustryDto>? = null
)

fun IndustryDto.mapToIndustry(): Industry {
    val mappedIndustries: List<Industry>? = industries?.map { it.mapToIndustry() }
    return Industry(id, name, mappedIndustries)
}


