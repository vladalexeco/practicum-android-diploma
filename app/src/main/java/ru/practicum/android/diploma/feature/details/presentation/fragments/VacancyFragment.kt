package ru.practicum.android.diploma.feature.details.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.feature.details.presentation.viewmodels.VacancyViewModel

class VacancyFragment : Fragment() {

    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VacancyViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVacancyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        binding.vacancyContactEmailValue.setOnClickListener { viewModel.onContactEmailClicked() }
        binding.vacancyContactPhoneValue.setOnClickListener { viewModel.onContactPhoneClicked() }
        binding.sharingIcon.setOnClickListener { viewModel.onShareVacancyClicked() }

        binding.vacancyDetailsBackArrowImageview.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.similarVacanciesButton.setOnClickListener {
            findNavController().navigate(R.id.action_vacancyFragment_to_similarVacanciesFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}