package ru.practicum.android.diploma.feature.filter.domain.model

data class FilterSettings(
    val country: Country?,
    val areaPlain: AreaPlain?,
    val industryPlain: IndustryPlain?,
    val expectedSalary: Int,
    val notShowWithoutSalary: Boolean
)
