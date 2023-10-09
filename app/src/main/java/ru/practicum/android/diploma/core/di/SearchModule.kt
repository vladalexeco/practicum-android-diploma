package ru.practicum.android.diploma.core.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.feature.search.data.NetworkClient
import ru.practicum.android.diploma.feature.search.data.VacancyRepositoryImpl
import ru.practicum.android.diploma.feature.search.data.network.HeadHunterApi
import ru.practicum.android.diploma.feature.search.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.feature.similar_vacancies.domain.GetSimilarVacanciesUseCase
import ru.practicum.android.diploma.feature.search.domain.GetVacanciesUseCase
import ru.practicum.android.diploma.feature.search.domain.GetVacancyUseCase
import ru.practicum.android.diploma.feature.search.domain.VacancyRepository
import ru.practicum.android.diploma.feature.search.presentation.viewmodels.SearchViewModel

val searchModule = module {
    viewModel {
        SearchViewModel(get())
    }

    factory<GetVacanciesUseCase>{
        GetVacanciesUseCase(get())
    }

    factory<GetVacancyUseCase>{
        GetVacancyUseCase(get())
    }

    single<VacancyRepository> {
        VacancyRepositoryImpl(get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), get())
    }

    single<HeadHunterApi> {
        Retrofit.Builder().baseUrl("https://api.hh.ru")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HeadHunterApi::class.java)
    }
}