package ru.practicum.android.diploma.feature.search.searchadapter

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.util.CurrencyLogoCreator
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.feature.search.domain.models.VacancyShort

class VacancyViewHolder(
    private val binding: VacancyItemBinding,
    private val context: Context
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("StringFormatMatches")
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
            when {
                model.salary.from != null && model.salary.to != null -> {
                    binding.salaryTextView.text =
                       context.getString(R.string.salary_template_from_to, model.salary.from, model.salary.to, text)
                }

                model.salary.from == null && model.salary.to != null -> {
                    binding.salaryTextView.text = context.getString(R.string.salary_template_to, model.salary.to, text)
                }

                else -> {
                    binding.salaryTextView.text = context.getString(R.string.salary_template_from, model.salary.from, text)
                }
            }

        } else {
            binding.salaryTextView.text = context.getString(R.string.message_salary_not_pointed)
        }
    }
}