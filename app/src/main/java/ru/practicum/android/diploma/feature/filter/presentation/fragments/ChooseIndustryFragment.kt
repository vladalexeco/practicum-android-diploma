package ru.practicum.android.diploma.feature.filter.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentChooseIndustryBinding
import ru.practicum.android.diploma.feature.filter.presentation.states.IndustriesState
import ru.practicum.android.diploma.feature.filter.presentation.viewmodels.ChooseIndustryViewModel
import ru.practicum.android.diploma.feature.filter.domain.model.Industry
import ru.practicum.android.diploma.feature.filter.presentation.adapter.FilterAdapter

class ChooseIndustryFragment : Fragment() {

    private var _binding: FragmentChooseIndustryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChooseIndustryViewModel by viewModel()
    private val industriesAdapter: FilterAdapter<Industry> = FilterAdapter { industry ->
        viewModel.onIndustryClicked(industry)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseIndustryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chooseIndustryListRecycleView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = industriesAdapter
        }

        viewModel.observeIndustriesState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is IndustriesState.DisplayIndustries -> displayIndustries(state.industries)
                is IndustriesState.Error -> displayError(state.errorText)
            }
        }
    }

    private fun displayIndustries(industries: List<Industry>) {
        binding.apply {
            chooseIndustryListRecycleView.visibility = View.VISIBLE
            placeholderImage.visibility = View.GONE
        }
        industriesAdapter.apply {
            items.clear()
            industriesAdapter.items.addAll(industries)
            notifyDataSetChanged()
        }
    }

    private fun displayError(errorText: String) {
        binding.apply {
            chooseIndustryListRecycleView.visibility = View.INVISIBLE
            placeholderImage.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}