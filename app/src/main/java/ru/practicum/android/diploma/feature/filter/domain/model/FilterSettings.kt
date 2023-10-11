package ru.practicum.android.diploma.feature.filter.domain.model

data class FilterSettings(
    val regionId: String,
    val industryId: String,
    val expectedSalary: Int,
    val notShowWithoutSalary: Boolean
)
