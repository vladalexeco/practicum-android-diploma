package ru.practicum.android.diploma.feature.filter.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.feature.filter.domain.model.Country

class CountriesAdapter(
    private val countries: ArrayList<Country>,
    private val clickListener: ClickListener
) : RecyclerView.Adapter<CountriesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesViewHolder =
        CountriesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.country_item, parent, false)
        )

    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        holder.bind(countries[position])
        holder.itemView.setOnClickListener { clickListener.onClick(countries[position]) }
    }

    override fun getItemCount(): Int = countries.size

    fun interface ClickListener {
        fun onClick(country: Country)
    }
}