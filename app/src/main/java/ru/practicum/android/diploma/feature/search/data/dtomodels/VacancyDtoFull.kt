package ru.practicum.android.diploma.feature.search.data.dtomodels

import com.google.gson.annotations.SerializedName


data class VacancyDtoFull(
    @SerializedName("apply_alternate_url")
    val applyAlternateUrl: String?,
    val area: AreaDto?,
    val contacts: ContactsDto?,
    val description: String?,
    val employer: EmployerDto?,
    val employment: EmploymentDto?,
    val experience: ExperienceDto?,
    val id: String,
    @SerializedName("key_skills")
    val keySkills: List<KeySkillDto>?,
    val name: String?,
    val salary: SalaryDto?,
    val schedule: ScheduleDto?
)