package ru.practicum.android.diploma.feature.search.searchadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.LoadingItemBinding
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.feature.search.domain.models.VacancyShort

class VacanciesAdapter(
    private val listener: ClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var vacancies = listOf<VacancyShort>()
        set(newValue) {
            val diffCallBack = VacancyDiffCallBack(field, newValue)
            val diffResult = DiffUtil.calculateDiff(diffCallBack)
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }

    fun setVacancyList(list: List<VacancyShort>) {
        val vacanciesTmp = vacancies.toMutableList()
        vacanciesTmp.addAll(list)
        vacancies = vacanciesTmp
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
        return if (position < itemCount - 4) {
            ITEM_VIEW_TYPE_ITEM
        } else {
            ITEM_VIEW_TYPE_LOADING
        }
    }

    companion object {
        private const val ITEM_VIEW_TYPE_ITEM = 0
        private const val ITEM_VIEW_TYPE_LOADING = 1
    }
}