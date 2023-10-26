package ru.practicum.android.diploma.feature.filter.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.core.util.STATUS_CODE_NO_NETWORK_CONNECTION
import ru.practicum.android.diploma.core.util.STATUS_CODE_SERVER_ERROR
import ru.practicum.android.diploma.core.util.STATUS_CODE_SUCCESS
import ru.practicum.android.diploma.feature.filter.data.network.dto.Request
import ru.practicum.android.diploma.feature.filter.data.network.dto.response.AreaResponse
import ru.practicum.android.diploma.feature.filter.data.network.dto.response.CountryResponse
import ru.practicum.android.diploma.feature.filter.data.network.dto.response.IndustryResponse
import ru.practicum.android.diploma.feature.filter.data.network.dto.response.Response

class RetrofitNetworkDirectoryClient(
    private val context: Context,
    private val directoryService: HeadHunterDirectoryApi
) : NetworkDirectoryClient {

    override suspend fun doRequest(dto: Request): Response {

        if (!isConnected()) {
            return Response().apply { resultCode = STATUS_CODE_NO_NETWORK_CONNECTION }
        }

        return withContext(Dispatchers.IO) {
            when (dto) {
                is Request.AreaRequest -> getAreas(dto)
                is Request.CountryRequest -> getCountries()
                is Request.IndustryRequest -> getIndustries()
                is Request.AllAreasRequest -> getAllAreas()
                is Request.AreaPlainRequest -> getAreaPlain(dto)
            }
        }
    }

    private suspend fun getAreas(dto: Request.AreaRequest): Response {
        return try {
            val response = directoryService.getAreas(dto.areaId)
            response.apply { resultCode = STATUS_CODE_SUCCESS }
        } catch (e: Throwable) {
            Response().apply { resultCode = STATUS_CODE_SERVER_ERROR }
        }
    }

    private suspend fun getCountries(): Response {
        return try {
            val responseList = directoryService.getCountries()
            val response = CountryResponse(countries = responseList)
            response.apply { resultCode = STATUS_CODE_SUCCESS }
        } catch (e: Throwable) {
            Response().apply { resultCode = STATUS_CODE_SERVER_ERROR }
        }
    }

    private suspend fun getIndustries(): Response {
        return try {
            val responseList = directoryService.getIndustries()
            val response = IndustryResponse(industries = responseList)
            response.apply { resultCode = STATUS_CODE_SUCCESS }
        } catch (e: Throwable) {
            Response().apply { resultCode = STATUS_CODE_SERVER_ERROR }
        }
    }

    private suspend fun getAllAreas(): Response {
        return try {
            val responseList = directoryService.getAllAreas()
            val response = AreaResponse(areas = responseList)
            response.apply { resultCode = STATUS_CODE_SUCCESS }
        } catch (e: Throwable) {
            Response().apply { resultCode = STATUS_CODE_SERVER_ERROR }
        }
    }

    private suspend fun getAreaPlain(dto: Request.AreaPlainRequest): Response {
        return try {
            val response = directoryService.getAreaPlain(dto.areaId)
            response.apply { resultCode = STATUS_CODE_SUCCESS }
        } catch (e: Throwable) {
            Response().apply { resultCode = STATUS_CODE_SERVER_ERROR }
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