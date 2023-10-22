package ru.practicum.android.diploma.feature.search.data.dtomodels

data class ContactsDto(
    val email: String,
    val name: String,
    val phones: List<PhoneDto>
)