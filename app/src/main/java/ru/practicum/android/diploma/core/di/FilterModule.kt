package ru.practicum.android.diploma.core.di

import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.core.util.BASE_URL
import ru.practicum.android.diploma.core.util.FILTER_SETTINGS_SP
import ru.practicum.android.diploma.feature.filter.data.network.HeadHunterDirectoryApi
import ru.practicum.android.diploma.feature.filter.data.network.NetworkDirectoryClient
import ru.practicum.android.diploma.feature.filter.data.network.RetrofitNetworkDirectoryClient
import ru.practicum.android.diploma.feature.filter.data.repository.DirectoryRepositoryImpl
import ru.practicum.android.diploma.feature.filter.data.repository.FilterSettingsRepositoryImpl
import ru.practicum.android.diploma.feature.filter.domain.api.DirectoryRepository
import ru.practicum.android.diploma.feature.filter.domain.api.FilterSettingsRepository
import ru.practicum.android.diploma.feature.filter.domain.usecase.ClearFilterSettingsUseCase
import ru.practicum.android.diploma.feature.filter.domain.usecase.GetAreasUseCase
import ru.practicum.android.diploma.feature.filter.domain.usecase.GetCountriesUseCase
import ru.practicum.android.diploma.feature.filter.domain.usecase.GetFilterSettingsUseCase
import ru.practicum.android.diploma.feature.filter.domain.usecase.GetIndustriesUseCase
import ru.practicum.android.diploma.feature.filter.domain.usecase.SaveFilterSettingsUseCase
import ru.practicum.android.diploma.feature.filter.presentation.viewmodels.ChooseCountryViewModel
import ru.practicum.android.diploma.feature.filter.presentation.viewmodels.ChooseAreaViewModel
import ru.practicum.android.diploma.feature.filter.presentation.viewmodels.ChooseIndustryViewModel
import ru.practicum.android.diploma.feature.filter.presentation.viewmodels.SettingsFiltersViewModel
import ru.practicum.android.diploma.feature.filter.presentation.viewmodels.ChooseWorkPlaceViewModel


val filterModule = module {

    // Network
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

    // Shared Preferences
    single<SharedPreferences> {
        androidContext().getSharedPreferences(FILTER_SETTINGS_SP, Context.MODE_PRIVATE)
    }

    single<FilterSettingsRepository> {
       FilterSettingsRepositoryImpl(sharedPreferences = get())
    }

    factory<GetFilterSettingsUseCase> {
        GetFilterSettingsUseCase(filterSettingsRepository = get())
    }

    factory<SaveFilterSettingsUseCase> {
        SaveFilterSettingsUseCase(filterSettingsRepository = get())
    }

    factory<ClearFilterSettingsUseCase> {
        ClearFilterSettingsUseCase(filterSettingsRepository = get())
    }


    viewModelOf(::ChooseCountryViewModel)
    viewModelOf(::ChooseAreaViewModel)
    viewModelOf(::ChooseIndustryViewModel)
    viewModelOf(::SettingsFiltersViewModel)
    viewModelOf(::ChooseWorkPlaceViewModel)

}