package ru.practicum.android.diploma.feature.search.searchadapter

import androidx.recyclerview.widget.DiffUtil
import ru.practicum.android.diploma.feature.search.domain.models.VacancyShort

class VacancyDiffCallBack(
    private val oldList: List<VacancyShort>,
    private val newList: List<VacancyShort>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldList = oldList[oldItemPosition]
        val newList = newList[newItemPosition]
        return oldList.id == newList.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldList = oldList[oldItemPosition]
        val newList = newList[newItemPosition]
        return oldList == newList
    }
}