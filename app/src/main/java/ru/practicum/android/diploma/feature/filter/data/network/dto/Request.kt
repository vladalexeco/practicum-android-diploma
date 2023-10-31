package ru.practicum.android.diploma.feature.filter.data.network.dto

sealed class Request {
    object IndustryRequest : Request()
    object CountryRequest : Request()
    object AllAreasRequest : Request()
    class AreaRequest(val areaId: String) : Request()
    class AreaPlainRequest(val areaId: String) : Request()
}
