package ru.practicum.android.diploma.feature.search.data.dtomodels

import com.usunin1994.headhunterapi.data.dtomodels.PhoneDto

data class ContactsDto(
    val email: String,
    val name: String,
    val phones: List<PhoneDto>
)