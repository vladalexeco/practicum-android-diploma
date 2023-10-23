package ru.practicum.android.diploma.feature.search.searchadapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.core.util.IsLastPage
import ru.practicum.android.diploma.databinding.LoadingItemBinding
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.feature.search.domain.models.VacancyShort

class VacanciesAdapter(
    private val listener: ClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var vacancies = listOf<VacancyShort>()
        set(newValue) {
            val diffCallBack = VacancyDiffCallBack(field, newValue)
            val diffResult = DiffUtil.calculateDiff(diffCallBack)
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }

    fun setVacancyList(list: List<VacancyShort>) {
        vacancies = list
    }

    fun clear() {
        vacancies = ArrayList()
    }

    fun interface ClickListener {
        fun onClick(vacancy: VacancyShort)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_VIEW_TYPE_ITEM) {
            VacancyViewHolder(
                VacancyItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        } else {
            LoadingViewHolder(
                LoadingItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is VacancyViewHolder) {
            holder.bind(vacancies[position], listener)
        } else {
            //
        }
    }

    override fun getItemCount(): Int {
        return vacancies.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < itemCount - 1) {
            ITEM_VIEW_TYPE_ITEM
        } else {
            if (!IsLastPage.IS_LAST_PAGE) ITEM_VIEW_TYPE_LOADING else ITEM_VIEW_TYPE_ITEM
        }
    }

    companion object {
        private const val ITEM_VIEW_TYPE_ITEM = 0
        private const val ITEM_VIEW_TYPE_LOADING = 1
    }
}