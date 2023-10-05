package ru.practicum.android.diploma.feature.search.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.feature.search.presentation.viewmodels.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.feature.search.domain.VacanciesResponse
import ru.practicum.android.diploma.feature.search.domain.models.VacancyShort
import ru.practicum.android.diploma.feature.search.presentation.SearchState
import ru.practicum.android.diploma.feature.search.searchadapter.VacanciesAdapter

class SearchFragment : Fragment(), VacanciesAdapter.ClickListener {

    private val viewModel: SearchViewModel by viewModel()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var vacanciesAdapter: VacanciesAdapter? = null

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

        vacanciesAdapter = VacanciesAdapter(this)
        binding.searchRecycler.adapter = vacanciesAdapter

        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            render(it)
        }

        binding.searchInputEditText.doOnTextChanged { text, _, _, _ ->
            text?.let { viewModel.searchDebounce(it.toString()) }
        }


    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showLoading()
            is SearchState.Content -> showContent(state.response)
            is SearchState.Error -> showError()
            is SearchState.Empty -> showEmpty()
            is SearchState.ClearScreen -> showClearScreen()
        }
    }

    private fun showLoading() {
        clearContent()
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showError() {
        clearContent()
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showEmpty() {
        clearContent()
        binding.amountTextView.text = ""
        binding.searchPlaceholderImageView.visibility = View.VISIBLE
    }

    private fun showContent(response: VacanciesResponse) {
        clearContent()
        binding.amountTextView.text = response.found.toString()
        vacanciesAdapter?.vacancies = response.items
        binding.searchRecycler.visibility = View.VISIBLE
        Log.d("HEADHUNTER", response.items.toString())
    }

    private fun showClearScreen() {
        clearContent()
    }

    private fun clearContent() {
        binding.searchPlaceholderImageView.visibility = View.GONE
        binding.searchRecycler.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(track: VacancyShort) {
        Toast.makeText(requireContext(), "CLICKED!!!", Toast.LENGTH_SHORT).show()
    }

}