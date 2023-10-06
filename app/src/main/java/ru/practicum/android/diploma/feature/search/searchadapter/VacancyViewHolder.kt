package ru.practicum.android.diploma.feature.search.searchadapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.feature.search.domain.models.VacancyShort

class VacancyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val vacancy = itemView.findViewById<TextView>(R.id.vacancy_textView)
    private val employer = itemView.findViewById<TextView>(R.id.employer_textView)
    private val salary = itemView.findViewById<TextView>(R.id.salary_textView)
    private val logo = itemView.findViewById<ImageView>(R.id.vacancy_imageView)

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