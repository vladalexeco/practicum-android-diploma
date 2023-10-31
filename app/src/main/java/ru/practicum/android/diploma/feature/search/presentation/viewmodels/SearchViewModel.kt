package ru.practicum.android.diploma.feature.search.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.util.IsLastPage
import ru.practicum.android.diploma.core.util.STATUS_CODE_NO_NETWORK_CONNECTION
import ru.practicum.android.diploma.core.util.STATUS_CODE_SERVER_ERROR
import ru.practicum.android.diploma.core.util.debounce
import ru.practicum.android.diploma.feature.filter.domain.model.FilterSettings
import ru.practicum.android.diploma.feature.filter.domain.usecase.GetFilterSettingsUseCase
import ru.practicum.android.diploma.feature.search.domain.GetVacanciesUseCase
import ru.practicum.android.diploma.feature.search.domain.models.VacancyShort
import ru.practicum.android.diploma.feature.search.presentation.VacanciesSearchState

class SearchViewModel(
    private val getVacanciesUseCase: GetVacanciesUseCase,
    private val filter: GetFilterSettingsUseCase
) : ViewModel() {

    var currentPage = 0
    var totalPages = 0
    var isLoading = false
    var isFirstLoad = true
    var vacanciesList: MutableSet<VacancyShort> = mutableSetOf()

    private var latestSearchText: String? = null

    private val _stateLiveData = MutableLiveData<VacanciesSearchState>()
    val stateLiveData: LiveData<VacanciesSearchState> = _stateLiveData

    private val vacanciesSearchDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY_MILLIS,
        viewModelScope,
        true
    ) { searchText ->
        searchRequest(searchText, totalPages, 20, currentPage)
    }

    private var filters: FilterSettings? = null

    init {
        renderState(VacanciesSearchState.ClearScreen)
    }

    fun clearList() {
        vacanciesList.clear()
    }

    private fun searchRequest(newSearchText: String, pages: Int, perPage: Int, page: Int) {
        if (newSearchText.isNotEmpty()) {

            isLoading = true
            renderState(VacanciesSearchState.Loading)
            viewModelScope.launch {
                getVacanciesUseCase
                    .getVacancies(newSearchText, pages, perPage, page, filter.invoke())
                    .collect { pair ->
                        when {
                            pair.second == STATUS_CODE_NO_NETWORK_CONNECTION -> renderState(
                                VacanciesSearchState.Error
                            )

                            pair.second == STATUS_CODE_SERVER_ERROR -> renderState(
                                VacanciesSearchState.ServerError
                            )

                            pair.first?.items.isNullOrEmpty() -> renderState(VacanciesSearchState.Empty)
                            else -> {
                                currentPage = page
                                totalPages = pair.first!!.pages
                                renderState(
                                    VacanciesSearchState.Content(
                                        response = pair.first!!,
                                    )
                                )
                                vacanciesList.addAll(pair.first!!.items)
                                renderState(VacanciesSearchState.Content(response = pair.first!!))
                                isLoading = false

                                IsLastPage.IS_LAST_PAGE = currentPage < totalPages
                            }
                        }
                    }
                filters = filter.invoke()
            }
        }
    }

    fun loadNextPage() {
        if (!isLastPage()) {
            IsLastPage.IS_LAST_PAGE = false
            val nextPage = currentPage + 1
            isFirstLoad = false
            isLoading = true
            searchRequest(latestSearchText!!, totalPages, perPage = PAGE_SIZE, nextPage)
        }
    }

    fun isLastPage(): Boolean {
        IsLastPage.IS_LAST_PAGE = true
        isLoading = false
        return currentPage == totalPages - 1
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText != changedText) {
            latestSearchText = changedText
            vacanciesSearchDebounce(changedText)
        }
    }

    private fun renderState(state: VacanciesSearchState) {
        _stateLiveData.postValue(state)
    }

    fun showClearScreen() {
        renderState(VacanciesSearchState.ClearScreen)
    }

    fun getFilters(): FilterSettings {
        return filter.invoke()
    }

    fun doNewSearch(request: String?) {
        if (filters != filter.invoke() && request != null) {
            vacanciesList.clear()
            searchRequest(request, 0, 20, 0)
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private const val PAGE_SIZE = 20
    }
}