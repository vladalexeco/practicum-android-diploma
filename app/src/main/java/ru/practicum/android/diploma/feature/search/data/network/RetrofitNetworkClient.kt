package ru.practicum.android.diploma.feature.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.core.util.STATUS_CODE_NO_NETWORK_CONNECTION
import ru.practicum.android.diploma.core.util.STATUS_CODE_SERVER_ERROR
import ru.practicum.android.diploma.core.util.STATUS_CODE_SUCCESS
import ru.practicum.android.diploma.feature.filter.domain.model.FilterSettings
import ru.practicum.android.diploma.feature.search.data.NetworkClient
import ru.practicum.android.diploma.feature.search.data.Response

class RetrofitNetworkClient(
    private val context: Context,
    private val hhApi: HeadHunterApi
) : NetworkClient {

    override suspend fun getVacancies(
        dto: SearchRequest,
        pages: Int,
        perPage: Int,
        page: Int,
        filterSettings: FilterSettings
    ): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = STATUS_CODE_NO_NETWORK_CONNECTION }
        }
        return withContext(Dispatchers.IO) {
            try {
                val options = mapOf(
                    "pages" to pages,
                    "per_page" to perPage,
                    "page" to page
                )
                val response = hhApi.getVacancies(
                    text = dto.data,
                    options = options,
                    area = if (filterSettings.areaPlain?.id == null) {
                        filterSettings.country?.id
                    } else {
                        filterSettings.areaPlain.id
                    },
                    industry = filterSettings.industryPlain?.id,
                    salary = if (filterSettings.expectedSalary == -1) null else filterSettings.expectedSalary,
                    withSalary = filterSettings.notShowWithoutSalary
                )
                response.apply { resultCode = STATUS_CODE_SUCCESS }
            } catch (e: Throwable) {
                Response().apply { resultCode = STATUS_CODE_SERVER_ERROR }
            }
        }
    }

    override suspend fun getVacancy(dto: SearchRequest): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = STATUS_CODE_NO_NETWORK_CONNECTION }
        }
        return withContext(Dispatchers.IO) {
            try {
                val responseVacancy = hhApi.getVacancy(dto.data)
                val response = VacancyDtoResponse(vacancy = responseVacancy)
                response.apply { resultCode = STATUS_CODE_SUCCESS }
            } catch (e: Throwable) {
                Response().apply { resultCode = STATUS_CODE_SERVER_ERROR }
            }
        }
    }

    override suspend fun getSimilarVacancies(dto: SearchRequest): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = STATUS_CODE_NO_NETWORK_CONNECTION }
        }
        return withContext(Dispatchers.IO) {
            try {
                val response = hhApi.getSimilarVacancies(dto.data)
                response.apply { resultCode = STATUS_CODE_SUCCESS }
            } catch (e: Throwable) {
                Response().apply { resultCode = STATUS_CODE_SERVER_ERROR }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.run {
            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } ?: false
    }
}