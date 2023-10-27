package ru.practicum.android.diploma.feature.filter.presentation.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
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
import ru.practicum.android.diploma.databinding.FragmentChooseWorkplaceBinding
import ru.practicum.android.diploma.feature.filter.domain.model.AreaPlain
import ru.practicum.android.diploma.feature.filter.presentation.viewmodels.ChooseWorkPlaceViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.feature.filter.domain.model.Country
import ru.practicum.android.diploma.feature.filter.presentation.states.LiveDataResource

class ChooseWorkplaceFragment : Fragment() {

    private var _binding: FragmentChooseWorkplaceBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChooseWorkPlaceViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseWorkplaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnBackPressedListener()
        setDataWorkPlaceObserver()
        binding.chooseWorkplaceBackArrowImageview.setOnClickListener {
            findNavController().popBackStack(R.id.settingsFiltersFragment, false)
            DataTransmitter.postCountry(null)
            DataTransmitter.postAreaPlain(null)
        }
        setEditTextsClickListeners()
        chooseButtonCLickListener()

        if (DataTransmitter.getCountry() != null) binding.chooseCountryTextInputEditText.setText(
            DataTransmitter.getCountry()?.name
        )

        if (DataTransmitter.getAreaPlain() != null) binding.areaTextInputEditText.setText(
            DataTransmitter.getAreaPlain()?.name
        )

        if (binding.chooseCountryTextInputEditText.text?.isEmpty() == true &&
            binding.areaTextInputEditText.text?.isNotEmpty() == true
        ) DataTransmitter.getAreaPlain()?.id?.let {
            viewModel.getAreaPlain(it)
        }

        setClearButtonsClickListeners()
        binding.chooseCountryTextInputEditText.doOnTextChanged { text, _, _, _ ->
            renderCountryTextInputLayout(text.toString())
        }
        if (binding.chooseCountryTextInputEditText.text?.isNotEmpty() == true ||
            binding.areaTextInputEditText.text?.isNotEmpty() == true
        ) setChooseButtonVisible(true)
        renderCountryTextInputLayout(binding.chooseCountryTextInputEditText.text.toString())
        renderAreaTextInputLayout(binding.areaTextInputEditText.text.toString())
        viewModel.initData()
    }

    private fun setOnBackPressedListener() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack(R.id.settingsFiltersFragment, false)
                    DataTransmitter.postCountry(null)
                    DataTransmitter.postAreaPlain(null)
                }
            })
    }

    private fun setDataWorkPlaceObserver() {
        viewModel.dataWorkplace.observe(viewLifecycleOwner) { liveDataResource ->
            Log.d("!@#", liveDataResource.javaClass.toString())
            when (liveDataResource) {
                is LiveDataResource.AreaPlainStorage -> {
                    Log.d("!@#", "LiveDataResource.AreaPlainStorage")
                    val areaPlain: AreaPlain? = liveDataResource.data
                    if (areaPlain != null) {
                        if (areaPlain.parentId != null) {
                            viewModel.getAreaPlain(areaPlain.parentId)
                        } else {
                            val country = Country(areaPlain.id, areaPlain.name)
                            DataTransmitter.postCountry(country)
                            binding.chooseCountryTextInputEditText.setText(areaPlain.name)
                        }
                    }
                }

                is LiveDataResource.CountryNameStorage -> {
                    Log.d("!@#", "LiveDataResource.CountryNameStorage")
                    val countryName: String = liveDataResource.data
                    binding.chooseCountryTextInputEditText.setText(countryName)
                    renderCountryTextInputLayout(countryName)
                }

                is LiveDataResource.AreaNameStorage -> {
                    Log.d("!@#", "LiveDataResource.AreaNameStorage")
                    val areaName: String = liveDataResource.data
                    binding.areaTextInputEditText.setText(areaName)
                    renderAreaTextInputLayout(areaName)
                }

                else -> throw Throwable(getString(R.string.bad_inheritor_error))
            }
        }
    }

    private fun setEditTextsClickListeners() {
        binding.apply {
            chooseCountryTextInputEditText.setOnClickListener {
                findNavController().navigate(R.id.action_chooseWorkplaceFragment_to_chooseCountryFragment)
            }
            areaTextInputEditText.setOnClickListener {
                findNavController().navigate(R.id.action_chooseWorkplaceFragment_to_chooseRegionFragment)
            }
            countryArrowForward.setOnClickListener {
                findNavController().navigate(R.id.action_chooseWorkplaceFragment_to_chooseCountryFragment)
            }
            areaArrowForward.setOnClickListener {
                findNavController().navigate(R.id.action_chooseWorkplaceFragment_to_chooseRegionFragment)
            }
        }
    }

    private fun chooseButtonCLickListener() {
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
    }

    private fun setClearButtonsClickListeners() {
        binding.apply {
            countryClear.setOnClickListener {
                viewModel.onCountryCleared()
                viewModel.onAreaCleared()
                setChooseButtonVisible(false)
            }
            areaClear.setOnClickListener {
                viewModel.onAreaCleared()
                if (binding.chooseCountryTextInputEditText.text?.isEmpty() == true)
                    setChooseButtonVisible(false)
            }
        }
    }

    private fun setChooseButtonVisible(isVisible: Boolean) {
        binding.chooseButton.isVisible = isVisible
    }

    private fun renderAreaTextInputLayout(areaName: String) {
        if (areaName.isNotEmpty()) {
            binding.apply {
                areaTextInputLayout.defaultHintTextColor = getColor(R.color.black_to_white_color)
                areaClear.visibility = View.VISIBLE
                areaArrowForward.visibility = View.GONE
            }
        } else {
            binding.apply {
                areaTextInputLayout.defaultHintTextColor = getColor(R.color.gray_color)
                areaClear.visibility = View.GONE
                areaArrowForward.visibility = View.VISIBLE
            }
        }
    }

    private fun renderCountryTextInputLayout(countryName: String) {
        Log.d("!@#", "renderCountryTextInputLayout")
        if (countryName.isNotEmpty()) {
            binding.apply {
                chooseCountryTextInputLayout.defaultHintTextColor =
                    getColor(R.color.black_to_white_color)
                countryClear.visibility = View.VISIBLE
                countryArrowForward.visibility = View.GONE
            }
        } else {
            binding.apply {
                chooseCountryTextInputLayout.defaultHintTextColor = getColor(R.color.gray_color)
                countryClear.visibility = View.GONE
                countryArrowForward.visibility = View.VISIBLE
            }
        }
    }

    private fun getColor(colorId: Int): ColorStateList? {
        return ContextCompat.getColorStateList(
            requireContext(),
            colorId
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}