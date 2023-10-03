package ru.practicum.android.diploma.feature.filter.domain.util

import ru.practicum.android.diploma.feature.filter.domain.model.Industry

data class DataResponse(
    val data: List<Industry>?,
    val networkError: NetworkError?
)
