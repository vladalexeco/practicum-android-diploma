package ru.practicum.android.diploma.feature.filter.presentation.states

import ru.practicum.android.diploma.feature.filter.domain.model.Area
import ru.practicum.android.diploma.feature.filter.domain.model.AreaPlain
import ru.practicum.android.diploma.feature.filter.domain.model.Country
import ru.practicum.android.diploma.feature.filter.domain.model.Industry

sealed class LiveDataResource {
    class AreaStorage(val data: Area) : LiveDataResource()

    class AreasStateStorage(val data: AreasState) : LiveDataResource()

    class CountryStorage(val data: Country) : LiveDataResource()

    class CountryStateStorage(val data: CountriesState) : LiveDataResource()

    class IndustryStorage(val data: Industry) : LiveDataResource()

    class IndustryStateStorage(val data: IndustriesState) : LiveDataResource()

    class AreaPlainStorage(val data: AreaPlain?) : LiveDataResource()

    class CountryNameStorage(val data: String) : LiveDataResource()

    class AreaNameStorage(val data: String) : LiveDataResource()
}
