package ru.practicum.android.diploma.feature.search.domain.models

data class VacancyFull(
    val applyAlternateUrl: String?,
    val area: Area?,
    val contacts: Contacts?,
    val description: String?,
    val employer: Employer?,
    val employment: Employment?,
    val experience: Experience?,
    val id: String?,
    val keySkills: List<KeySkill>?,
    val name: String?,
    val salary: Salary?,
    val schedule: Schedule?
)