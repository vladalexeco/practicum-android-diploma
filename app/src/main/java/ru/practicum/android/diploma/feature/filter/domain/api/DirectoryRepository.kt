package ru.practicum.android.diploma.feature.filter.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.feature.filter.domain.util.Resource
import ru.practicum.android.diploma.feature.filter.domain.model.Industry

interface DirectoryRepository {

    fun getIndustries(): Flow<Resource<List<Industry>>>

}