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
import ru.practicum.android.diploma.databinding.FragmentSettingsFiltersBinding
import ru.practicum.android.diploma.feature.filter.domain.model.FilterSettings
import ru.practicum.android.diploma.feature.filter.presentation.viewmodels.SettingsFiltersViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.feature.filter.domain.model.AreaPlain
import ru.practicum.android.diploma.feature.filter.domain.model.Country
import ru.practicum.android.diploma.feature.filter.domain.model.IndustryPlain

class SettingsFiltersFragment : Fragment() {

    private var _binding: FragmentSettingsFiltersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsFiltersViewModel by viewModel()

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
            var plainText = "${filterSettings.country!!.name}\n${filterSettings.areaPlain!!.name}"
            plainText = plainText.trim()
            binding.workPlaceTextInputEditText.setText(plainText)
        } else if (filterSettings.country != null) {
            binding.workPlaceTextInputEditText.setText(filterSettings.country!!.name)
        } else if (filterSettings.areaPlain != null) {
            binding.workPlaceTextInputEditText.setText(filterSettings.areaPlain!!.name)
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

            binding.workPlaceTextInputEditText.setText("")
            binding.industryTextInputEditText.setText("")
            binding.filterSettingsExpectedSalaryEditText.setText("")
            binding.doNotShowWithoutSalaryCheckBox.isChecked = false

            DataTransmitter.postAreaPlain(AreaPlain(id = "", name = " "))
            DataTransmitter.postCountry(Country(id = "", name = ""))
            DataTransmitter.postIndustryPlain(IndustryPlain(id = "", name = ""))

            renderWorkplaceTextInputLayout("")
            renderIndustryTextInputLayout("")
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

            var country = if (DataTransmitter.getCountry() != null) DataTransmitter.getCountry()
            else oldFilterSettings.country

            var areaPlain =
                if (DataTransmitter.getAreaPlain() != null) DataTransmitter.getAreaPlain()
                else oldFilterSettings.areaPlain

            var industryPlain =
                if (DataTransmitter.getIndustryPlain() != null) DataTransmitter.getIndustryPlain()
                else oldFilterSettings.industryPlain

            areaPlain = if (areaPlain?.id?.isEmpty() == true) null else areaPlain
            country = if (country?.id?.isEmpty() == true) null else country
            industryPlain = if (industryPlain?.id?.isEmpty() == true) null else industryPlain

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
            val industryName = DataTransmitter.getIndustryPlain()?.name
            binding.industryTextInputEditText.setText(industryName)
            renderIndustryTextInputLayout(industryName!!)
        }

        if (DataTransmitter.getCountry() != null && DataTransmitter.getAreaPlain() != null) {
            var plainText =
                "${DataTransmitter.getCountry()?.name}, ${DataTransmitter.getAreaPlain()?.name}"
            plainText = plainText.trim()
            binding.workPlaceTextInputEditText.setText(plainText)
        } else if (DataTransmitter.getCountry() != null) {
            binding.workPlaceTextInputEditText.setText(DataTransmitter.getCountry()?.name)
        } else if (DataTransmitter.getAreaPlain() != null) {
            binding.workPlaceTextInputEditText.setText(DataTransmitter.getAreaPlain()?.name)
        }

        binding.apply {
            workplaceClear.setOnClickListener {
                binding.workPlaceTextInputEditText.setText("")
                DataTransmitter.postAreaPlain(AreaPlain(id = "", name = " "))
                DataTransmitter.postCountry(Country(id = "", name = ""))
                renderWorkplaceTextInputLayout("")
            }
            industryClear.setOnClickListener {
                binding.industryTextInputEditText.setText("")
                DataTransmitter.postIndustryPlain(IndustryPlain(id = "", name = ""))
                renderIndustryTextInputLayout("")
            }
        }
    }

    private fun renderIndustryTextInputLayout(industry: String) {
        if (industry.isNotEmpty()) {
            binding.apply {
                industryTextInputLayout.defaultHintTextColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.black_to_white_color)
                industryClear.visibility = View.VISIBLE
                industryArrowForward.visibility = View.GONE
            }
        } else {
            binding.apply {
                industryTextInputLayout.defaultHintTextColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.gray_color)
                industryClear.visibility = View.GONE
                industryArrowForward.visibility = View.VISIBLE
            }
        }
    }

    private fun renderWorkplaceTextInputLayout(workplace: String) {
        if (workplace.isNotEmpty()) {
            binding.apply {
                workPlaceTextInputLayout.defaultHintTextColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.black_to_white_color)
                workplaceClear.visibility = View.VISIBLE
                workplaceArrowForward.visibility = View.GONE
            }
        } else {
            binding.apply {
                workPlaceTextInputLayout.defaultHintTextColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.gray_color)
                workplaceClear.visibility = View.GONE
                workplaceArrowForward.visibility = View.VISIBLE
            }
        }
    }

    private fun clearFields() {
        DataTransmitter.postIndustryPlain(null)
        DataTransmitter.postCountry(null)
        DataTransmitter.postAreaPlain(null)
    }

    override fun onResume() {
        super.onResume()
        renderIndustryTextInputLayout(binding.industryTextInputEditText.text.toString())
        renderWorkplaceTextInputLayout(binding.workPlaceTextInputEditText.text.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}