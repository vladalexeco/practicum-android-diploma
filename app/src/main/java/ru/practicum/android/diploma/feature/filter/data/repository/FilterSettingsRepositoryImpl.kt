package ru.practicum.android.diploma.feature.filter.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.practicum.android.diploma.core.util.EXPECTED_SALARY_KEY
import ru.practicum.android.diploma.core.util.INDUSTRY_KEY
import ru.practicum.android.diploma.core.util.NOT_SHOW_WITHOUT_SALARY_KEY
import ru.practicum.android.diploma.core.util.AREA_KEY
import ru.practicum.android.diploma.core.util.COUNTRY_KEY
import ru.practicum.android.diploma.feature.filter.domain.api.FilterSettingsRepository
import ru.practicum.android.diploma.feature.filter.domain.model.AreaPlain
import ru.practicum.android.diploma.feature.filter.domain.model.Country
import ru.practicum.android.diploma.feature.filter.domain.model.FilterSettings
import ru.practicum.android.diploma.feature.filter.domain.model.IndustryPlain

class FilterSettingsRepositoryImpl(
    private val sharedPreferences: SharedPreferences?
) : FilterSettingsRepository  {

    override fun getFilterSettings(): FilterSettings {
        val gson = Gson()
        val countryRawString = sharedPreferences?.getString(COUNTRY_KEY, null) ?: "null"
        val areaPlainRawString = sharedPreferences?.getString(AREA_KEY, null) ?: "null"
        val industryPlainRawString = sharedPreferences?.getString(INDUSTRY_KEY, null) ?: "null"
        val expectedSalary = sharedPreferences?.getInt(EXPECTED_SALARY_KEY, -1) ?: -1
        val notShowWithoutSalary = sharedPreferences?.getBoolean(NOT_SHOW_WITHOUT_SALARY_KEY, false) ?: false
        val country: Country? = gson.fromJson(countryRawString, Country::class.java)
        val area: AreaPlain? = gson.fromJson(areaPlainRawString, AreaPlain::class.java)
        val industry: IndustryPlain? = gson.fromJson(industryPlainRawString, IndustryPlain::class.java)
        return FilterSettings(
            country = country,
            areaPlain = area,
            industryPlain = industry,
            expectedSalary = expectedSalary,
            notShowWithoutSalary = notShowWithoutSalary
        )
    }

    override fun saveFilterSettings(filterSettings: FilterSettings) {
        val gson = Gson()
        val countryGson: String = gson.toJson(filterSettings.country)
        val areaPlainGson: String = gson.toJson(filterSettings.areaPlain)
        val industryPlainGson: String = gson.toJson(filterSettings.industryPlain)
        sharedPreferences?.edit()
            ?.putString(COUNTRY_KEY, countryGson)
            ?.putString(AREA_KEY, areaPlainGson)
            ?.putString(INDUSTRY_KEY, industryPlainGson)
            ?.putInt(EXPECTED_SALARY_KEY, filterSettings.expectedSalary)
            ?.putBoolean(NOT_SHOW_WITHOUT_SALARY_KEY, filterSettings.notShowWithoutSalary)
            ?.apply()
    }

    override fun clearFilterSettings() {
        sharedPreferences?.edit()
            ?.putString(COUNTRY_KEY, "null")
            ?.putString(AREA_KEY, "null")
            ?.putString(INDUSTRY_KEY, "null")
            ?.putInt(EXPECTED_SALARY_KEY, -1)
            ?.putBoolean(NOT_SHOW_WITHOUT_SALARY_KEY, false)
            ?.apply()
    }
}