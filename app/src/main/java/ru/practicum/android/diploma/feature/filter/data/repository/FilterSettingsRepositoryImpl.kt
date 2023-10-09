package ru.practicum.android.diploma.feature.filter.data.repository

import android.content.SharedPreferences
import ru.practicum.android.diploma.core.util.EXPECTED_SALARY_KEY
import ru.practicum.android.diploma.core.util.INDUSTRY_ID_KEY
import ru.practicum.android.diploma.core.util.NOT_SHOW_WITHOUT_SALARY_KEY
import ru.practicum.android.diploma.core.util.REGION_ID_KEY
import ru.practicum.android.diploma.feature.filter.domain.api.FilterSettingsRepository
import ru.practicum.android.diploma.feature.filter.domain.model.FilterSettings

class FilterSettingsRepositoryImpl(
    private val sharedPreferences: SharedPreferences?
) : FilterSettingsRepository  {
    override fun getFilterSettings(): FilterSettings {

        val regionId = sharedPreferences?.getString(REGION_ID_KEY, null) ?: ""
        val industryId = sharedPreferences?.getString(INDUSTRY_ID_KEY, null) ?: ""
        val expectedSalary = sharedPreferences?.getInt(EXPECTED_SALARY_KEY, 0) ?: 0
        val notShowWithoutSalary = sharedPreferences?.getBoolean(NOT_SHOW_WITHOUT_SALARY_KEY, false) ?: false

        return FilterSettings(regionId, industryId, expectedSalary, notShowWithoutSalary)
    }

    override fun saveFilterSettings(filterSettings: FilterSettings) {
        sharedPreferences?.edit()
            ?.putString(REGION_ID_KEY, filterSettings.regionId)
            ?.putString(INDUSTRY_ID_KEY, filterSettings.industryId)
            ?.putInt(EXPECTED_SALARY_KEY, filterSettings.expectedSalary)
            ?.putBoolean(NOT_SHOW_WITHOUT_SALARY_KEY, filterSettings.notShowWithoutSalary)
            ?.apply()
    }

    override fun clearFilterSettings() {
        sharedPreferences?.edit()
            ?.putString(REGION_ID_KEY, "")
            ?.putString(INDUSTRY_ID_KEY, "")
            ?.putInt(EXPECTED_SALARY_KEY, 0)
            ?.putBoolean(NOT_SHOW_WITHOUT_SALARY_KEY, false)
            ?.apply()
    }
}