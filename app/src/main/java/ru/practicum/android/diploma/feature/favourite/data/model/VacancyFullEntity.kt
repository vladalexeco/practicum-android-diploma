package ru.practicum.android.diploma.feature.favourite.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.usunin1994.headhunterapi.data.dtomodels.EmployerDto
import com.usunin1994.headhunterapi.data.dtomodels.LogoUrlsDto
import ru.practicum.android.diploma.feature.search.data.toLogoUrls
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
    @PrimaryKey(autoGenerate = true)
    val vacancyId: Int,
    val acceptHandicapped: Boolean?,
    val acceptIncompleteResumes: Boolean?,
    val acceptKids: Boolean?,
    val acceptTemporary: Boolean?,
    val address: Any?,
    val allowMessages: Boolean?,
    val alternateUrl: String?,
    val applyAlternateUrl: String?,
    val archived: Boolean?,
    val area: Area?,
    val billingType: BillingType?,
    val brandedDescription: Any?,
    val code: Any?,
    val contacts: Contacts?,
    val createdAt: String?,
    val department: Any?,
    val description: String?, // Содержит xml!!!
    val driverLicenseTypes: List<Any>?,
    val employer: Employer?,
    val employment: Employment?,
    val experience: Experience?,
    val hasTest: Boolean?,
    val hidden: Boolean?,
    val id: String?,
    val initialCreatedAt: String?,
    val insiderInterview: Any?,
    val keySkills: List<KeySkill>?,
    val languages: List<Any>?,
    val name: String?,
    val negotiationsUrl: Any?,
    val premium: Boolean?,
    val professionalRoles: List<ProfessionalRole>?,
    val publishedAt: String?,
    val quickResponsesAllowed: Boolean?,
    val relations: List<Any>?,
    val responseLetterRequired: Boolean?,
    val responseUrl: Any?,
    val salary: Salary?,
    val schedule: Schedule?,
    val specializations: List<Any>?,
    val suitableResumesUrl: Any?,
    val test: Any?,
    val type: Type?,
    val vacancyConstructorTemplate: Any?,
    val workingDays: List<Any>?,
    val workingTimeIntervals: List<Any>?,
    val workingTimeModes: List<Any>?
)


fun VacancyFull.toVacancyFullEntity(): VacancyFullEntity {
    return VacancyFullEntity(
        vacancyId = 0, // Значение по умолчанию для автоматической генерации id
        acceptHandicapped = this.acceptHandicapped,
        acceptIncompleteResumes = this.acceptIncompleteResumes,
        acceptKids = this.acceptKids,
        acceptTemporary = this.acceptTemporary,
        address = this.address,
        allowMessages = this.allowMessages,
        alternateUrl = this.alternateUrl,
        applyAlternateUrl = this.applyAlternateUrl,
        archived = this.archived,
        area = this.area,
        billingType = this.billingType,
        brandedDescription = this.brandedDescription,
        code = this.code,
        contacts = this.contacts,
        createdAt = this.createdAt,
        department = this.department,
        description = this.description,
        driverLicenseTypes = this.driverLicenseTypes,
        employer = this.employer,
        employment = this.employment,
        experience = this.experience,
        hasTest = this.hasTest,
        hidden = this.hidden,
        id = this.id,
        initialCreatedAt = this.initialCreatedAt,
        insiderInterview = this.insiderInterview,
        keySkills = this.keySkills,
        languages = this.languages,
        name = this.name,
        negotiationsUrl = this.negotiationsUrl,
        premium = this.premium,
        professionalRoles = this.professionalRoles,
        publishedAt = this.publishedAt,
        quickResponsesAllowed = this.quickResponsesAllowed,
        relations = this.relations,
        responseLetterRequired = this.responseLetterRequired,
        responseUrl = this.responseUrl,
        salary = this.salary,
        schedule = this.schedule,
        specializations = this.specializations,
        suitableResumesUrl = this.suitableResumesUrl,
        test = this.test,
        type = this.type,
        vacancyConstructorTemplate = this.vacancyConstructorTemplate,
        workingDays = this.workingDays,
        workingTimeIntervals = this.workingTimeIntervals,
        workingTimeModes = this.workingTimeModes
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