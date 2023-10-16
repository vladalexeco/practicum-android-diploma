package ru.practicum.android.diploma.feature.filter.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.feature.filter.domain.api.DirectoryRepository
import ru.practicum.android.diploma.feature.filter.domain.model.AreaPlain
import ru.practicum.android.diploma.feature.filter.domain.util.NetworkError
import ru.practicum.android.diploma.feature.filter.domain.util.Resource

class GetAreaPlainUseCase(private val directoryRepository: DirectoryRepository) {
    operator fun invoke(areaId: String): Flow<Pair<AreaPlain?, NetworkError?>> {
        return directoryRepository.getAreaPlain(areaId).map { result ->

            when(result) {

                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.networkError)
                }
            }

        }
    }
}