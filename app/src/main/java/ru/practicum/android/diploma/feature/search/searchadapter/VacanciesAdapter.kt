package ru.practicum.android.diploma.feature.search.searchadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.feature.search.domain.models.VacancyShort

class VacanciesAdapter (private val listener: ClickListener
): RecyclerView.Adapter<VacancyViewHolder> () {

    internal var vacancies = listOf<VacancyShort>()
        set(newValue) {
            val diffCallBack = VacancyDiffCallBack(field, newValue)
            val diffResult = DiffUtil.calculateDiff(diffCallBack)
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }


    fun interface ClickListener {
        fun onClick(vacancy: VacancyShort)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val binding = VacancyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VacancyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(vacancies[position], listener)
    }

    override fun getItemCount(): Int {
        return vacancies.size
    }

}