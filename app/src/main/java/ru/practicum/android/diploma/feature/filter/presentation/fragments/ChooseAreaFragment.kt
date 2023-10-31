package ru.practicum.android.diploma.feature.filter.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.util.DataTransmitter
import ru.practicum.android.diploma.databinding.FragmentChooseAreaBinding
import ru.practicum.android.diploma.feature.filter.domain.model.Area
import ru.practicum.android.diploma.feature.filter.domain.model.AreaPlain
import ru.practicum.android.diploma.feature.filter.domain.model.mapToAreaPlain
import ru.practicum.android.diploma.feature.filter.presentation.adapter.IndustriesAreasAdapter
import ru.practicum.android.diploma.feature.filter.presentation.states.AreasState
import ru.practicum.android.diploma.feature.filter.presentation.states.LiveDataResource
import ru.practicum.android.diploma.feature.filter.presentation.viewmodels.ChooseAreaViewModel

class ChooseAreaFragment : Fragment() {

    private var currentAreaPlain: AreaPlain? = null
    private var _binding: FragmentChooseAreaBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChooseAreaViewModel by viewModel()
    private var areasAdapter: IndustriesAreasAdapter<Area>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseAreaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        binding.apply {
            chooseAreaBackArrowImageview.setOnClickListener {
                findNavController().popBackStack()
            }
            chooseAreaApproveButton.setOnClickListener {
                if (currentAreaPlain != null) {
                    DataTransmitter.postAreaPlain(currentAreaPlain)
                    findNavController().popBackStack()
                }
            }
            chooseAreaEnterFieldEdittext.doOnTextChanged { text, _, _, _ ->
                viewModel.onAreaTextChanged(text.toString())
                setEditTextIcon(text.isNullOrEmpty())
            }
            clearAreaImageView.setOnClickListener {
                binding.chooseAreaEnterFieldEdittext.text.clear()
            }
        }
    }

    private fun initObservers() {
        viewModel.apply {
            areaData.observe(viewLifecycleOwner) { liveDataResource ->
                when(liveDataResource) {
                    is LiveDataResource.AreaStorage -> {
                        val area: Area = liveDataResource.data
                        if (area.isChecked) {
                            binding.chooseAreaApproveButton.visibility = View.VISIBLE
                            currentAreaPlain = area.mapToAreaPlain()
                        } else {
                            binding.chooseAreaApproveButton.visibility = View.GONE
                            currentAreaPlain = null
                        }
                    }

                    is LiveDataResource.AreasStateStorage -> {
                        when (val state: AreasState = liveDataResource.data) {
                            is AreasState.DisplayAreas -> displayAreas(ArrayList(state.areas))
                            is AreasState.Error -> displayError(state)
                        }
                    }

                    else -> throw Throwable(getString(R.string.bad_inheritor_error))
                }
            }
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
        areasAdapter = IndustriesAreasAdapter(areas) { area, position, notifyItemChanged ->

            areasAdapter!!.items[position].isChecked = !area.isChecked
            notifyItemChanged.invoke()
            val areaClicked = areasAdapter!!.items[position]

            viewModel.onAreaClicked(position, areaClicked) { previousAreaPositionClicked: Int ->
                areasAdapter!!.items[previousAreaPositionClicked].isChecked = false
                areasAdapter!!.notifyItemChanged(
                    previousAreaPositionClicked
                )
            }
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

    private fun setEditTextIcon(textIsEmpty: Boolean) {
        binding.apply {
            clearAreaImageView.isVisible = !textIsEmpty
            searchAreaImageView.isVisible = textIsEmpty
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}