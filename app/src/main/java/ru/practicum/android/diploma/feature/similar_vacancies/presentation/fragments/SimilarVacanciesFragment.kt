package ru.practicum.android.diploma.feature.similar_vacancies.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSimilarVacanciesBinding
import ru.practicum.android.diploma.feature.details.presentation.fragments.VacancyFragment
import ru.practicum.android.diploma.feature.search.domain.VacanciesResponse
import ru.practicum.android.diploma.feature.search.domain.models.VacancyShort
import ru.practicum.android.diploma.feature.similar_vacancies.presentation.SimilarSearchState
import ru.practicum.android.diploma.feature.similar_vacancies.presentation.viewmodels.SimilarVacanciesViewModel
import ru.practicum.android.diploma.feature.similar_vacancies.simillarvacanciesadapter.VacanciesAdapterCommon

class SimilarVacanciesFragment : Fragment(), VacanciesAdapterCommon.ClickListener {

    private val viewModel: SimilarVacanciesViewModel by viewModel()
    private var _binding: FragmentSimilarVacanciesBinding? = null
    private val binding get() = _binding!!

    private var similarVacanciesAdapter: VacanciesAdapterCommon? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimilarVacanciesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        similarVacanciesAdapter = VacanciesAdapterCommon(this, requireContext())
        binding.similarVacanciesRecyclerView.adapter = similarVacanciesAdapter

        binding.similarVacanciesBackArrowImageview.setOnClickListener {
            findNavController().popBackStack()
        }

        val vacancyId = requireArguments().getString(VACANCY_ID)
        viewModel.searchSimilarVacancies(vacancyId ?: "")

        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: SimilarSearchState) {
        when (state) {
            is SimilarSearchState.Loading -> showLoading()
            is SimilarSearchState.Content -> showContent(state.response)
            is SimilarSearchState.Error -> showError()
            is SimilarSearchState.Empty -> showEmpty()
            is SimilarSearchState.ServerError -> showServerError()
        }
    }

    private fun showLoading() {
        clearContent()
        binding.progressBarSimilar.visibility = View.VISIBLE
    }

    private fun showError() {
        clearContent()
        binding.internetProblemLinearlayout.visibility = View.VISIBLE
    }

    private fun showEmpty() {
        clearContent()
        binding.nothingFoundLinearlayout.visibility = View.VISIBLE
    }

    private fun showContent(response: VacanciesResponse) {
        clearContent()
        similarVacanciesAdapter?.setVacancyList(response.items)
        binding.similarVacanciesRecyclerView.visibility = View.VISIBLE
    }

    private fun showServerError() {
        clearContent()
        binding.serverNotRespondingLinearlayout.visibility = View.VISIBLE
    }

    private fun clearContent() {
        binding.apply {
            similarVacanciesRecyclerView.visibility = View.GONE
            progressBarSimilar.visibility = View.GONE
            internetProblemLinearlayout.visibility = View.GONE
            nothingFoundLinearlayout.visibility = View.GONE
            serverNotRespondingLinearlayout.visibility = View.GONE
        }
    }

    override fun onClick(vacancy: VacancyShort) {
        findNavController().navigate(
            R.id.action_similarVacanciesFragment_to_vacancyFragment,
            VacancyFragment.createArgs(vacancy.id)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        similarVacanciesAdapter = null
    }

    companion object {
        fun createArgs(vacancyId: String?) = bundleOf(VACANCY_ID to vacancyId)
        private const val VACANCY_ID = "VACANCY_ID"
    }
}