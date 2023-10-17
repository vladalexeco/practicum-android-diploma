package ru.practicum.android.diploma.core.util

import android.util.Log
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

    private var vacancyId: String = ""

    fun postAreaPlain(areaPlain: AreaPlain?) {
        currentAreaPlain = areaPlain
        Log.d("!@#", "${object{}.javaClass.enclosingMethod?.name} $currentAreaPlain")
    }

    fun getAreaPlain(): AreaPlain? {
        Log.d("!@#", "${object{}.javaClass.enclosingMethod?.name} $currentAreaPlain")
        return currentAreaPlain
    }

    fun postCountry(country: Country?) {
        currentCountry = country
        Log.d("!@#", "${object{}.javaClass.enclosingMethod?.name} $currentCountry")
    }

    fun getCountry(): Country? {
        Log.d("!@#", "${object{}.javaClass.enclosingMethod?.name} $currentCountry")
        return currentCountry
    }

    fun postIndustryPlain(industryPlain: IndustryPlain?) {
        currentIndustryPlain = industryPlain
        Log.d("!@#", "${object{}.javaClass.enclosingMethod?.name} $currentIndustryPlain")
    }

    fun getIndustryPlain(): IndustryPlain? {
        Log.d("!@#", "${object{}.javaClass.enclosingMethod?.name} $currentIndustryPlain")
        return currentIndustryPlain
    }

    fun postId(id: String) {
        vacancyId = id
    }

    fun getId(): String {
        return vacancyId
    }
}