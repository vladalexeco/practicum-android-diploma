package ru.practicum.android.diploma.feature.filter.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.util.DataTransmitter
import ru.practicum.android.diploma.databinding.FragmentSettingsFiltersBinding
import ru.practicum.android.diploma.feature.filter.domain.model.FilterSettings
import ru.practicum.android.diploma.feature.filter.presentation.viewmodels.SettingsFiltersViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

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
            var plainText = "${filterSettings.country!!.name}, ${filterSettings.areaPlain!!.name}"
            plainText = plainText.trim()
            binding.workPlaceTextInputEditText.setText(plainText)
            showConfirmAndClearButtons(true)
        } else if (filterSettings.country != null) {
            binding.workPlaceTextInputEditText.setText(filterSettings.country!!.name)
            showConfirmAndClearButtons(true)
        }

        if (filterSettings.industryPlain != null) {
            binding.industryTextInputEditText.setText(filterSettings.industryPlain!!.name)
            showConfirmAndClearButtons(true)
        }

        if (filterSettings.expectedSalary != -1) {
            binding.filterSettingsExpectedSalaryEditText.setText(filterSettings.expectedSalary.toString())
            showConfirmAndClearButtons(true)
            binding.clearSalaryImageView.visibility = View.VISIBLE
        } else {
            binding.filterSettingsExpectedSalaryEditText.setText("")
            binding.clearSalaryImageView.visibility = View.GONE
        }

        if (filterSettings.notShowWithoutSalary) {
            binding.doNotShowWithoutSalaryCheckBox.isChecked = filterSettings.notShowWithoutSalary
            showConfirmAndClearButtons(true)
        }


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

            viewModel.clearFilterSettings()

            showConfirmAndClearButtons(false)

            DataTransmitter.postAreaPlain(null)
            DataTransmitter.postCountry(null)
            DataTransmitter.postIndustryPlain(null)

            renderWorkplaceTextInputLayout("")
            renderIndustryTextInputLayout("")
        }

        binding.workPlaceTextInputEditText.setOnClickListener {
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
            showConfirmAndClearButtons(true)
        }

        if (DataTransmitter.getCountry() != null) {
            val countryName = DataTransmitter.getCountry()!!.name
            var workplaceText: String
            var areaName: String
            if (DataTransmitter.getAreaPlain() != null) {
                areaName = DataTransmitter.getAreaPlain()!!.name
                workplaceText = "$countryName, $areaName"
            } else {
                workplaceText = countryName
            }
            binding.workPlaceTextInputEditText.setText(workplaceText)
            renderIndustryTextInputLayout(workplaceText)
            showConfirmAndClearButtons(true)
        }

        binding.filterSettingsExpectedSalaryEditText.doOnTextChanged { text, _, _, _ ->
            if (text?.isNotEmpty() == true) {
                showConfirmAndClearButtons(true)
            } else {
                if (binding.workPlaceTextInputEditText.text?.isEmpty() == true &&
                    binding.industryTextInputEditText.text?.isEmpty() == true &&
                    !binding.doNotShowWithoutSalaryCheckBox.isChecked
                ) {
                    showConfirmAndClearButtons(false)
                }
            }
            clearButtonVisibility(text)
        }

        binding.doNotShowWithoutSalaryCheckBox.setOnClickListener {
            if (binding.doNotShowWithoutSalaryCheckBox.isChecked) {
                showConfirmAndClearButtons(true)
            } else {
                if (binding.workPlaceTextInputEditText.text?.isEmpty() == true &&
                    binding.industryTextInputEditText.text?.isEmpty() == true &&
                    binding.filterSettingsExpectedSalaryEditText.text?.isEmpty() == true &&
                    !binding.doNotShowWithoutSalaryCheckBox.isChecked
                ) {
                    showConfirmAndClearButtons(false)
                }
            }
        }

        binding.apply {
            workplaceClear.setOnClickListener {
                binding.workPlaceTextInputEditText.setText("")
                DataTransmitter.postAreaPlain(null)
                DataTransmitter.postCountry(null)
                renderWorkplaceTextInputLayout("")
            }
            industryClear.setOnClickListener {
                binding.industryTextInputEditText.setText("")
                DataTransmitter.postIndustryPlain(null)
                renderIndustryTextInputLayout("")
            }
        }

        binding.clearSalaryImageView.setOnClickListener {
            clearSearch()
        }

        renderIndustryTextInputLayout(binding.industryTextInputEditText.text.toString())
        renderWorkplaceTextInputLayout(binding.workPlaceTextInputEditText.text.toString())
    }

    private fun clearSearch() {
        binding.filterSettingsExpectedSalaryEditText.setText("")
        binding.filterSettingsExpectedSalaryEditText.clearFocus()
        binding.clearSalaryImageView.isVisible = false
    }

    private fun clearButtonVisibility(s: CharSequence?) {
        binding.clearSalaryImageView.isVisible = !s.isNullOrEmpty()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showConfirmAndClearButtons(isVisible: Boolean) {
        if (isVisible) {
            binding.confirmButton.visibility = View.VISIBLE
            binding.resetSettingsTextview.visibility = View.VISIBLE
        } else {
            binding.confirmButton.visibility = View.GONE
            binding.resetSettingsTextview.visibility = View.GONE
        }
    }

}