package ru.practicum.android.diploma.feature.filter.domain.usecase

import ru.practicum.android.diploma.feature.filter.domain.api.FilterSettingsRepository

/**
 * Сбрасывает все настройки в SharedPreferences до начальных состояний.
 * - параметр country становится равен "null"
 * - параметр areaPlain становится равен "null"
 * - параметр industryPlain становится равен "null"
 * - параметр expectedSalary становится равен -1
 * - параметр notShowWithoutSalary становится равен false
 */
class ClearFilterSettingsUseCase(private val filterSettingsRepository: FilterSettingsRepository) {
    operator fun invoke() {
        filterSettingsRepository.clearFilterSettings()
    }
}