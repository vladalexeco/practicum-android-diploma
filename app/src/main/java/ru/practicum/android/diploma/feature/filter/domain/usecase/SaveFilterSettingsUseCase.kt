package ru.practicum.android.diploma.feature.filter.domain.usecase

import ru.practicum.android.diploma.feature.filter.domain.api.FilterSettingsRepository
import ru.practicum.android.diploma.feature.filter.domain.model.FilterSettings

/**
 * сохраняет значения полей объекта FilterSettings в SharedPreferences. Поля объекта FilterSettings:
 * - regionId - идентификатор региона (String)
 * - industryId - индентификатор отрасли (String)
 * - expectedSalary - ожидаемая заработная плата (Int)
 * - notShowWithoutSalary - значение, указывающее, нужно ли проводить поиск среди тех вакансий,
 * где отсутствует указание заработанной платы (Boolean)
 */
class SaveFilterSettingsUseCase(private val filterSettingsRepository: FilterSettingsRepository) {
    operator fun invoke(filterSettings: FilterSettings) {
        filterSettingsRepository.saveFilterSettings(filterSettings)
    }
}