package ru.practicum.android.diploma.feature.filter.domain.usecase

import ru.practicum.android.diploma.feature.filter.domain.api.FilterSettingsRepository

/**
 * Сбрасывает все настройки в SharedPreferences до начальных состояний.
 * - параметр regionId становится равен ""
 * - параметр industryId становится равен ""
 * - параметр expectedSalary становится равен 0
 * - параметр notShowWithoutSalary становится равен false
 */
class ClearFilterSettingsUseCase(private val filterSettingsRepository: FilterSettingsRepository) {
    operator fun invoke() {
        filterSettingsRepository.clearFilterSettings()
    }
}