package ru.practicum.android.diploma.core.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.core.util.BASE_URL
import ru.practicum.android.diploma.feature.filter.data.network.HeadHunterDirectoryApi
import ru.practicum.android.diploma.feature.filter.data.network.NetworkDirectoryClient
import ru.practicum.android.diploma.feature.filter.data.network.RetrofitNetworkDirectoryClient
import ru.practicum.android.diploma.feature.filter.data.repository.DirectoryRepositoryImpl
import ru.practicum.android.diploma.feature.filter.domain.api.DirectoryRepository
import ru.practicum.android.diploma.feature.filter.domain.usecase.GetAreasUseCase
import ru.practicum.android.diploma.feature.filter.domain.usecase.GetCountriesUseCase
import ru.practicum.android.diploma.feature.filter.domain.usecase.GetIndustriesUseCase
import ru.practicum.android.diploma.feature.filter.presentation.viewmodels.ChooseIndustryViewModel

val filterModule = module {

    single<HeadHunterDirectoryApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HeadHunterDirectoryApi::class.java)
    }

    single<NetworkDirectoryClient> {
        RetrofitNetworkDirectoryClient(context = androidContext(), directoryService = get())
    }

    single<DirectoryRepository> {
        DirectoryRepositoryImpl(networkDirectoryClient = get())
    }

    factory<GetIndustriesUseCase> {
        GetIndustriesUseCase(directoryRepository = get())
    }

    factory<GetCountriesUseCase> {
        GetCountriesUseCase(directoryRepository = get())
    }

    factory<GetAreasUseCase> {
        GetAreasUseCase(directoryRepository = get())
    }

    viewModelOf(::ChooseIndustryViewModel)
}