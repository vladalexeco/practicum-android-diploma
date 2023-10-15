package ru.practicum.android.diploma.feature.search.searchadapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.util.CurrencyLogoCreator
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.feature.search.domain.models.VacancyShort

class VacancyViewHolder(private val binding: VacancyItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        model: VacancyShort,
        listener: VacanciesAdapter.ClickListener
    ) {

        itemView.setOnClickListener {
            listener.onClick(model)
        }

        Glide.with(itemView)
            .load(model.employer?.logoUrls?.original)
            .placeholder(R.drawable.placeholder)
            .into(binding.vacancyImageView)

        binding.vacancyTextView.text = "${model.name}, ${model.area.name}"
        binding.employerTextView.text = model.employer?.name
        if (model.salary != null) {
            val text = CurrencyLogoCreator.getSymbol(model.salary.currency)
            if (model.salary.from != null && model.salary.to != null) {
                binding.salaryTextView.text = "От ${model.salary.from} до ${model.salary.to} $text"
            } else if (model.salary.from == null && model.salary.to != null) {
                binding.salaryTextView.text = "До ${model.salary.to} $text"
            } else {
                binding.salaryTextView.text = "От ${model.salary.from} $text"
            }

        } else {
            binding.salaryTextView.text = "Зарплата не указана"
        }
    }
}