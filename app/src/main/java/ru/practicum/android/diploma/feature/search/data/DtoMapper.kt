package ru.practicum.android.diploma.feature.search.data

import ru.practicum.android.diploma.feature.search.data.dtomodels.AreaDto
import ru.practicum.android.diploma.feature.search.data.dtomodels.ContactsDto
import ru.practicum.android.diploma.feature.search.data.dtomodels.EmployerDto
import ru.practicum.android.diploma.feature.search.data.dtomodels.EmploymentDto
import ru.practicum.android.diploma.feature.search.data.dtomodels.ExperienceDto
import ru.practicum.android.diploma.feature.search.data.dtomodels.KeySkillDto
import ru.practicum.android.diploma.feature.search.data.dtomodels.LogoUrlsDto
import ru.practicum.android.diploma.feature.search.data.dtomodels.PhoneDto
import ru.practicum.android.diploma.feature.search.data.dtomodels.SalaryDto
import ru.practicum.android.diploma.feature.search.data.dtomodels.ScheduleDto
import ru.practicum.android.diploma.feature.search.data.dtomodels.VacancyDtoFull
import ru.practicum.android.diploma.feature.search.data.dtomodels.VacancyDtoShort
import ru.practicum.android.diploma.feature.search.data.network.VacanciesDtoResponse
import ru.practicum.android.diploma.feature.search.data.network.VacancyDtoResponse
import ru.practicum.android.diploma.feature.search.domain.VacanciesResponse
import ru.practicum.android.diploma.feature.search.domain.VacancyResponse
import ru.practicum.android.diploma.feature.search.domain.models.Area
import ru.practicum.android.diploma.feature.search.domain.models.Contacts
import ru.practicum.android.diploma.feature.search.domain.models.Employer
import ru.practicum.android.diploma.feature.search.domain.models.Employment
import ru.practicum.android.diploma.feature.search.domain.models.Experience
import ru.practicum.android.diploma.feature.search.domain.models.KeySkill
import ru.practicum.android.diploma.feature.search.domain.models.LogoUrls
import ru.practicum.android.diploma.feature.search.domain.models.Phone
import ru.practicum.android.diploma.feature.search.domain.models.Salary
import ru.practicum.android.diploma.feature.search.domain.models.Schedule
import ru.practicum.android.diploma.feature.search.domain.models.VacancyFull
import ru.practicum.android.diploma.feature.search.domain.models.VacancyShort

fun VacancyDtoResponse.toVacancyResponse(): VacancyResponse {
    return VacancyResponse(
        vacancy = this.vacancy.toVacancyFull()
    )
}

fun VacanciesDtoResponse.toVacanciesResponse(): VacanciesResponse {
    return VacanciesResponse(
        items = this.items.map { it.toVacancyShort() },
        found = this.found,
        pages = this.pages,
        perPage = this.perPage,
        page = this.page
    )
}

fun VacancyDtoShort.toVacancyShort(): VacancyShort {
    return VacancyShort(
        id = this.id,
        area = this.area.toArea(),
        employer = if (this.employer == null) null else this.employer.toEmployer(),
        name = this.name,
        salary = if (this.salary == null) null else this.salary.toSalary()
    )
}

fun VacancyDtoFull.toVacancyFull(): VacancyFull{
    return VacancyFull(
        applyAlternateUrl = this.applyAlternateUrl,
        area = this.area?.toArea(),
        contacts = this.contacts?.toContacts(),
        description = this.description,
        employer = this.employer?.toEmployer(),
        employment = this.employment?.toEmployment(),
        experience = this.experience?.toExperience(),
        id = this.id,
        keySkills = this.keySkills?.map { it.toKeySkill() },
        name = this.name,
        salary = this.salary?.toSalary(),
        schedule = this.schedule?.toSchedule()
    )
}

fun ScheduleDto.toSchedule(): Schedule {
    return Schedule(
        id = this.id,
        name = this.name
    )
}

fun KeySkillDto.toKeySkill(): KeySkill {
    return KeySkill(
        name = this.name
    )
}

fun ExperienceDto.toExperience(): Experience {
    return Experience(
        id = this.id,
        name = this.name
    )
}

fun EmploymentDto.toEmployment(): Employment {
    return Employment(
        id = this.id,
        name = this.name
    )
}

fun ContactsDto.toContacts(): Contacts {
    return Contacts(
        email = this.email,
        name = this.name,
        phones = this.phones.map { it.toPhone() }
    )
}

fun PhoneDto.toPhone(): Phone {
    return Phone(
        city = this.city,
        comment = this.comment,
        country = this.country,
        formatted = this.formatted,
        number = this.number
    )
}

fun AreaDto.toArea(): Area {
    return Area (
        id = this.id,
        name = this.name,
        url = this.url
    )
}

fun EmployerDto.toEmployer(): Employer {
    return Employer(
        accreditedItEmployer = this.accreditedItEmployer,
        alternateUrl = this.alternateUrl,
        id = this.id,
        logoUrls = if (this.logoUrls == null) null else this.logoUrls.toLogoUrls(),
        name = this.name,
        trusted = this.trusted,
        url = this.url,
        vacanciesUrl = this.vacanciesUrl
    )
}

fun LogoUrlsDto.toLogoUrls(): LogoUrls {
    return LogoUrls(
        logo240 = this.logo240,
        logo90 = this.logo90,
        original = this.original
    )
}

fun SalaryDto.toSalary(): Salary {
    return Salary(
        currency = this.currency,
        from = this.from,
        gross = this.gross,
        to = this.to
    )
}