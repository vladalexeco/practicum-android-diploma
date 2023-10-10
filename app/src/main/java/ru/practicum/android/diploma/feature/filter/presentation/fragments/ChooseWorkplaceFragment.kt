package ru.practicum.android.diploma.feature.filter.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.util.DataTransmitter
import ru.practicum.android.diploma.databinding.FragmentChooseWorkplaceBinding
import ru.practicum.android.diploma.feature.filter.domain.model.Area
import ru.practicum.android.diploma.feature.filter.domain.model.AreaPlain
import ru.practicum.android.diploma.feature.filter.domain.model.Country

class ChooseWorkplaceFragment : Fragment() {

    private var _binding: FragmentChooseWorkplaceBinding? = null
    private val binding get() = _binding!!

    var currentCountry: Country? = null
    var currentAreaPlain: AreaPlain? = null

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
                currentCountry = null
                currentAreaPlain = null
            }

        })

        binding.chooseWorkplaceBackArrowImageview.setOnClickListener {
            findNavController().popBackStack(R.id.settingsFiltersFragment, false)
            DataTransmitter.postCountry(null)
            DataTransmitter.postAreaPlain(null)
            currentCountry = null
            currentAreaPlain = null
        }

        binding.chooseCountryTextInputEditText.setOnClickListener {
            findNavController().navigate(R.id.action_chooseWorkplaceFragment_to_chooseCountryFragment)
        }

        binding.regionTextInputEditText.setOnClickListener {
            if (currentCountry != null) {
                DataTransmitter.postCountryId(currentCountry!!.id)
                findNavController().navigate(R.id.action_chooseWorkplaceFragment_to_chooseRegionFragment)
            }
        }

        binding.chooseButton.setOnClickListener {
            if (binding.chooseCountryTextInputEditText.text?.isNotEmpty() == true ||
                binding.regionTextInputEditText.text?.isNotEmpty() == true) {
                findNavController().navigate(R.id.action_chooseWorkplaceFragment_to_settingsFiltersFragment)
            }
        }

        if (DataTransmitter.getCountry() != null) {
            binding.chooseCountryTextInputEditText.setText(DataTransmitter.getCountry()?.name)
            currentCountry = DataTransmitter.getCountry()
        }

        if (DataTransmitter.getAreaPlain() != null) {
            binding.regionTextInputEditText.setText(DataTransmitter.getAreaPlain()?.name)
            currentAreaPlain = DataTransmitter.getAreaPlain()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}