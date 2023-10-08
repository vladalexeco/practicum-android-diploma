package ru.practicum.android.diploma.feature.filter.domain.model

data class Industry(
    override val id: String,
    override val name: String,
    val industries: List<Industry>? = null,
    override var isChecked: Boolean = false
) : IndustryAreaModel(id, name)
