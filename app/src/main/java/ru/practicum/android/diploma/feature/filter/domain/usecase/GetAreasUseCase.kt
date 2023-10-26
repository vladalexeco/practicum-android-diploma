package ru.practicum.android.diploma.feature.filter.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.feature.filter.domain.api.DirectoryRepository
import ru.practicum.android.diploma.feature.filter.domain.model.Area
import ru.practicum.android.diploma.feature.filter.domain.util.DataResponse
import ru.practicum.android.diploma.feature.filter.domain.util.Resource

/**
 * Делает запрос на сервер по адресу https://api.hh.ru/areas/{area_id},
 * где area_id - идентификатор страны, являющийся полем объекта Country.
 *
 * Соответственно, этот запрос возвращает список List всех субъектов
 * и регионов (объект Area) конкретной страны по ее id.
 *
 * Стоит отметить, что объект Area сам может содержить другие объекты Area в виде списка
 *
 * Метод юзкейса возвращает объект DataResponse c двумя nullable полями
 * - поле data представляет собой список List, содержащий в себе регионы (объект Area)
 * - поле networkError представляет собой объект enum класса NetworkError,
 * который содержит две константы BAD_CONNECTION и SERVER_ERROR.
 * BAD_CONNECTION - результат отсутствия интернет связи на устройстве
 * SERVER_ERROR - ошибка на сервере
 */

class GetAreasUseCase(private val directoryRepository: DirectoryRepository) {

    operator fun invoke(areaId: String): Flow<DataResponse<Area>> {
        return directoryRepository.getAreas(areaId).map { result ->
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