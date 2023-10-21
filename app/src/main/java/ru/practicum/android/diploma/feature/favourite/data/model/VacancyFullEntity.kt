package ru.practicum.android.diploma.feature.favourite.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.practicum.android.diploma.feature.search.domain.models.Area

import ru.practicum.android.diploma.feature.search.domain.models.BillingType
import ru.practicum.android.diploma.feature.search.domain.models.Contacts
import ru.practicum.android.diploma.feature.search.domain.models.Employer
import ru.practicum.android.diploma.feature.search.domain.models.Employment
import ru.practicum.android.diploma.feature.search.domain.models.Experience
import ru.practicum.android.diploma.feature.search.domain.models.KeySkill
import ru.practicum.android.diploma.feature.search.domain.models.LogoUrls
import ru.practicum.android.diploma.feature.search.domain.models.ProfessionalRole
import ru.practicum.android.diploma.feature.search.domain.models.Salary
import ru.practicum.android.diploma.feature.search.domain.models.Schedule
import ru.practicum.android.diploma.feature.search.domain.models.Type
import ru.practicum.android.diploma.feature.search.domain.models.VacancyFull
import ru.practicum.android.diploma.feature.search.domain.models.VacancyShort

@Entity(tableName = "vacancy_table")
data class VacancyFullEntity(
    @PrimaryKey
    val id: String, //
    val applyAlternateUrl: String?, //
    val area: Area?, //
    val contacts: Contacts?, //
    val description: String?, // Содержит xml!!!
    val employer: Employer?, //
    val employment: Employment?, //
    val experience: Experience?, //
    val keySkills: List<KeySkill>?, //
    val name: String?, //
    val salary: Salary?, //
    val schedule: Schedule?, //
)

fun VacancyFull.toVacancyFullEntity(): VacancyFullEntity {
    return VacancyFullEntity(
        id = this.id!!,
        applyAlternateUrl = this.applyAlternateUrl,
        area = this.area,
        contacts = this.contacts,
        description = this.description,
        employer = this.employer,
        employment = this.employment,
        experience = this.experience,
        keySkills = this.keySkills,
        name = this.name,
        salary = this.salary,
        schedule = this.schedule,
    )
}

fun VacancyFullEntity.toVacancyFull(): VacancyFull {
    return VacancyFull(
        id = this.id!!,
        applyAlternateUrl = this.applyAlternateUrl,
        area = this.area,
        contacts = this.contacts,
        description = this.description,
        employer = this.employer,
        employment = this.employment,
        experience = this.experience,
        keySkills = this.keySkills,
        name = this.name,
        salary = this.salary,
        schedule = this.schedule,
    )
}




fun VacancyFullEntity.toVacancyShort(): VacancyShort {
    return this.area?.toArea()?.let {
        VacancyShort(
            id = this.id ?: "",
            area = it,
            employer = this.employer?.toEmployer() ,
            name = this.name ?: "",
            salary = this.salary?.toSalaryDto()
        )
    }!!
}

fun VacancyFull.toVacancyShort(): VacancyShort {
    return this.area?.toArea()?.let {
        VacancyShort(
            id = this.id ?: "",
            area = it,
            employer = this.employer?.toEmployer() ,
            name = this.name ?: "",
            salary = this.salary?.toSalaryDto()
        )
    }!!
}

fun Area.toArea(): Area {
    return Area(
        id = this.id,
        name = this.name,
        url = this.url
    )
}


fun Employer.toEmployer(): Employer {
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

fun LogoUrls.toLogoUrls(): LogoUrls {
    return LogoUrls(
        logo240 = this.logo240,
        logo90 = this.logo90,
        original = this.original
    )
}

fun Salary.toSalaryDto(): Salary {
    return Salary(
        from = this.from ?: 0,
        to = this.to ?: 0,
        currency = this.currency ?: "",
        gross = this.gross ?: false
    )
}