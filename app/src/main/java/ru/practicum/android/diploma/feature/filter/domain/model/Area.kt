package ru.practicum.android.diploma.feature.filter.domain.model

data class Area(
    override val id: String,
    val parent_id: String?,
    override val name: String,
    val areas: List<Area>,
    override var isChecked: Boolean = false
) : IndustryAreaModel(id, name)

fun Area.mapToAreaPlain(): AreaPlain = AreaPlain(id = this.id, name = this.name)
