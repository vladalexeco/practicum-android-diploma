package ru.practicum.android.diploma.feature.details.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.feature.details.presentation.viewmodels.VacancyViewModel

class VacancyFragment : Fragment() {

    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VacancyViewModel by viewModels()

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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}