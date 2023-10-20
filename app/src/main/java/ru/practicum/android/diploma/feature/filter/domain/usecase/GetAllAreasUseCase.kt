package ru.practicum.android.diploma.feature.filter.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.feature.filter.domain.api.DirectoryRepository
import ru.practicum.android.diploma.feature.filter.domain.model.Area
import ru.practicum.android.diploma.feature.filter.domain.util.DataResponse
import ru.practicum.android.diploma.feature.filter.domain.util.Resource

class GetAllAreasUseCase(private val directoryRepository: DirectoryRepository) {

    operator fun invoke(): Flow<DataResponse<Area>> {
        return directoryRepository.getAllAreas().map { result ->

            when(result) {
                is Resource.Success -> {
                    DataResponse(data = result.data, networkError = null)
                }

                is Resource.Error -> {
                    DataResponse(data = null, networkError = result.networkError)
                }
            }

        }
    }

}