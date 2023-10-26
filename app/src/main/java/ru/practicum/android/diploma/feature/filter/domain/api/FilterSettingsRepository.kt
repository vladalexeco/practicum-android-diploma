package ru.practicum.android.diploma.feature.filter.domain.api

import ru.practicum.android.diploma.feature.filter.domain.model.FilterSettings

interface FilterSettingsRepository {
    fun getFilterSettings(): FilterSettings
    fun saveFilterSettings(filterSettings: FilterSettings)
    fun clearFilterSettings()
}