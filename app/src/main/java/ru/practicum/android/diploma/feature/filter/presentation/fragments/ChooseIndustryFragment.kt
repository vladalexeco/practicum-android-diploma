package ru.practicum.android.diploma.feature.filter.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.util.DataTransmitter
import ru.practicum.android.diploma.databinding.FragmentChooseIndustryBinding
import ru.practicum.android.diploma.feature.filter.presentation.states.IndustriesState
import ru.practicum.android.diploma.feature.filter.presentation.viewmodels.ChooseIndustryViewModel
import ru.practicum.android.diploma.feature.filter.domain.model.Industry
import ru.practicum.android.diploma.feature.filter.presentation.adapter.FilterAdapter
import kotlin.collections.ArrayList

class ChooseIndustryFragment : Fragment() {

    private var _binding: FragmentChooseIndustryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChooseIndustryViewModel by viewModel()
    private var industriesAdapter: FilterAdapter<Industry>? = null

    var currentIndustry: Industry? = null

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

        viewModel.observeIndustriesState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is IndustriesState.DisplayIndustries -> displayIndustries(state.industries)
                is IndustriesState.Error -> displayError(state.errorText)
            }
        }

        viewModel.dataIndustry.observe(viewLifecycleOwner) { industry ->
            if (industry.isChecked) {
                binding.chooseIndustryApproveButton.visibility = View.VISIBLE
                currentIndustry = industry
            } else {
                binding.chooseIndustryApproveButton.visibility = View.GONE
                currentIndustry = null
            }
        }

        binding.chooseIndustryApproveButton.setOnClickListener {
            if (currentIndustry != null) {
                DataTransmitter.postIndustry(currentIndustry!!)
                findNavController().navigate(
                    R.id.action_chooseIndustryFragment_to_settingsFiltersFragment
                )
            }
        }

        binding.chooseIndustryBackArrowImageview.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun displayIndustries(industries: ArrayList<Industry>) {
        binding.apply {
            chooseIndustryListRecycleView.visibility = View.VISIBLE
            errorIndustryLayout.visibility = View.GONE
        }
        if (industriesAdapter == null) {
            industriesAdapter =
                FilterAdapter(industries) { industry, position, notifyItemChanged, setPositionChecked ->
                    industries[position] = (industry as Industry).copy(isChecked = !industry.isChecked)
                    viewModel.onIndustryClicked(industries[position])
                    notifyItemChanged.invoke()
                    setPositionChecked.invoke(industries[position].isChecked)
                }
            binding.chooseIndustryListRecycleView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = industriesAdapter
            }
        } else {
            //todo
        }
    }

    private fun displayError(errorText: String) {
        binding.apply {
            chooseIndustryListRecycleView.visibility = View.INVISIBLE
            errorIndustryLayout.visibility = View.VISIBLE
            industryErrorText.text = errorText
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}