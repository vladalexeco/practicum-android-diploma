package ru.practicum.android.diploma.feature.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.feature.search.data.NetworkClient
import ru.practicum.android.diploma.feature.search.data.Response

class RetrofitNetworkClient (private val context: Context,
                             private val hhApi: HeadHunterApi) : NetworkClient {

    override suspend fun getVacancies(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }
        if (dto !is SearchRequest) {
            return Response().apply { resultCode = 400 }
        }
        return withContext(Dispatchers.IO) {
            try {
                val response = hhApi.getVacancies(dto.data)
                response.apply { resultCode = 200 }
            } catch (e:Throwable) {
                Response().apply { resultCode = 500}
            }
        }
    }

    override suspend fun getVacancy(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }
        if (dto !is SearchRequest) {
            return Response().apply { resultCode = 400 }
        }
        return withContext(Dispatchers.IO) {
            try {
                val responseVacancy = hhApi.getVacancy(dto.data)
                val response = VacancyDtoResponse(vacancy = responseVacancy)
                response.apply { resultCode = 200 }
            } catch (e:Throwable) {
                Response().apply { resultCode = 500}
            }
        }
    }

    override suspend fun getSimilarVacancies(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }
        if (dto !is SearchRequest) {
            return Response().apply { resultCode = 400 }
        }
        return withContext(Dispatchers.IO) {
            try {
                val response = hhApi.getSimilarVacancies(dto.data)
                response.apply { resultCode = 200 }
            } catch (e:Throwable) {
                Response().apply { resultCode = 500}
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