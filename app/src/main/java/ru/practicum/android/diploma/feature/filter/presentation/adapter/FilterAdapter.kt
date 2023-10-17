package ru.practicum.android.diploma.feature.filter.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.feature.filter.domain.model.IndustryAreaModel

class FilterAdapter<T : IndustryAreaModel>(
    val items: ArrayList<T>,
    private val clickListener: ClickListener
) : RecyclerView.Adapter<FilterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder =
        FilterViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.industry_area_item, parent, false)
        )

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(
            items[holder.adapterPosition],
            holder.adapterPosition,
            clickListener,
        ) { notifyItemChanged(holder.adapterPosition) }

        holder.itemView.setOnClickListener {
            clickListener.onItemClicked(
                items[holder.adapterPosition],
                holder.adapterPosition,
            ) { notifyItemChanged(holder.adapterPosition) }
        }
    }

    override fun getItemCount(): Int = items.size

    fun interface ClickListener {
        fun onItemClicked(
            model: IndustryAreaModel,
            position: Int,
            notifyItemChanged: () -> Unit,
        )
    }
}