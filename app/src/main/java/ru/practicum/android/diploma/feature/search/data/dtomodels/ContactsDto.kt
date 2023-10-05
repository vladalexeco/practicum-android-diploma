package com.usunin1994.headhunterapi.data.dtomodels

data class ContactsDto(
    val email: String,
    val name: String,
    val phones: List<PhoneDto>
)