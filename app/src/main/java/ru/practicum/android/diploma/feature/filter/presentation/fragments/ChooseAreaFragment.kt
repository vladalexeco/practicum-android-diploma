package ru.practicum.android.diploma.feature.filter.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.core.util.DataTransmitter
import ru.practicum.android.diploma.databinding.FragmentChooseAreaBinding
import ru.practicum.android.diploma.feature.filter.domain.model.Area
import ru.practicum.android.diploma.feature.filter.domain.model.AreaPlain
import ru.practicum.android.diploma.feature.filter.domain.model.mapToAreaPlain
import ru.practicum.android.diploma.feature.filter.presentation.adapter.FilterAdapter
import ru.practicum.android.diploma.feature.filter.presentation.states.AreasState
import ru.practicum.android.diploma.feature.filter.presentation.viewmodels.ChooseAreaViewModel

class ChooseAreaFragment : Fragment() {

    private var currentAreaPlain: AreaPlain? = null

    private var _binding: FragmentChooseAreaBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChooseAreaViewModel by viewModel()
    private var areasAdapter: FilterAdapter<Area>? = null
    private var previousAreaClicked: Area? = null

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
                is AreasState.DisplayAreas -> displayAreas(ArrayList(state.areas))
                is AreasState.Error -> displayError(state)
                else -> {}
            }
        }

        viewModel.dataArea.observe(viewLifecycleOwner) { area ->
            if (area.isChecked) {
                binding.chooseAreaApproveButton.visibility = View.VISIBLE
                currentAreaPlain = area.mapToAreaPlain()
            } else {
                binding.chooseAreaApproveButton.visibility = View.GONE
                currentAreaPlain = null
            }
        }

        binding.chooseAreaBackArrowImageview.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.chooseAreaApproveButton.setOnClickListener {
            if (currentAreaPlain != null) {
                DataTransmitter.postAreaPlain(currentAreaPlain)
                //findNavController().navigate(R.id.action_chooseAreaFragment_to_chooseWorkplaceFragment)
                findNavController().popBackStack()
            }
        }

        binding.chooseAreaEnterFieldEdittext.doOnTextChanged { text, _, _, _ ->
            viewModel.onAreaTextChanged(text.toString())
        }
    }

    private fun displayAreas(areas: ArrayList<Area>) {
        binding.apply {
            chooseAreaListRecycleView.visibility = View.VISIBLE
            errorAreasLayout.visibility = View.GONE
        }
        if (areasAdapter == null) {
            initAdapter(areas)
            binding.chooseAreaListRecycleView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = areasAdapter
            }
        } else {
            areasAdapter!!.apply {
                items.clear()
                items.addAll(areas)
                notifyDataSetChanged()
            }
        }
    }

    private fun initAdapter(areas: ArrayList<Area>) {
        areasAdapter = FilterAdapter(areas) { area, position, notifyItemChanged ->

            areasAdapter!!.items[position].isChecked = !area.isChecked
            notifyItemChanged.invoke()

            val previousAreaPosition = if (previousAreaClicked != null)
                areasAdapter!!.items.indexOf(previousAreaClicked) else -1
            if (previousAreaPosition != -1) {
                areasAdapter!!.items[previousAreaPosition].isChecked = false
                areasAdapter!!.notifyItemChanged(previousAreaPosition)
            }

            val areaClicked = areasAdapter!!.items[position]
            if (previousAreaPosition != -1) previousAreaClicked =
                areasAdapter!!.items[previousAreaPosition]

            viewModel.onAreaClicked(areaClicked, previousAreaClicked)

            previousAreaClicked =
                if (previousAreaClicked != areaClicked) areaClicked else null
            binding.chooseAreaApproveButton.visibility =
                if (areas[position].isChecked) View.VISIBLE else View.GONE
        }
    }

    private fun displayError(state: AreasState.Error) {
        binding.apply {
            chooseAreaListRecycleView.visibility = View.INVISIBLE
            errorAreasLayout.visibility = View.VISIBLE
            areasErrorText.text = state.errorText
        }
        Glide
            .with(requireContext())
            .load(state.drawableId)
            .transform(CenterCrop())
            .into(binding.areasErrorImage)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}