package ru.practicum.android.diploma.feature.filter.data.network

import ru.practicum.android.diploma.feature.filter.data.network.dto.Request
import ru.practicum.android.diploma.feature.filter.data.network.dto.response.Response

interface NetworkDirectoryClient {
    suspend fun doRequest(dto: Request): Response
}