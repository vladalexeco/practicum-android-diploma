package ru.practicum.android.diploma.feature.filter.domain.model

abstract class IndustryAreaModel(
    open val id: String,
    open val name: String,
    open var isChecked: Boolean = false,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as IndustryAreaModel

        if (
            this.id != other.id ||
            this.name != other.name
        ) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}