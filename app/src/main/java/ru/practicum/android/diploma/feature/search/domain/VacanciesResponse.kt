package ru.practicum.android.diploma.feature.search.domain

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.feature.search.domain.models.VacancyShort

data class VacanciesResponse(
    val items: List<VacancyShort>,
    val found: Int,
    val pages: Int,
    val perPage: Int,
    val page: Int)

