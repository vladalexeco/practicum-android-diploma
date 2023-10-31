package ru.practicum.android.diploma.feature.filter.presentation.adapter

import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.feature.filter.domain.model.IndustryAreaModel

class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val name: TextView = itemView.findViewById(R.id.industry_region_title_textview)
    private val radioButton: RadioButton =
        itemView.findViewById(R.id.industry_region_check_radiobutton)

    fun bind(
        model: IndustryAreaModel,
        position: Int,
        clickListener: IndustriesAreasAdapter.ClickListener,
        notifyItemChanged: () -> Unit,
    ) {
        name.text = model.name
        radioButton.apply {
            isChecked = model.isChecked
            setOnClickListener {
                clickListener.onItemClicked(model, position, notifyItemChanged)
            }
        }
    }
}