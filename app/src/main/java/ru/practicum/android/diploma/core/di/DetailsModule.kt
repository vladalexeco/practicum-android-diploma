package ru.practicum.android.diploma.core.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.practicum.android.diploma.feature.details.data.impl.ExternalNavigatorImpl
import ru.practicum.android.diploma.feature.details.domain.api.ExternalNavigator
import ru.practicum.android.diploma.feature.details.domain.usecases.MakeCallUseCase
import ru.practicum.android.diploma.feature.details.domain.usecases.SendEmailUseCase
import ru.practicum.android.diploma.feature.details.domain.usecases.ShareVacancyUseCase
import ru.practicum.android.diploma.feature.details.presentation.viewmodels.VacancyViewModel
import ru.practicum.android.diploma.feature.favourite.domain.usecase.AddVacancyToFavouriteUseCase
import ru.practicum.android.diploma.feature.favourite.domain.usecase.RemoveVacancyFromFavouriteUseCase

val detailsModule = module {
    viewModelOf(::VacancyViewModel)
    singleOf(::ShareVacancyUseCase)
    singleOf(::MakeCallUseCase)
    singleOf(::SendEmailUseCase)
    singleOf(::AddVacancyToFavouriteUseCase)
    singleOf(::RemoveVacancyFromFavouriteUseCase)
    singleOf(::ExternalNavigatorImpl) bind ExternalNavigator::class

}