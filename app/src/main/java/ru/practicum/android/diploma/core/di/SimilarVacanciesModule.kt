package ru.practicum.android.diploma.core.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.feature.similar_vacancies.domain.GetSimilarVacanciesUseCase
import ru.practicum.android.diploma.feature.similar_vacancies.presentation.viewmodels.SimilarVacanciesViewModel

val similarVacanciesModule = module {
    viewModel {
        SimilarVacanciesViewModel(get())
    }

    factory<GetSimilarVacanciesUseCase>{
        GetSimilarVacanciesUseCase(get())

    }
}