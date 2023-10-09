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
import ru.practicum.android.diploma.databinding.FragmentSettingsFiltersBinding
import ru.practicum.android.diploma.feature.filter.domain.model.Area
import ru.practicum.android.diploma.feature.filter.domain.model.Country
import ru.practicum.android.diploma.feature.filter.domain.model.Industry

class SettingsFiltersFragment : Fragment() {

    private var _binding: FragmentSettingsFiltersBinding? = null
    private val binding get() = _binding!!

    var settingIndustry: Industry? = null
    var settingCountry: Country? = null
    var settingArea: Area? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {
                DataTransmitter.postIndustry(null)
                DataTransmitter.postCountry(null)
                DataTransmitter.postArea(null)
                settingIndustry = null
                settingCountry = null
                settingArea = null
                findNavController().popBackStack(R.id.searchFragment, false)
            }

        })

        binding.settingsBackArrowImageview.setOnClickListener {
            DataTransmitter.postIndustry(null)
            DataTransmitter.postCountry(null)
            DataTransmitter.postArea(null)
            settingIndustry = null
            settingCountry = null
            settingArea = null
            findNavController().popBackStack(R.id.searchFragment, false)
        }

        binding.workPlaceTextInputEditText.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFiltersFragment_to_chooseWorkplaceFragment)
        }

        binding.industryTextInputEditText.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFiltersFragment_to_chooseIndustryFragment)
        }

        binding.confirmButton.setOnClickListener {

        }

        if (DataTransmitter.getIndustry() != null) {
            binding.industryTextInputEditText.setText(DataTransmitter.getIndustry()?.name)
            settingIndustry = DataTransmitter.getIndustry()
        }

        if (DataTransmitter.getCountry() != null && DataTransmitter.getArea() != null) {
            settingCountry = DataTransmitter.getCountry()
            settingArea = DataTransmitter.getArea()

            val plainText = "${settingCountry?.name}\n${settingArea?.name}"
            binding.workPlaceTextInputEditText.setText(plainText)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}