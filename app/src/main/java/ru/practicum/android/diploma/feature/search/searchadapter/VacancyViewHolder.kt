package ru.practicum.android.diploma.feature.search.searchadapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.feature.search.domain.models.VacancyShort

class VacancyViewHolder(binding: VacancyItemBinding) : RecyclerView.ViewHolder(binding.root) {

    private val vacancy = binding.vacancyTextView
    private val employer = binding.employerTextView
    private val salary = binding.salaryTextView
    private val logo = binding.vacancyImageView

    fun bind(
        model: VacancyShort,
        listener: VacanciesAdapter.ClickListener
    ) {

        itemView.setOnClickListener {
            listener.onClick(model)
        }

        if (model.employer?.logoUrls != null) {
            Glide.with(itemView)
                .load(model.employer.logoUrls.original)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(logo)
        } else {
            // Если logo_urls равен null, загружаем изображение по умолчанию
            Glide.with(itemView)
                .load(R.drawable.ic_launcher_foreground)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(logo)
        }

        vacancy.text="${model.name}, ${model.area.name}"
        employer.text=model.employer?.name
        if (model.salary != null) {
            salary.text="От ${model.salary.from} до ${model.salary.to}"
        } else {
            salary.text = "Зарплата не указана"
        }

    }
}