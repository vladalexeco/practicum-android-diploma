package ru.practicum.android.diploma.feature.filter.presentation.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.feature.filter.domain.model.IndustryAreaModel

class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val name: TextView = itemView.findViewById(R.id.industry_region_title_textview)

    fun bind(model: IndustryAreaModel) {
        name.text = model.name
    }
}