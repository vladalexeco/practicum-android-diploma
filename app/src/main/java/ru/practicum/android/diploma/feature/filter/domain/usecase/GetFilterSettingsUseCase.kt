package ru.practicum.android.diploma.feature.filter.domain.usecase

import ru.practicum.android.diploma.feature.filter.domain.api.FilterSettingsRepository
import ru.practicum.android.diploma.feature.filter.domain.model.FilterSettings

/**
 * Возвращает объект FilterSettings, который содержит четыре поля:
 * - regionId - идентификатор региона (String). Если объект SharedPreferences на данный момент
 * не существует, то возвращает ""
 * - industryId - индентификатор отрасли (String). Если объект SharedPreferences на данный момент не
 * существует, то возвращает ""
 * - expectedSalary - ожидаемая заработная плата (Int). Если объект SharedPreferences на данный момент не
 * существует, то возвращает 0
 * - notShowWithoutSalary - значение, указывающее, нужно ли проводить поиск среди тех вакансий,
 * где отсутствует указание заработанной платы (Boolean). Если объект SharedPreferences на данный момент не
 * существует, то возвращает false
 */
class GetFilterSettingsUseCase(private val filterSettingsRepository: FilterSettingsRepository) {
    operator fun invoke(): FilterSettings {
        return filterSettingsRepository.getFilterSettings()
    }
}