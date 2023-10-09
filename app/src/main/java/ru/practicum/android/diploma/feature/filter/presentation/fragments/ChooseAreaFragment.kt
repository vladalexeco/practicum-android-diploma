package ru.practicum.android.diploma.feature.filter.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.util.DataTransmitter
import ru.practicum.android.diploma.databinding.FragmentChooseAreaBinding
import ru.practicum.android.diploma.feature.filter.domain.model.Area
import ru.practicum.android.diploma.feature.filter.presentation.adapter.FilterAdapter
import ru.practicum.android.diploma.feature.filter.presentation.states.AreasState
import ru.practicum.android.diploma.feature.filter.presentation.viewmodels.ChooseAreaViewModel

class ChooseAreaFragment : Fragment() {

    var currentArea: Area? = null

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

        viewModel.dataArea.observe(viewLifecycleOwner) {area ->
            if (area.isChecked) {
                binding.chooseAreaApproveButton.visibility = View.VISIBLE
                currentArea = area
            } else {
                binding.chooseAreaApproveButton.visibility = View.GONE
                currentArea = null
            }
        }

        binding.chooseAreaBackArrowImageview.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.chooseAreaApproveButton.setOnClickListener {
            if (currentArea != null) {
                DataTransmitter.postArea(currentArea)
                findNavController().navigate(R.id.action_chooseAreaFragment_to_chooseWorkplaceFragment)
            }
        }
    }

    private fun displayAreas(areas: ArrayList<Area>) {
        binding.apply {
            chooseAreaListRecycleView.visibility = View.VISIBLE
            errorAreasLayout.visibility = View.GONE
        }
        if (areasAdapter == null) {
            areasAdapter = FilterAdapter(areas) { area, position, notifyItemChanged, setPositionChecked ->
                areas[position] = (area as Area).copy(isChecked = !area.isChecked)
                viewModel.onAreaClicked(areas[position])
                notifyItemChanged.invoke()
                setPositionChecked.invoke(areas[position].isChecked)
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