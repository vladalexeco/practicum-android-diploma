package ru.practicum.android.diploma.core.util

import ru.practicum.android.diploma.feature.filter.domain.model.Area
import ru.practicum.android.diploma.feature.filter.domain.model.Country
import ru.practicum.android.diploma.feature.filter.domain.model.Industry

/** Передатчик Id вакансии
 * Например, по клику на вакансию в поиске мы вызываем DataTransmitter.postId(vacancy.id) и
 * переходим на другой фрагмент.
 * Там мы либо в самом фрагменте, либо во ViewModel делаем примерно следующее:
 * val id = DataTransmitter.getId()
 * И потом делаем с этим Id что нужно.
 */

object DataTransmitter {

    private var currentIndustry: Industry? = null
    private var currentCountry: Country? = null
    private var currentArea: Area? = null

    private var vacancyId: String = ""
    private var countryId: String = ""

    fun postArea(area: Area?) {
        currentArea = area
    }

    fun getArea(): Area? {
        return currentArea
    }

    fun postCountryId(id: String) {
        countryId = id
    }

    fun getCountryId(): String {
        return countryId
    }

    fun postCountry(country: Country?) {
        currentCountry = country
    }

    fun getCountry(): Country? {
        return currentCountry
    }

    fun postIndustry(industry: Industry?) {
        currentIndustry = industry
    }

    fun getIndustry(): Industry? {
        return currentIndustry
    }

    fun postId(id: String) {
        vacancyId = id
    }

    fun getId(): String {
        return vacancyId
    }
}