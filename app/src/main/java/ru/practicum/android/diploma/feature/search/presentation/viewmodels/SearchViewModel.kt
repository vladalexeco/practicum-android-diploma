package ru.practicum.android.diploma.feature.search.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.util.debounce
import ru.practicum.android.diploma.feature.search.domain.GetVacanciesUseCase
import ru.practicum.android.diploma.feature.search.presentation.SearchState

class SearchViewModel(private val getVacanciesUseCase: GetVacanciesUseCase): ViewModel() {

    private var latestSearchText: String? = null

    private val _stateLiveData = MutableLiveData<SearchState>()
    val stateLiveData : LiveData<SearchState> = _stateLiveData

    private val vacanciesSearchDebounce = debounce<String>(SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true) {
        searchRequest(it)
    }
    init {
        renderState(SearchState.ClearScreen())
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(SearchState.Loading)

            viewModelScope.launch {
                getVacanciesUseCase
                    .getVacancies(newSearchText)
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
                                renderState(
                                    SearchState.Content(
                                        response = pair.first!!,
                                    )
                                )
                            }
                        }
                    }
            }
        }
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

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}