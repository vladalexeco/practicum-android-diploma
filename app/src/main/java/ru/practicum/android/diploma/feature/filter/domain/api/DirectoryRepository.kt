package ru.practicum.android.diploma.feature.filter.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.feature.filter.domain.model.Area
import ru.practicum.android.diploma.feature.filter.domain.model.Country
import ru.practicum.android.diploma.feature.filter.domain.util.Resource
import ru.practicum.android.diploma.feature.filter.domain.model.Industry

interface DirectoryRepository {

    fun getIndustries(): Flow<Resource<List<Industry>>>

    fun getCountries(): Flow<Resource<List<Country>>>

    fun getAreas(areaId: String): Flow<Resource<List<Area>>>

}