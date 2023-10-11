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
import ru.practicum.android.diploma.feature.filter.domain.model.Country
import ru.practicum.android.diploma.feature.filter.domain.model.FilterSettings
import ru.practicum.android.diploma.feature.filter.presentation.viewmodels.SettingsFiltersViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.feature.filter.domain.model.AreaPlain
import ru.practicum.android.diploma.feature.filter.domain.model.IndustryPlain

class SettingsFiltersFragment : Fragment() {

    private var _binding: FragmentSettingsFiltersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsFiltersViewModel by viewModel()

    private var settingIndustryPlain: IndustryPlain? = null
    private var settingCountry: Country? = null
    private var settingAreaPlain: AreaPlain? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var filterSettings: FilterSettings = viewModel.getFilterSettings()

        if (filterSettings.country != null && filterSettings.areaPlain != null) {
            val plainText = "${filterSettings.country!!.name}\n${filterSettings.areaPlain!!.name}"
            binding.workPlaceTextInputEditText.setText(plainText)
        } else if (filterSettings.country != null) {
            binding.workPlaceTextInputEditText.setText(filterSettings.country!!.name)
        }

        if (filterSettings.industryPlain != null) {
            binding.industryTextInputEditText.setText(filterSettings.industryPlain!!.name)
        }

        if (filterSettings.expectedSalary != -1) {
            binding.filterSettingsExpectedSalaryEditText.setText(filterSettings.expectedSalary.toString())
        } else {
            binding.filterSettingsExpectedSalaryEditText.setText("")
        }

        binding.doNotShowWithoutSalaryCheckBox.isChecked = filterSettings.notShowWithoutSalary

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {
                clearFields()
                findNavController().popBackStack(R.id.searchFragment, false)
            }

        })

        binding.settingsBackArrowImageview.setOnClickListener {
            clearFields()
            findNavController().popBackStack(R.id.searchFragment, false)
        }

        binding.resetSettingsTextview.setOnClickListener {

            viewModel.clearFilterSettings()

            binding.workPlaceTextInputEditText.setText("")
            binding.industryTextInputEditText.setText("")
            binding.filterSettingsExpectedSalaryEditText.setText("")
            binding.doNotShowWithoutSalaryCheckBox.isChecked = false

            clearFields()

        }

        binding.workPlaceTextInputEditText.setOnClickListener {
            DataTransmitter.postCountry(null)
            DataTransmitter.postAreaPlain(null)
            findNavController().navigate(R.id.action_settingsFiltersFragment_to_chooseWorkplaceFragment)
        }

        binding.industryTextInputEditText.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFiltersFragment_to_chooseIndustryFragment)
        }

        binding.confirmButton.setOnClickListener {

            val expectedSalaryRawString: String =
                binding.filterSettingsExpectedSalaryEditText.text.toString()

            val expectedSalary: Int =
                if (expectedSalaryRawString.isEmpty()) -1 else expectedSalaryRawString.toInt()

            val notShowWithoutSalary: Boolean = binding.doNotShowWithoutSalaryCheckBox.isChecked

            val oldFilterSettings = viewModel.getFilterSettings()

            val country = if (DataTransmitter.getCountry() != null) DataTransmitter.getCountry()
            else oldFilterSettings.country

            var areaPlain =
                if (DataTransmitter.getAreaPlain() != null) DataTransmitter.getAreaPlain()
                else oldFilterSettings.areaPlain

            areaPlain = if (areaPlain?.id?.isEmpty() == true) null else areaPlain

            val industryPlain =
                if (DataTransmitter.getIndustryPlain() != null) DataTransmitter.getIndustryPlain()
                else oldFilterSettings.industryPlain

            filterSettings = FilterSettings(
                country = country,
                areaPlain = areaPlain,
                industryPlain = industryPlain,
                expectedSalary = expectedSalary,
                notShowWithoutSalary = notShowWithoutSalary
            )

            viewModel.saveFilterSettings(filterSettings)

            findNavController().navigate(R.id.action_settingsFiltersFragment_to_searchFragment)

        }

        if (DataTransmitter.getIndustryPlain() != null) {
            binding.industryTextInputEditText.setText(DataTransmitter.getIndustryPlain()?.name)
            settingIndustryPlain = DataTransmitter.getIndustryPlain()
        }

        if (DataTransmitter.getCountry() != null && DataTransmitter.getAreaPlain() != null) {
            settingCountry = DataTransmitter.getCountry()
            settingAreaPlain = DataTransmitter.getAreaPlain()

            val plainText = "${settingCountry?.name}\n${settingAreaPlain?.name}"
            binding.workPlaceTextInputEditText.setText(plainText)
        }

    }

    private fun clearFields() {
        settingIndustryPlain = null
        settingCountry = null
        settingAreaPlain = null
        DataTransmitter.postIndustryPlain(null)
        DataTransmitter.postCountry(null)
        DataTransmitter.postAreaPlain(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}