package com.usunin1994.headhunterapi.data.dtomodels

data class VacancyDtoShort(
    val id: String,
    val area: AreaDto,
    val employer: EmployerDto,
    val name: String,
    val salary: SalaryDto,
)
