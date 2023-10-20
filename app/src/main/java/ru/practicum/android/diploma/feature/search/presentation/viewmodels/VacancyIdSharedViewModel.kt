package ru.practicum.android.diploma.feature.search.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VacancyIdSharedViewModel : ViewModel() {

    var vacancyId: String? = null
        set(value) {
            vacancyIdLiveData.value = value ?: ""
            field = value
        }

    private var vacancyIdLiveData = MutableLiveData<String>()
    fun observeVacancyId(): LiveData<String> = vacancyIdLiveData
}