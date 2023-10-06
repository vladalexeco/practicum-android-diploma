package ru.practicum.android.diploma.feature.filter.data.network.dto.model

import ru.practicum.android.diploma.feature.filter.domain.model.Country

data class CountryDto(
    val id: String,
    val name: String,
    val url: String
)

fun CountryDto.mapToCountry(): Country = Country(id, name, url)
