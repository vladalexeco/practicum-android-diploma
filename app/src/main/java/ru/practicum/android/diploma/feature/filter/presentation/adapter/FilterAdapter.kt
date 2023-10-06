package ru.practicum.android.diploma.feature.filter.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.feature.filter.domain.model.IndustryAreaModel

class FilterAdapter<T : IndustryAreaModel>(private val clickListener: ClickListener) :
    RecyclerView.Adapter<FilterViewHolder>() {

    val items: ArrayList<T> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder =
        FilterViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.industry_area_item, parent, false)
        )

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener { clickListener.onClick(items[position]) }
    }

    override fun getItemCount(): Int = items.size

    fun interface ClickListener {
        fun onClick(model: IndustryAreaModel)
    }
}