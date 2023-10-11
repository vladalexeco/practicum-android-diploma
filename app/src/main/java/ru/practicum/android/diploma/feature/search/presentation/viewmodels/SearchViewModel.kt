package ru.practicum.android.diploma.feature.search.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.util.debounce
import ru.practicum.android.diploma.feature.search.domain.GetVacanciesUseCase
import ru.practicum.android.diploma.feature.search.presentation.SearchState

class SearchViewModel(private val getVacanciesUseCase: GetVacanciesUseCase) : ViewModel() {

    private var currentPage = 0
    private var totalPages = 0
    var isLoading = false
    var isFirstLoad = true

    private var latestSearchText: String? = null

    private val _stateLiveData = MutableLiveData<SearchState>()
    val stateLiveData: LiveData<SearchState> = _stateLiveData

    private val vacanciesSearchDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY_MILLIS,
        viewModelScope,
        true
    ) { searchText ->
        searchRequest(searchText, totalPages, PAGE_SIZE, currentPage)
    }

    init {
        renderState(SearchState.ClearScreen())
    }

    private fun searchRequest(newSearchText: String, pages: Int, perPage: Int, page: Int) {
        if (newSearchText.isNotEmpty()) {
            renderState(SearchState.Loading)
            isLoading = true
            viewModelScope.launch {
                getVacanciesUseCase
                    .getVacancies(newSearchText, pages, perPage, page)
                    .collect { pair ->
                        when {
                            pair.second != null -> {
                                renderState(
                                    SearchState.Error()
                                )
                            }

                            pair.first?.items.isNullOrEmpty() -> {
                                renderState(
                                    SearchState.Empty()
                                )
                            }

                            else -> {
                                currentPage = page
                                totalPages = pair.first!!.pages
                                renderState(
                                    SearchState.Content(
                                        response = pair.first!!,
                                    )
                                )
                                isLoading = false

                            }
                        }
                    }
            }
        }
    }

    fun loadNextPage() {
        if (!isLastPage()) {
            val nextPage = currentPage + 1
            isFirstLoad = false
            isLoading = true
            searchRequest(latestSearchText!!, totalPages, perPage = 20, nextPage)
        }
    }

    fun isLastPage(): Boolean {
        return currentPage == totalPages
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText != changedText) {
            latestSearchText = changedText
            vacanciesSearchDebounce(changedText)
        }
    }

    private fun renderState(state: SearchState) {
        _stateLiveData.postValue(state)
    }

    fun showClearScreen() {
        renderState(SearchState.ClearScreen())
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private const val PAGE_SIZE = 20
    }
}