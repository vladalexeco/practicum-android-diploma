package ru.practicum.android.diploma.feature.filter.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.util.DataTransmitter
import ru.practicum.android.diploma.databinding.FragmentChooseIndustryBinding
import ru.practicum.android.diploma.feature.filter.presentation.states.IndustriesState
import ru.practicum.android.diploma.feature.filter.presentation.viewmodels.ChooseIndustryViewModel
import ru.practicum.android.diploma.feature.filter.domain.model.Industry
import ru.practicum.android.diploma.feature.filter.domain.model.IndustryPlain
import ru.practicum.android.diploma.feature.filter.domain.model.mapToIndustryPlain
import ru.practicum.android.diploma.feature.filter.presentation.adapter.AreaIndustriesAdapter
import kotlin.collections.ArrayList

class ChooseIndustryFragment : Fragment() {

    private var _binding: FragmentChooseIndustryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChooseIndustryViewModel by viewModel()
    private var industriesAdapter: AreaIndustriesAdapter<Industry>? = null
    private var previousIndustryClicked: Industry? = null
    private var currentIndustryPlain: IndustryPlain? = null

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
        initObservers()
        binding.apply {
            chooseIndustryApproveButton.setOnClickListener {
                if (currentIndustryPlain != null) {
                    DataTransmitter.postIndustryPlain(currentIndustryPlain!!)
                    findNavController().navigate(R.id.action_chooseIndustryFragment_to_settingsFiltersFragment)
                }
            }
            chooseIndustryBackArrowImageview.setOnClickListener {
                findNavController().popBackStack()
            }
            chooseIndustryEnterFieldEdittext.doOnTextChanged { text, _, _, _ ->
                viewModel.onIndustryTextChanged(text.toString())
                setEditTextIcon(text.isNullOrEmpty())
            }
            clearIndustryImageView.setOnClickListener {
                binding.chooseIndustryEnterFieldEdittext.text.clear()
            }
        }
    }

    private fun initObservers() {
        viewModel.apply {
            observeIndustriesState().observe(viewLifecycleOwner) { state ->
                when (state) {
                    is IndustriesState.DisplayIndustries -> displayIndustries(ArrayList(state.industries))
                    is IndustriesState.Error -> displayError(state)
                }
            }
            dataIndustry().observe(viewLifecycleOwner) { industry ->
                if (industry.isChecked) {
                    binding.chooseIndustryApproveButton.visibility = View.VISIBLE
                    currentIndustryPlain = industry.mapToIndustryPlain()
                } else {
                    binding.chooseIndustryApproveButton.visibility = View.GONE
                    currentIndustryPlain = null
                }
            }
        }
    }

    private fun displayIndustries(industries: ArrayList<Industry>) {
        binding.apply {
            chooseIndustryListRecycleView.visibility = View.VISIBLE
            errorIndustryLayout.visibility = View.GONE
        }
        if (industriesAdapter == null) {
            initAdapter(industries)
            binding.chooseIndustryListRecycleView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = industriesAdapter
            }
        } else {
            industriesAdapter!!.apply {
                items.clear()
                items.addAll(industries)
                notifyDataSetChanged()
            }
        }
    }

    private fun initAdapter(industries: ArrayList<Industry>) {
        industriesAdapter =
            AreaIndustriesAdapter(industries) { industry, position, notifyItemChanged ->

                industriesAdapter!!.items[position].isChecked = !industry.isChecked
                notifyItemChanged.invoke()

                val previousAreaPosition = if (previousIndustryClicked != null)
                    industriesAdapter!!.items.indexOf(previousIndustryClicked) else -1
                if (previousAreaPosition != -1) {
                    industriesAdapter!!.items[previousAreaPosition].isChecked = false
                    industriesAdapter!!.notifyItemChanged(previousAreaPosition)
                }

                val areaClicked = industriesAdapter!!.items[position]
                if (previousAreaPosition != -1) previousIndustryClicked =
                    industriesAdapter!!.items[previousAreaPosition]

                viewModel.onIndustryClicked(areaClicked, previousIndustryClicked)

                previousIndustryClicked =
                    if (previousIndustryClicked != areaClicked) areaClicked else null
                binding.chooseIndustryApproveButton.visibility =
                    if (industries[position].isChecked) View.VISIBLE else View.GONE
            }
    }

    private fun displayError(state: IndustriesState.Error) {
        binding.apply {
            chooseIndustryListRecycleView.visibility = View.INVISIBLE
            errorIndustryLayout.visibility = View.VISIBLE
            industryErrorText.text = state.errorText
        }
        Glide
            .with(requireContext())
            .load(state.drawableId)
            .transform(CenterCrop())
            .into(binding.industriesErrorImage)
    }

    private fun setEditTextIcon(textIsEmpty: Boolean) {
        binding.apply {
            clearIndustryImageView.isVisible = !textIsEmpty
            searchIndustryImageView.isVisible = textIsEmpty
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}