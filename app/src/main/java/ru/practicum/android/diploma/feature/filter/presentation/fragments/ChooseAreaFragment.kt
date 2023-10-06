package ru.practicum.android.diploma.feature.filter.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentChooseAreaBinding
import ru.practicum.android.diploma.feature.filter.domain.model.Area
import ru.practicum.android.diploma.feature.filter.presentation.adapter.FilterAdapter
import ru.practicum.android.diploma.feature.filter.presentation.viewmodels.ChooseAreaViewModel

class ChooseAreaFragment : Fragment() {

    private var _binding: FragmentChooseAreaBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChooseAreaViewModel by viewModel()
    private var areaAdapter: FilterAdapter<Area>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseAreaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}