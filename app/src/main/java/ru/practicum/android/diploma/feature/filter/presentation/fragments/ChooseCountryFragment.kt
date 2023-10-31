package ru.practicum.android.diploma.feature.filter.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.util.DataTransmitter
import ru.practicum.android.diploma.databinding.FragmentChooseCountryBinding
import ru.practicum.android.diploma.feature.filter.domain.model.Country
import ru.practicum.android.diploma.feature.filter.presentation.adapter.CountriesAdapter
import ru.practicum.android.diploma.feature.filter.presentation.states.CountriesState
import ru.practicum.android.diploma.feature.filter.presentation.states.LiveDataResource
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

        viewModel.dataCountry.observe(viewLifecycleOwner) { liveDataResource ->
            when(liveDataResource) {
                is LiveDataResource.CountryStorage -> {
                    val country: Country = liveDataResource.data
                    DataTransmitter.postCountry(country)
                    DataTransmitter.postAreaPlain(null)
                    findNavController().navigate(R.id.action_chooseCountryFragment_to_chooseWorkplaceFragment)
                }

                is LiveDataResource.CountryStateStorage -> {
                    when (val state = liveDataResource.data) {
                        is CountriesState.DisplayCountries -> displayCountries(state.countries)
                        is CountriesState.Error -> displayError(state)
                    }
                }

                else -> throw Throwable(getString(R.string.bad_inheritor_error))
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

    private fun displayError(state: CountriesState.Error) {
        binding.apply {
            countryListRecyclerView.visibility = View.INVISIBLE
            errorCountriesLayout.visibility = View.VISIBLE
            countriesErrorText.text = state.errorText
        }
        Glide
            .with(requireContext())
            .load(state.drawableId)
            .transform(CenterCrop())
            .into(binding.countriesErrorImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}