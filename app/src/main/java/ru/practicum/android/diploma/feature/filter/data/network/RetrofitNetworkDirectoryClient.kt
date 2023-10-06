package ru.practicum.android.diploma.feature.filter.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.feature.filter.data.network.dto.Request
import ru.practicum.android.diploma.feature.filter.data.network.dto.response.Response


class RetrofitNetworkDirectoryClient(
    private val context: Context,
    private val directoryService: HeadHunterDirectoryApi
) : NetworkDirectoryClient {

    override suspend fun doRequest(dto: Request): Response {

        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }

        return withContext(Dispatchers.IO) {
            when(dto) {

                is Request.IndustryRequest -> {
                    try {
                        val response = directoryService.getIndustries()
                        response.apply { resultCode = 200 }
                    } catch (e: Throwable) {
                        Response().apply { resultCode = 500 }
                    }
                }

                is Request.CountryRequest -> {
                    try {
                        val response = directoryService.getCountries()
                        response.apply { resultCode = 200 }
                    }catch (e: Throwable) {
                        Response().apply { resultCode = 500 }
                    }
                }

                is Request.AreaRequest -> {
                    try {
                        val response = directoryService.getAreas(dto.areaId)
                        response.apply { resultCode = 200 }
                    }catch (e: Throwable) {
                        Response().apply { resultCode = 500 }
                    }
                }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}