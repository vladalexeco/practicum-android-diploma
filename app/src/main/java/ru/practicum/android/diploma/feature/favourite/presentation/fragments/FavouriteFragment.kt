package ru.practicum.android.diploma.feature.favourite.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFavouriteBinding
import ru.practicum.android.diploma.feature.details.presentation.fragments.VacancyFragment
import ru.practicum.android.diploma.feature.favourite.data.model.toVacancyShort
import ru.practicum.android.diploma.feature.favourite.presentation.FavoriteVacancyState
import ru.practicum.android.diploma.feature.favourite.presentation.viewmodels.FavouriteFragmentViewModel
import ru.practicum.android.diploma.feature.search.domain.models.VacancyShort
import ru.practicum.android.diploma.feature.similar_vacancies.simillarvacanciesadapter.VacanciesAdapterCommon

class FavouriteFragment : Fragment(), VacanciesAdapterCommon.ClickListener {

    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavouriteFragmentViewModel by viewModel()
    private var favoriteAdapter: VacanciesAdapterCommon? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteAdapter = VacanciesAdapterCommon(this, requireContext())

        binding.recyclerViewFavorite.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerViewFavorite.adapter = favoriteAdapter

        viewModel.getAllVacancies()

        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is FavoriteVacancyState.Empty -> {
                        binding.placeHolderFavorite.visibility = View.VISIBLE
                        binding.recyclerViewFavorite.visibility = View.GONE
                    }

                    is FavoriteVacancyState.VacancyLoaded -> {
                        binding.placeHolderFavorite.visibility = View.GONE
                        binding.recyclerViewFavorite.visibility = View.VISIBLE
                        val vacancy = state.vacancy.map { it.toVacancyShort() }
                        favoriteAdapter!!.setVacancyList(vacancy)
                    }
                }
            }
        }
    }

    override fun onClick(vacancy: VacancyShort) {
        findNavController().navigate(
            R.id.action_favouriteFragment_to_vacancyFragment,
            VacancyFragment.createArgs(vacancy.id)
        )
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllVacancies()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}