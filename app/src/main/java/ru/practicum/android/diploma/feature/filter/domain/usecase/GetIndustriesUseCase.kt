package ru.practicum.android.diploma.feature.filter.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.feature.filter.domain.util.Resource
import ru.practicum.android.diploma.feature.filter.domain.api.DirectoryRepository
import ru.practicum.android.diploma.feature.filter.domain.model.Industry
import ru.practicum.android.diploma.feature.filter.domain.util.DataResponse

/**
 * Делает запрос на сервер по адресу https://api.hh.ru/industries
 * возвращает объект DataResponse c двумя nullable полями
 * - поле data представляет собой список List, содержащий в себе доступные отрасли (объект Industry).
 * Стоит отметить, что элемент списка сам может содержать в себе список объектов Industry
 * - поле networkError представляет собой объект enum класса NetworkError
 * который содержит две константы BAD_CONNECTION и SERVER_ERROR.
 * BAD_CONNECTION - результат отсутствия интернет связи на устройстве
 * SERVER_ERROR - ошибка на сервере
 */
class GetIndustriesUseCase(private val directoryRepository: DirectoryRepository) {

    operator fun invoke(): Flow<DataResponse<Industry>> {
        return directoryRepository.getIndustries().map { result ->

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