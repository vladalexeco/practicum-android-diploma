package ru.practicum.android.diploma.feature.filter.domain.util

data class DataResponse<T>(
    val data: List<T>?,
    val networkError: NetworkError?
)
