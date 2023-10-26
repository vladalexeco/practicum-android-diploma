package ru.practicum.android.diploma.feature.details.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.feature.details.domain.api.ExternalNavigator

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    override fun sendEmail(email: String) {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(context.getString(R.string.external_nav_mail_to))
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(this)
        }
    }

    override fun makeCall(phoneNumber: String) {
        Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse(buildString{
                append(R.string.external_nav_telephone)
                append(phoneNumber)
            })
            putExtra(Intent.EXTRA_EMAIL, arrayOf(phoneNumber))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(this)
        }
    }

    //todo заменить параметр String на Vacancy, когда модель будет добавлена, реализовать формирование текста из модели Вакансии
    override fun shareVacancy(vacancy: String) {
        Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, vacancy)
            type = "text/plain"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(this)
        }
    }
}