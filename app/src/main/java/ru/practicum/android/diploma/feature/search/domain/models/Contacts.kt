package ru.practicum.android.diploma.feature.search.domain.models

data class Contacts(
    val email: String,
    val name: String,
    val phones: List<Phone>
)