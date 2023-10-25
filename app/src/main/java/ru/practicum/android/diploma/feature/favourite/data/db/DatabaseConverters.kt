package ru.practicum.android.diploma.feature.favourite.data.db

import androidx.room.TypeConverter
import ru.practicum.android.diploma.feature.search.domain.models.Area
import ru.practicum.android.diploma.feature.search.domain.models.BillingType
import ru.practicum.android.diploma.feature.search.domain.models.Contacts
import ru.practicum.android.diploma.feature.search.domain.models.Employer
import ru.practicum.android.diploma.feature.search.domain.models.Employment
import ru.practicum.android.diploma.feature.search.domain.models.Experience
import ru.practicum.android.diploma.feature.search.domain.models.KeySkill
import ru.practicum.android.diploma.feature.search.domain.models.ProfessionalRole
import ru.practicum.android.diploma.feature.search.domain.models.Salary
import ru.practicum.android.diploma.feature.search.domain.models.Schedule
import ru.practicum.android.diploma.feature.search.domain.models.Type

class DatabaseConverters {
    @TypeConverter
    fun fromArea(value: Area) = JsonConverter.itemToJson(value)

    @TypeConverter
    fun toArea(value: String) = JsonConverter.jsonToItem<Area>(value)

    @TypeConverter
    fun fromEmployer(value: Employer) = JsonConverter.itemToJson(value)

    @TypeConverter
    fun toEmployer(value: String) = JsonConverter.jsonToItem<Employer>(value)

    @TypeConverter
    fun fromEmployment(value: Employment) = JsonConverter.itemToJson(value)

    @TypeConverter
    fun toEmployment(value: String) = JsonConverter.jsonToItem<Employment>(value)

    @TypeConverter
    fun fromExperience(value: Experience) = JsonConverter.itemToJson(value)

    @TypeConverter
    fun toExperience(value: String) = JsonConverter.jsonToItem<Experience>(value)

    @TypeConverter
    fun fromSalary(value: Salary) = JsonConverter.itemToJson(value)

    @TypeConverter
    fun toSalary(value: String) = JsonConverter.jsonToItem<Salary>(value)

    @TypeConverter
    fun fromSchedule(value: Schedule) = JsonConverter.itemToJson(value)

    @TypeConverter
    fun toSchedule(value: String) = JsonConverter.jsonToItem<Schedule>(value)

    @TypeConverter
    fun fromType(value: Type) = JsonConverter.itemToJson(value)

    @TypeConverter
    fun toType(value: String) = JsonConverter.jsonToItem<Type>(value)

    @TypeConverter
    fun fromBillingType(value: BillingType) = JsonConverter.itemToJson(value)

    @TypeConverter
    fun toBillingType(value: String) = JsonConverter.jsonToItem<BillingType>(value)

    @TypeConverter
    fun fromContacts(value: Contacts) = JsonConverter.itemToJson(value)

    @TypeConverter
    fun toContacts(value: String) = JsonConverter.jsonToItem<Contacts>(value)

    @TypeConverter
    fun fromAny(value: Any) = JsonConverter.itemToJson(value)

    @TypeConverter
    fun toAny(value: Any) = JsonConverter.jsonToItem<Any>(value.toString())

    @TypeConverter
    fun fromAnyList(value: List<Any>?): String? {
        return JsonConverter.itemListToJson(value)
    }

    // Конвертация JSON строки обратно в список
    @TypeConverter
    fun toAnyList(value: String?): List<Any>? {
        return value?.let { JsonConverter.jsonToItemList(it) }
    }

    @TypeConverter
    fun fromKeySkillsList(value: List<KeySkill>) = JsonConverter.itemListToJson(value)

    @TypeConverter
    fun toKeySkillsList(value: String) = JsonConverter.jsonToItemList<KeySkill>(value)

    @TypeConverter
    fun fromProfessionalRoleList(value: List<ProfessionalRole>?): String? {
        return JsonConverter.itemListToJson(value)
    }

    // Конвертация JSON строки обратно в список
    @TypeConverter
    fun toProfessionalRoleList(value: String?): List<ProfessionalRole>? {
        return value?.let { JsonConverter.jsonToItemList(it) }
    }
}