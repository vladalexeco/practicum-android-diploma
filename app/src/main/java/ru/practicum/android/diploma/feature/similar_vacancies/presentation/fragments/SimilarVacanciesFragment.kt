package ru.practicum.android.diploma.feature.similar_vacancies.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.util.DataTransmitter
import ru.practicum.android.diploma.databinding.FragmentSimilarVacanciesBinding
import ru.practicum.android.diploma.feature.search.domain.VacanciesResponse
import ru.practicum.android.diploma.feature.search.domain.models.VacancyShort
import ru.practicum.android.diploma.feature.search.presentation.SearchState
import ru.practicum.android.diploma.feature.search.presentation.viewmodels.SearchViewModel
import ru.practicum.android.diploma.feature.search.searchadapter.VacanciesAdapter
import ru.practicum.android.diploma.feature.similar_vacancies.presentation.viewmodels.SimilarVacanciesViewModel

class SimilarVacanciesFragment : Fragment(), VacanciesAdapter.ClickListener {

    private val viewModel: SimilarVacanciesViewModel by viewModel()

    private var _binding: FragmentSimilarVacanciesBinding? = null
    private val binding get() = _binding!!

    private var similarVacanciesAdapter: VacanciesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimilarVacanciesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        similarVacanciesAdapter = VacanciesAdapter(this)
        binding.similarVacanciesRecyclerView.adapter = similarVacanciesAdapter

        binding.similarVacanciesBackArrowImageview.setOnClickListener {
            findNavController().popBackStack(R.id.similarVacanciesFragment, false)
        }

        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            render(it)
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
        binding.progressBarSimilar.visibility = View.VISIBLE
    }

    private fun showError() {
        clearContent()

    }

    private fun showEmpty() {
        clearContent()

    }

    private fun showContent(response: VacanciesResponse) {
        clearContent()
        similarVacanciesAdapter?.vacancies = response.items
        binding.similarVacanciesRecyclerView.visibility = View.VISIBLE
        binding.similarVacanciesRecyclerView.visibility = View.VISIBLE
    }

    private fun showClearScreen() {
        clearContent()

    }

    private fun clearContent() {
        binding.similarVacanciesRecyclerView.visibility = View.GONE
        binding.progressBarSimilar.visibility = View.GONE
    }

    override fun onClick(vacancy: VacancyShort) {
        DataTransmitter.postId(vacancy.id)
        findNavController().navigate(R.id.action_similarVacanciesFragment_to_vacancyFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        similarVacanciesAdapter = null
    }
}