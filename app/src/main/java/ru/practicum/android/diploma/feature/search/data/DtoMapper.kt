package ru.practicum.android.diploma.feature.search.data

import ru.practicum.android.diploma.feature.search.data.dtomodels.AreaDto
import ru.practicum.android.diploma.feature.search.data.dtomodels.BillingTypeDto
import ru.practicum.android.diploma.feature.search.data.dtomodels.ContactsDto
import ru.practicum.android.diploma.feature.search.data.dtomodels.EmployerDto
import ru.practicum.android.diploma.feature.search.data.dtomodels.EmploymentDto
import ru.practicum.android.diploma.feature.search.data.dtomodels.ExperienceDto
import ru.practicum.android.diploma.feature.search.data.dtomodels.KeySkillDto
import ru.practicum.android.diploma.feature.search.data.dtomodels.LogoUrlsDto
import ru.practicum.android.diploma.feature.search.data.dtomodels.PhoneDto
import ru.practicum.android.diploma.feature.search.data.dtomodels.ProfessionalRoleDto
import ru.practicum.android.diploma.feature.search.data.dtomodels.SalaryDto
import ru.practicum.android.diploma.feature.search.data.dtomodels.ScheduleDto
import ru.practicum.android.diploma.feature.search.data.dtomodels.TypeDto
import ru.practicum.android.diploma.feature.search.data.dtomodels.VacancyDtoFull
import ru.practicum.android.diploma.feature.search.data.dtomodels.VacancyDtoShort
import ru.practicum.android.diploma.feature.search.data.network.VacanciesDtoResponse
import ru.practicum.android.diploma.feature.search.data.network.VacancyDtoResponse
import ru.practicum.android.diploma.feature.search.domain.VacanciesResponse
import ru.practicum.android.diploma.feature.search.domain.VacancyResponse
import ru.practicum.android.diploma.feature.search.domain.models.Area
import ru.practicum.android.diploma.feature.search.domain.models.BillingType
import ru.practicum.android.diploma.feature.search.domain.models.Contacts
import ru.practicum.android.diploma.feature.search.domain.models.Employer
import ru.practicum.android.diploma.feature.search.domain.models.Employment
import ru.practicum.android.diploma.feature.search.domain.models.Experience
import ru.practicum.android.diploma.feature.search.domain.models.KeySkill
import ru.practicum.android.diploma.feature.search.domain.models.LogoUrls
import ru.practicum.android.diploma.feature.search.domain.models.Phone
import ru.practicum.android.diploma.feature.search.domain.models.ProfessionalRole
import ru.practicum.android.diploma.feature.search.domain.models.Salary
import ru.practicum.android.diploma.feature.search.domain.models.Schedule
import ru.practicum.android.diploma.feature.search.domain.models.Type
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
        acceptHandicapped = this.acceptHandicapped,
        acceptIncompleteResumes = this.acceptIncompleteResumes,
        acceptKids = this.acceptKids,
        acceptTemporary = this.acceptTemporary,
        address = this.address,
        allowMessages = this.allowMessages,
        alternateUrl = this.alternateUrl,
        applyAlternateUrl = this.applyAlternateUrl,
        archived = this.archived,
        area = this.area?.toArea(),
        billingType = this.billingType?.toBillingType(),
        brandedDescription = this.brandedDescription,
        code = this.code,
        contacts = this.contacts?.toContacts(),
        createdAt = this.createdAt,
        department = this.department,
        description = this.description,
        driverLicenseTypes = this.driverLicenseTypes,
        employer = this.employer?.toEmployer(),
        employment = this.employment?.toEmployment(),
        experience = this.experience?.toExperience(),
        hasTest = this.hasTest,
        hidden = this.hidden,
        id = this.id,
        initialCreatedAt = this.initialCreatedAt,
        insiderInterview = this.insiderInterview,
        keySkills = this.keySkills?.map { it.toKeySkill() },
        languages = this.languages,
        name = this.name,
        negotiationsUrl = this.negotiationsUrl,
        premium = this.premium,
        professionalRoles = this.professionalRoles?.map { it.toProfessionalRole() },
        publishedAt = this.publishedAt,
        quickResponsesAllowed = this.quickResponsesAllowed,
        relations = this.relations,
        responseLetterRequired = this.responseLetterRequired,
        responseUrl = this.responseUrl,
        salary = this.salary?.toSalary(),
        schedule = this.schedule?.toSchedule(),
        specializations = this.specializations,
        suitableResumesUrl = this.suitableResumesUrl,
        test = this.test,
        type = this.type?.toType(),
        vacancyConstructorTemplate = this.vacancyConstructorTemplate,
        workingDays = this.workingDays,
        workingTimeIntervals = this.workingTimeIntervals,
        workingTimeModes = this.workingTimeModes
    )
}

fun TypeDto.toType(): Type {
    return Type(
        id = this.id,
        name = this.name
    )
}

fun ScheduleDto.toSchedule(): Schedule {
    return Schedule(
        id = this.id,
        name = this.name
    )
}

fun ProfessionalRoleDto.toProfessionalRole(): ProfessionalRole {
    return ProfessionalRole(
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

fun BillingTypeDto.toBillingType(): BillingType {
    return BillingType(
        id = this.id,
        name = this.name
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