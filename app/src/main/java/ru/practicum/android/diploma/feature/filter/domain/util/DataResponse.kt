package ru.practicum.android.diploma.feature.filter.domain.util

import ru.practicum.android.diploma.feature.filter.domain.model.Industry

data class DataResponse<T>(
    val data: List<T>?,
    val networkError: NetworkError?
)
