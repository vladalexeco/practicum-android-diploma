package ru.practicum.android.diploma.core.util

import ru.practicum.android.diploma.feature.filter.domain.model.AreaPlain
import ru.practicum.android.diploma.feature.filter.domain.model.Country
import ru.practicum.android.diploma.feature.filter.domain.model.IndustryPlain

/** Передатчик Id вакансии
 * Например, по клику на вакансию в поиске мы вызываем DataTransmitter.postId(vacancy.id) и
 * переходим на другой фрагмент.
 * Там мы либо в самом фрагменте, либо во ViewModel делаем примерно следующее:
 * val id = DataTransmitter.getId()
 * И потом делаем с этим Id что нужно.
 */

object DataTransmitter {

    private var currentIndustryPlain: IndustryPlain? = null
    private var currentCountry: Country? = null
    private var currentAreaPlain: AreaPlain? = null

    fun postAreaPlain(areaPlain: AreaPlain?) {
        currentAreaPlain = areaPlain
    }

    fun getAreaPlain(): AreaPlain? {
        return currentAreaPlain
    }

    fun postCountry(country: Country?) {
        currentCountry = country
    }

    fun getCountry(): Country? {
        return currentCountry
    }

    fun postIndustryPlain(industryPlain: IndustryPlain?) {
        currentIndustryPlain = industryPlain
    }

    fun getIndustryPlain(): IndustryPlain? {
        return currentIndustryPlain
    }
}