package ru.practicum.android.diploma.feature.favourite.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.util.CurrencyLogoCreator
import ru.practicum.android.diploma.feature.search.domain.models.VacancyFull

class VacanciesPagingAdapter : PagingDataAdapter<VacancyFull, VacancyViewHolder>(diffCallback) {

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<VacancyFull>() {
            override fun areItemsTheSame(oldItem: VacancyFull, newItem: VacancyFull): Boolean {
                // Сравниваем уникальные идентификаторы элементов
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: VacancyFull, newItem: VacancyFull): Boolean {
                // Сравниваем содержимое элементов, если они имеют одинаковый идентификатор
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        return VacancyViewHolder(parent)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}


class VacancyViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.vacancy_item, parent, false)
) {

    private var logoImageViewItem = itemView.findViewById<ImageView>(R.id.vacancy_imageView)
    private var vacancyTextViewItem = itemView.findViewById<TextView>(R.id.vacancy_textView)
    private var employerTextViewItem = itemView.findViewById<TextView>(R.id.employer_textView)
    private var salaryTextViewItem = itemView.findViewById<TextView>(R.id.salary_textView)

    fun bind(vacancyFull: VacancyFull?) {

        Glide.with(itemView)
            .load(vacancyFull?.employer?.logoUrls?.original)
            .placeholder(R.drawable.placeholder)
            .into(logoImageViewItem)

        vacancyTextViewItem.text = "${vacancyFull?.name}, ${vacancyFull?.area?.name}"
        employerTextViewItem.text = vacancyFull?.employer?.name

        if (vacancyFull?.salary != null) {
            val text = CurrencyLogoCreator.getSymbol(vacancyFull.salary.currency)
            when {
                vacancyFull.salary.from != null && vacancyFull.salary.to != null -> {
                    salaryTextViewItem.text =
                        "От ${vacancyFull.salary.from} до ${vacancyFull.salary.to} $text"
                }

                vacancyFull.salary.from == null && vacancyFull.salary.to != null -> {
                    salaryTextViewItem.text = "До ${vacancyFull.salary.to} $text"
                }

                else -> {
                    salaryTextViewItem.text = "От ${vacancyFull.salary.from} $text"
                }
            }

        } else {
            salaryTextViewItem.text = "Зарплата не указана"
        }

    }

}