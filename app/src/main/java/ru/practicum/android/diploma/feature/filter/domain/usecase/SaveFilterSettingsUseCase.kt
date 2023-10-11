package ru.practicum.android.diploma.feature.filter.domain.usecase

import ru.practicum.android.diploma.feature.filter.domain.api.FilterSettingsRepository
import ru.practicum.android.diploma.feature.filter.domain.model.FilterSettings

/**
 * сохраняет значения полей объекта FilterSettings в SharedPreferences.
 */
class SaveFilterSettingsUseCase(private val filterSettingsRepository: FilterSettingsRepository) {
    operator fun invoke(filterSettings: FilterSettings) {
        filterSettingsRepository.saveFilterSettings(filterSettings)
    }
}