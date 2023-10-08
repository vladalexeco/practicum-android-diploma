package ru.practicum.android.diploma.feature.filter.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentChooseCountryBinding
import ru.practicum.android.diploma.feature.filter.domain.model.Country
import ru.practicum.android.diploma.feature.filter.presentation.adapter.CountriesAdapter
import ru.practicum.android.diploma.feature.filter.presentation.states.CountriesState
import ru.practicum.android.diploma.feature.filter.presentation.viewmodels.ChooseCountryViewModel

class ChooseCountryFragment : Fragment() {

    private var _binding: FragmentChooseCountryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChooseCountryViewModel by viewModel()
    private var countriesAdapter: CountriesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseCountryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeCountriesState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is CountriesState.DisplayCountries -> displayCountries(state.countries)
                is CountriesState.Error -> displayError(state.errorText)
            }
        }

        binding.chooseCountryBackArrowImageview.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun displayCountries(countries: ArrayList<Country>) {
        binding.apply {
            countryListRecyclerView.visibility = View.VISIBLE
            errorCountriesLayout.visibility = View.INVISIBLE
        }
        if (countriesAdapter == null) {
            countriesAdapter = CountriesAdapter(countries) { country ->
                viewModel.onCountryClicked(country)
            }
            binding.countryListRecyclerView.apply {
                adapter = countriesAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    private fun displayError(errorText: String) {
        binding.apply {
            countryListRecyclerView.visibility = View.INVISIBLE
            errorCountriesLayout.visibility = View.VISIBLE
            countriesErrorText.text = errorText
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}