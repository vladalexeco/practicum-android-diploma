package ru.practicum.android.diploma.feature.filter.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.util.DataTransmitter
import ru.practicum.android.diploma.databinding.FragmentChooseWorkplaceBinding
import ru.practicum.android.diploma.feature.filter.domain.model.AreaPlain
import ru.practicum.android.diploma.feature.filter.presentation.viewmodels.ChooseWorkPlaceViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

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

        viewModel.hasRegionsLiveData.observe(viewLifecycleOwner) {
            hasRegions = it
        }

        binding.chooseWorkplaceBackArrowImageview.setOnClickListener {
            findNavController().popBackStack(R.id.settingsFiltersFragment, false)
            DataTransmitter.postCountry(null)
            DataTransmitter.postAreaPlain(null)
        }

        binding.chooseCountryTextInputEditText.setOnClickListener {
            findNavController().navigate(R.id.action_chooseWorkplaceFragment_to_chooseCountryFragment)
        }

        binding.regionTextInputEditText.setOnClickListener {
            if (DataTransmitter.getCountry() != null) {
               if (hasRegions) {
                   findNavController().navigate(R.id.action_chooseWorkplaceFragment_to_chooseRegionFragment)
               } else {
                   Toast.makeText(requireContext(), "Выбранная страна не имеет в базе регионы", Toast.LENGTH_SHORT).show()
               }
            } else {
                Toast.makeText(requireContext(), "Сначала выберете страну", Toast.LENGTH_SHORT).show()
            }
        }

        binding.chooseButton.setOnClickListener {
            if (binding.chooseCountryTextInputEditText.text?.isNotEmpty() == true &&
                binding.regionTextInputEditText.text?.isEmpty() == true) {

                DataTransmitter.postAreaPlain(AreaPlain(id = "", name = ""))
                findNavController().navigate(R.id.action_chooseWorkplaceFragment_to_settingsFiltersFragment)

            } else if(binding.chooseCountryTextInputEditText.text?.isNotEmpty() == true &&

                binding.regionTextInputEditText.text?.isNotEmpty() == true) {
                findNavController().navigate(R.id.action_chooseWorkplaceFragment_to_settingsFiltersFragment)

            } else {

                Toast.makeText(requireContext(), "Вам нужно выбрать страну", Toast.LENGTH_SHORT).show()

            }
        }

        if (DataTransmitter.getCountry() != null) {
            binding.chooseCountryTextInputEditText.setText(DataTransmitter.getCountry()?.name)
        }

        if (DataTransmitter.getAreaPlain() != null) {
            binding.regionTextInputEditText.setText(DataTransmitter.getAreaPlain()?.name)
        }

        if (binding.chooseCountryTextInputEditText.text!!.isNotEmpty()) {
            viewModel.checkCountryHasRegions()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}