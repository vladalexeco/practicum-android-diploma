package ru.practicum.android.diploma.core.util

import ru.practicum.android.diploma.feature.filter.domain.model.AreaPlain
import ru.practicum.android.diploma.feature.filter.domain.model.Country
import ru.practicum.android.diploma.feature.filter.domain.model.IndustryPlain

object DataTransmitter {

    private var currentIndustryPlain: IndustryPlain? = null
    private var currentCountry: Country? = null
    private var currentAreaPlain: AreaPlain? = null

    fun postAreaPlain(areaPlain: AreaPlain?) {
        currentAreaPlain = areaPlain
    }

    fun getAreaPlain(): AreaPlain? = currentAreaPlain

    fun postCountry(country: Country?) {
        currentCountry = country
    }

    fun getCountry(): Country? = currentCountry

    fun postIndustryPlain(industryPlain: IndustryPlain?) {
        currentIndustryPlain = industryPlain
    }

    fun getIndustryPlain(): IndustryPlain? = currentIndustryPlain
}