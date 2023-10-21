package com.usunin1994.headhunterapi.data.dtomodels

import ru.practicum.android.diploma.feature.search.data.dtomodels.AreaDto
import ru.practicum.android.diploma.feature.search.data.dtomodels.EmployerDto
import ru.practicum.android.diploma.feature.search.data.dtomodels.SalaryDto

data class VacancyDtoShort(
    val id: String,
    val area: AreaDto,
    val employer: EmployerDto,
    val name: String,
    val salary: SalaryDto,
)
