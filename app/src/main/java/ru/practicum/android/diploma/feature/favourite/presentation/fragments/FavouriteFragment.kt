package ru.practicum.android.diploma.feature.favourite.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFavouriteBinding
import ru.practicum.android.diploma.feature.favourite.data.model.toVacancyFullEntity
import ru.practicum.android.diploma.feature.favourite.data.model.toVacancyShort
import ru.practicum.android.diploma.feature.favourite.presentation.FavoriteVacancyState
import ru.practicum.android.diploma.feature.favourite.presentation.adapters.VacanciesPagingAdapter
import ru.practicum.android.diploma.feature.favourite.presentation.viewmodels.FavouriteFragmentViewModel
import ru.practicum.android.diploma.feature.search.domain.models.VacancyShort
import ru.practicum.android.diploma.feature.search.searchadapter.VacanciesAdapter
import ru.practicum.android.diploma.feature.similar_vacancies.simillarvacanciesadapter.SimilarVacanciesAdapter

class FavouriteFragment : Fragment(),SimilarVacanciesAdapter.ClickListener {

    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavouriteFragmentViewModel by viewModel()
    private val favoriteAdapter = VacanciesPagingAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewFavorite.layoutManager= LinearLayoutManager(requireActivity())


        viewModel.getAllVacancies()

        viewModel.pagingData.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                Log.d("PAGING", it.toString())
                favoriteAdapter.submitData(it)
            }
        }

        binding.recyclerViewFavorite.adapter=favoriteAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(vacancy: VacancyShort) {
        val bundle = Bundle()
        bundle.putString("vacancyId", vacancy.id)
        findNavController().navigate(R.id.action_favouriteFragment_to_vacancyFragment, bundle)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllVacancies()
    }
}