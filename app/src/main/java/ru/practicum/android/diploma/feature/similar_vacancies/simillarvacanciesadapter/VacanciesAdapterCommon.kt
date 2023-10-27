package ru.practicum.android.diploma.feature.similar_vacancies.simillarvacanciesadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.feature.search.domain.models.VacancyShort
import ru.practicum.android.diploma.feature.search.searchadapter.VacancyDiffCallBack

class VacanciesAdapterCommon(
    private val listener: ClickListener,
    private val context: Context
) : RecyclerView.Adapter<VacanciesViewHolderCommon>() {

    private val vacancies = mutableListOf<VacancyShort>()

    fun setVacancyList(list: List<VacancyShort>) {
        val diffCallBack = VacancyDiffCallBack(vacancies, list)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        vacancies.clear()
        vacancies.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    fun interface ClickListener {
        fun onClick(vacancy: VacancyShort)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacanciesViewHolderCommon {
        return VacanciesViewHolderCommon(
            VacancyItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            context
        )
    }

    override fun onBindViewHolder(holder: VacanciesViewHolderCommon, position: Int) {
        holder.bind(vacancies[position], listener)
    }

    override fun getItemCount(): Int {
        return vacancies.size
    }
}