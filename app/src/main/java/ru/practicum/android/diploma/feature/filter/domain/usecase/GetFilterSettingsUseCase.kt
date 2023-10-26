package ru.practicum.android.diploma.feature.filter.domain.usecase

import ru.practicum.android.diploma.feature.filter.domain.api.FilterSettingsRepository
import ru.practicum.android.diploma.feature.filter.domain.model.FilterSettings

/**
 * Возвращает объект FilterSettings.
 * Если на момент запроса объект sharedPreferences отсутсвует, то FilterSettings имеет поля:
 * - country = null
 * - areaPlain = null
 * - industryPlain = null
 * - expectedSalary = -1
 * - notShowWithoutSalary = false
 */
class GetFilterSettingsUseCase(private val filterSettingsRepository: FilterSettingsRepository) {
    operator fun invoke(): FilterSettings = filterSettingsRepository.getFilterSettings()
}