package ru.practicum.android.diploma.feature.filter.presentation.viewmodels

import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.feature.filter.domain.model.FilterSettings
import ru.practicum.android.diploma.feature.filter.domain.usecase.ClearFilterSettingsUseCase
import ru.practicum.android.diploma.feature.filter.domain.usecase.GetFilterSettingsUseCase
import ru.practicum.android.diploma.feature.filter.domain.usecase.SaveFilterSettingsUseCase

class SettingsFiltersViewModel(
    private val getFilterSettingsUseCase: GetFilterSettingsUseCase,
    private val saveFilterSettingsUseCase: SaveFilterSettingsUseCase,
    private val clearFilterSettingsUseCase: ClearFilterSettingsUseCase
) : ViewModel() {

    fun getFilterSettings(): FilterSettings {
        return getFilterSettingsUseCase()
    }

    fun saveFilterSettings(filterSettings: FilterSettings) {
        saveFilterSettingsUseCase(filterSettings)
    }

    fun clearFilterSettings() {
        clearFilterSettingsUseCase()
    }

}