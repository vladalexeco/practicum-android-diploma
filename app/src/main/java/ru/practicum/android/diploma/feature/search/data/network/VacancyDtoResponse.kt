package ru.practicum.android.diploma.feature.search.data.network

import com.usunin1994.headhunterapi.data.dtomodels.VacancyDtoFull
import ru.practicum.android.diploma.feature.search.data.Response

data class VacancyDtoResponse(val vacancy: VacancyDtoFull): Response()
