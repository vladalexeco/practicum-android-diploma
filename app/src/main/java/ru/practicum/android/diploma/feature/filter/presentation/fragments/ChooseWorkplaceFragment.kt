package ru.practicum.android.diploma.feature.filter.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.util.DataTransmitter
import ru.practicum.android.diploma.databinding.FragmentChooseWorkplaceBinding
import ru.practicum.android.diploma.feature.filter.domain.model.AreaPlain
import ru.practicum.android.diploma.feature.filter.presentation.viewmodels.ChooseWorkPlaceViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.feature.filter.domain.model.Country

class ChooseWorkplaceFragment : Fragment() {

    private var _binding: FragmentChooseWorkplaceBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChooseWorkPlaceViewModel by viewModel()

    private var hasRegions = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseWorkplaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.settingsFiltersFragment, false)
                DataTransmitter.postCountry(null)
                DataTransmitter.postAreaPlain(null)
            }

        })

        viewModel.initScreenData()

        viewModel.hasRegionsLiveData.observe(viewLifecycleOwner) {
            hasRegions = it
        }

        viewModel.dataCountry.observe(viewLifecycleOwner) {
            binding.chooseCountryTextInputEditText.setText(it)
            renderCountryTextInputLayout(it)
        }

        viewModel.dataArea.observe(viewLifecycleOwner) {
            binding.areaTextInputEditText.setText(it)
            renderAreaTextInputLayout(it)
        }

        binding.chooseWorkplaceBackArrowImageview.setOnClickListener {
            findNavController().popBackStack(R.id.settingsFiltersFragment, false)
            DataTransmitter.postCountry(null)
            DataTransmitter.postAreaPlain(null)
        }

        binding.chooseCountryTextInputEditText.setOnClickListener {
            findNavController().navigate(R.id.action_chooseWorkplaceFragment_to_chooseCountryFragment)
        }

        binding.areaTextInputEditText.setOnClickListener {
            findNavController().navigate(R.id.action_chooseWorkplaceFragment_to_chooseRegionFragment)
        }


        binding.countryArrowForward.setOnClickListener {
            findNavController().navigate(R.id.action_chooseWorkplaceFragment_to_chooseCountryFragment)
        }

        binding.areaArrowForward.setOnClickListener {
            findNavController().navigate(R.id.action_chooseWorkplaceFragment_to_chooseRegionFragment)
        }

        binding.chooseButton.setOnClickListener {
            if (binding.chooseCountryTextInputEditText.text?.isNotEmpty() == true &&
                binding.areaTextInputEditText.text?.isEmpty() == true
            ) {

                DataTransmitter.postAreaPlain(AreaPlain(id = "", name = ""))
                findNavController().navigate(R.id.action_chooseWorkplaceFragment_to_settingsFiltersFragment)

            } else if (binding.chooseCountryTextInputEditText.text?.isNotEmpty() == true &&

                binding.areaTextInputEditText.text?.isNotEmpty() == true
            ) {
                findNavController().navigate(R.id.action_chooseWorkplaceFragment_to_settingsFiltersFragment)

            } else if (binding.chooseCountryTextInputEditText.text?.isEmpty() == true &&
                binding.areaTextInputEditText.text?.isNotEmpty() == true
            ) {
                DataTransmitter.postCountry(Country(id = "", name = ""))
                findNavController().navigate(R.id.action_chooseWorkplaceFragment_to_settingsFiltersFragment)
            }
        }

        if (DataTransmitter.getCountry() != null) {
            binding.chooseCountryTextInputEditText.setText(DataTransmitter.getCountry()?.name)
        }

        if (DataTransmitter.getAreaPlain() != null) {
            binding.areaTextInputEditText.setText(DataTransmitter.getAreaPlain()?.name)
        }

        if (binding.chooseCountryTextInputEditText.text!!.isNotEmpty()) {
            viewModel.checkCountryHasRegions()
        }

        binding.countryClear.setOnClickListener {
            viewModel.onCountryCleared()
        }

        binding.areaClear.setOnClickListener {
            viewModel.onAreaCleared()
        }
    }

    private fun renderAreaTextInputLayout(areaName: String) {
        if (areaName.isNotEmpty()) {
            binding.apply {
                areaTextInputLayout.defaultHintTextColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.black_to_white_color)
                areaClear.visibility = View.VISIBLE
                areaArrowForward.visibility = View.GONE
            }
        } else {
            binding.apply {
                areaTextInputLayout.defaultHintTextColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.gray_color)
                areaClear.visibility = View.GONE
                areaArrowForward.visibility = View.VISIBLE
            }
        }
    }

    private fun renderCountryTextInputLayout(countryName: String) {
        if (countryName.isNotEmpty()) {
            binding.apply {
                chooseCountryTextInputLayout.defaultHintTextColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.black_to_white_color)
                countryClear.visibility = View.VISIBLE
                countryArrowForward.visibility = View.GONE
            }
        } else {
            binding.apply {
                chooseCountryTextInputLayout.defaultHintTextColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.gray_color)
                countryClear.visibility = View.GONE
                countryArrowForward.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}