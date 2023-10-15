package ru.practicum.android.diploma.feature.similar_vacancies.simillarvacanciesadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.feature.search.domain.models.VacancyShort
import ru.practicum.android.diploma.feature.search.searchadapter.VacancyDiffCallBack

class SimilarVacanciesAdapter(private val listener: ClickListener)
    : RecyclerView.Adapter<SimilarVacanciesViewHolder> () {

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

    fun interface ClickListener {
        fun onClick(vacancy: VacancyShort)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarVacanciesViewHolder {
        return SimilarVacanciesViewHolder(
            VacancyItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SimilarVacanciesViewHolder, position: Int) {
        holder.bind(vacancies[position], listener)
    }

    override fun getItemCount(): Int {
        return vacancies.size
    }
}