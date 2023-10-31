package ru.practicum.android.diploma.feature.search.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.feature.search.presentation.viewmodels.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.feature.details.presentation.fragments.VacancyFragment
import ru.practicum.android.diploma.feature.search.domain.VacanciesResponse
import ru.practicum.android.diploma.feature.search.domain.models.VacancyShort
import ru.practicum.android.diploma.feature.search.searchadapter.SlideInBottomAnimator
import ru.practicum.android.diploma.feature.search.presentation.VacanciesSearchState
import ru.practicum.android.diploma.feature.search.searchadapter.VacanciesAdapter

class SearchFragment : Fragment(), VacanciesAdapter.ClickListener {

    private val viewModel: SearchViewModel by viewModel()
    private var _binding: FragmentSearchBinding? = null
    private var vacanciesAdapter: VacanciesAdapter? = null
    private val binding get() = _binding!!
    private var lastVisibleItemPosition: Int = 0

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

            if (!viewModel.isLoading) {
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && !viewModel.isLastPage()
                ) {
                    viewModel.loadNextPage()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.filterButtonImageView.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_settingsFiltersFragment)
        }

        vacanciesAdapter = VacanciesAdapter(this, requireContext())
        binding.searchRecycler.adapter = vacanciesAdapter

        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            render(it)
            showVacanciesNumber(it)
        }

        binding.searchInputEditText.doOnTextChanged { text, _, _, _ ->
            vacanciesAdapter?.clear()
            clearButtonVisibility(text)
            if (text?.isEmpty() == true) {
                viewModel.clearList()
            }
            text?.let {
                viewModel.searchDebounce(it.toString())
                binding.apply {
                    amountTextView.visibility = View.GONE
                    searchPlaceholderImageView.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
            }
        }

        binding.clearSearchImageView.setOnClickListener {
            clearSearch()
            viewModel.vacanciesList.clear()
        }

        binding.searchRecycler.addOnScrollListener(onScrollListener)
        renderFilter()
    }

    private fun render(state: VacanciesSearchState) {
        when (state) {
            is VacanciesSearchState.Loading -> showLoading()
            is VacanciesSearchState.Content -> showContent(state.response)
            is VacanciesSearchState.Error -> showError()
            is VacanciesSearchState.Empty -> showEmpty()
            is VacanciesSearchState.ClearScreen -> showClearScreen()
            is VacanciesSearchState.ServerError -> showServerError()
        }
    }

    private fun showLoading() {
        if (viewModel.isFirstLoad) {
            clearContent()
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showError() {
        clearContent()
        binding.internetProblemLinearlayout.visibility = View.VISIBLE
    }

    private fun showEmpty() {
        clearContent()
        binding.apply {
            amountTextView.visibility = View.VISIBLE
            nothingFoundLinearlayout.visibility = View.VISIBLE
            amountTextView.text = getString(R.string.search_message_no_vacancies)
        }
    }

    private fun showContent(response: VacanciesResponse) {
        clearContent()
        binding.amountTextView.visibility = View.VISIBLE
        binding.amountTextView.text = response.found.toString()
        vacanciesAdapter?.setVacancyList(viewModel.vacanciesList.toList())
        binding.searchRecycler.visibility = View.VISIBLE
        val itemAnimator = SlideInBottomAnimator()
        binding.searchRecycler.itemAnimator = itemAnimator
    }

    private fun showClearScreen() {
        clearContent()
        binding.amountTextView.visibility = View.GONE
        binding.searchPlaceholderImageView.visibility = View.VISIBLE
    }

    private fun showServerError() {
        clearContent()
        binding.serverNotRespondingLinearlayout.visibility = View.VISIBLE
    }

    private fun clearContent() {
        binding.apply {
            searchPlaceholderImageView.visibility = View.GONE
            searchRecycler.visibility = View.GONE
            progressBar.visibility = View.GONE
            internetProblemLinearlayout.visibility = View.GONE
            nothingFoundLinearlayout.visibility = View.GONE
            serverNotRespondingLinearlayout.visibility = View.GONE
            amountTextView.visibility = View.GONE
        }
    }

    private fun clearButtonVisibility(s: CharSequence?) {
        if (s.isNullOrEmpty()) {
            viewModel.showClearScreen()
            binding.searchImageView.visibility = View.VISIBLE
            binding.clearSearchImageView.visibility = View.GONE
        } else {
            binding.searchImageView.visibility = View.GONE
            binding.clearSearchImageView.visibility = View.VISIBLE
        }
    }

    private fun clearSearch() {
        binding.searchInputEditText.setText("")
        binding.searchInputEditText.clearFocus()
        clearContent()
        binding.searchPlaceholderImageView.visibility = View.VISIBLE
        viewModel.currentPage = 0
        viewModel.totalPages = 0
    }

    private fun showVacanciesNumber(vacanciesSearchState: VacanciesSearchState) {
        if (vacanciesSearchState is VacanciesSearchState.Content) {
            binding.amountTextView.text = requireContext().resources.getQuantityString(
                R.plurals.plural_vacancies,
                vacanciesSearchState.response.found,
                vacanciesSearchState.response.found
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        vacanciesAdapter = null
    }

    override fun onClick(vacancy: VacancyShort) {
        findNavController().navigate(
            R.id.action_searchFragment_to_vacancyFragment,
            VacancyFragment.createArgs(vacancy.id)
        )
    }

    private fun renderFilter() {
        val filters = viewModel.getFilters()
        if (
            filters.country != null
            || filters.industryPlain != null
            || filters.areaPlain != null
            || filters.notShowWithoutSalary
            || filters.expectedSalary != NEGATIVE_BALANCE
        )
            binding.filterButtonImageView.setImageResource(R.drawable.ic_filter_on)
    }

    override fun onResume() {
        super.onResume()
        viewModel.doNewSearch(binding.searchInputEditText.text.toString())
    }

    companion object {
        const val NEGATIVE_BALANCE = -1
    }
}