package ru.practicum.android.diploma.feature.filter.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentChooseAreaBinding
import ru.practicum.android.diploma.feature.filter.domain.model.Area
import ru.practicum.android.diploma.feature.filter.presentation.adapter.FilterAdapter
import ru.practicum.android.diploma.feature.filter.presentation.states.AreasState
import ru.practicum.android.diploma.feature.filter.presentation.viewmodels.ChooseAreaViewModel

class ChooseAreaFragment : Fragment() {

    private var _binding: FragmentChooseAreaBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChooseAreaViewModel by viewModel()
    private var areasAdapter: FilterAdapter<Area>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseAreaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeAreasState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is AreasState.DisplayAreas -> displayAreas(state.areas)
                is AreasState.Error -> displayError(state.errorText)
            }
        }
    }

    private fun displayAreas(areas: ArrayList<Area>) {
        binding.apply {
            chooseAreaListRecycleView.visibility = View.VISIBLE
            errorAreasLayout.visibility = View.GONE
        }
        if (areasAdapter == null) {
            areasAdapter = FilterAdapter(areas) { area ->
                viewModel.onAreaClicked(area as Area)
            }
            binding.chooseAreaListRecycleView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = areasAdapter
            }
        } else {
            //todo
        }
    }

    private fun displayError(errorText: String) {
        binding.apply {
            chooseAreaListRecycleView.visibility = View.INVISIBLE
            errorAreasLayout.visibility = View.VISIBLE
            areasErrorText.text = errorText
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}