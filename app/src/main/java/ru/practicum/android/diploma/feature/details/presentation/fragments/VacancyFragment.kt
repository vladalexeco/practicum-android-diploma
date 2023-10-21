package ru.practicum.android.diploma.feature.details.presentation.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.util.CurrencyLogoCreator
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.feature.details.presentation.DataState
import ru.practicum.android.diploma.feature.details.presentation.viewmodels.VacancyViewModel
import ru.practicum.android.diploma.feature.search.domain.models.Salary
import ru.practicum.android.diploma.feature.search.domain.models.VacancyFull
import ru.practicum.android.diploma.feature.search.presentation.viewmodels.VacancyIdSharedViewModel

class VacancyFragment : Fragment() {

    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VacancyViewModel by viewModel()
    private val sharedViewModel: VacancyIdSharedViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVacancyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var vacancyId: String?

        sharedViewModel.observeVacancyId().observe(viewLifecycleOwner) {
            vacancyId = it
            viewModel.getDetailedVacancyData(vacancyId)
            getVacancy(vacancyId)
        }

        initListeners()

        viewModel.dataState.observe(viewLifecycleOwner) { dataState ->
            render(dataState)
            if (dataState is DataState.DataReceived) {
                viewModel.checkFavoriteStatus(dataState.data)
            }
        }
    }

    private fun getVacancy(vacancyId: String?) {
        if (!isNetworkAvailable(requireContext())) {
            if (vacancyId != null) {
                viewModel.getVacancyById(vacancyId).observe(viewLifecycleOwner) { vacancy ->
                    if (vacancy != null) {
                        render(DataState.DataReceived(vacancy))
                        viewModel.checkFavoriteStatus(vacancy)
                        binding.similarVacanciesButton.visibility=View.GONE
                    }
                }
            }
        }
    }

    private fun initListeners() {
        binding.vacancyContactEmailValue.setOnClickListener {
            viewModel.currentVacancyFull?.contacts?.email?.let { email ->
                viewModel.onContactEmailClicked(email)
            }
        }
        binding.vacancyContactPhoneValue.setOnClickListener {
            viewModel.currentVacancyFull?.contacts?.phones?.get(0)
                ?.let { phone -> viewModel.onContactPhoneClicked(phone.number) }
        }
        binding.sharingIcon.setOnClickListener { viewModel.currentVacancyFull?.applyAlternateUrl?.let { alternateUrl ->
            viewModel.onShareVacancyClicked(alternateUrl)
            }
        }

        binding.vacancyDetailsBackArrowImageview.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.similarVacanciesButton.setOnClickListener {
            findNavController().navigate(R.id.action_vacancyFragment_to_similarVacanciesFragment)
        }

        binding.favoritesIcon.setOnClickListener {
            viewModel.currentVacancyFull?.let { vacancyFull ->
                viewModel.onFavoriteButtonClick(vacancyFull)
            }
        }

        viewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            setFabIcon(isFavorite)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun render(dataState: DataState) {

        when(dataState) {

            is DataState.Loading -> {
                showLoader()
            }

            is DataState.Failed -> {
                showErrorMessage()
            }

            is DataState.DataReceived -> {
                setContentToViews(vacancyFull = dataState.data)
                showContent()
                viewModel.checkFavoriteStatus(dataState.data)
            }
        }

    }

    @SuppressLint("ResourceAsColor")
    private fun setContentToViews(vacancyFull: VacancyFull) {

        viewModel.currentVacancyFull = vacancyFull

        // Наименование вакансии
        binding.vacancyName.text = vacancyFull.name

        // Предлагаемая заработанная плата
        if (vacancyFull.salary == null) {
            binding.salary.text = "Зарплата не указана"
        } else {
            val salary: Salary = vacancyFull.salary

            val currencySymbol = CurrencyLogoCreator.getSymbol(salary.currency)

            val message = if (salary.to == null && salary.from != null) {
                "от ${salary.from} $currencySymbol"
            } else if (salary.to != null && salary.from == null) {
                "до ${salary.to} $currencySymbol"
            } else {
                "от ${salary.from} до ${salary.to} $currencySymbol"
            }

            binding.salary.text = message
        }

        // Логотип работодателя
        val logoUrl: String? = vacancyFull.employer?.logoUrls?.original
        setLogoToImageView(logoUrl)

        // Имя работодателя
        binding.employerName.text = vacancyFull.employer?.name

        // Город, регион
        binding.city.text = vacancyFull.area?.name

        // Требуемый опыт работы
        binding.requiredExperienceValue.text = vacancyFull.experience?.name

        // Трудоустройство
        val schedule = "${vacancyFull.employment?.name}, ${vacancyFull.schedule?.name}"
        binding.scheduleValue.text = schedule

        // Описание вакансии
        binding.vacancyDescriptionValue.setText(Html.fromHtml(vacancyFull.description, Html.FROM_HTML_MODE_COMPACT))

        // Ключевые навыки
        if (vacancyFull.keySkills.isNullOrEmpty()) {
            binding.keySkillsContainer.visibility = View.GONE
        } else {
            val interpunct = "\u00B7"
            var message = ""

            vacancyFull.keySkills.forEach { keySkill ->
                val singleLine = "$interpunct ${keySkill.name} \n"
                message += singleLine
            }

            binding.vacancyKeySkillsValue.text = message.trimEnd()

        }

        if (vacancyFull.contacts == null) {
            binding.contactsContainer.visibility = View.GONE
        } else {
            // Контактное лицо
            binding.vacancyContactPersonValue.text = vacancyFull.contacts.name

            // Email
            binding.vacancyContactEmailValue.text = vacancyFull.contacts.email

            // Телефон
            binding.vacancyContactPhoneValue.text = vacancyFull.contacts.phones[0].formatted

            // Комментарий
            binding.vacancyPhoneCommentValue.text = vacancyFull.contacts.phones[0].comment.toString()
        }

    }

    private fun setLogoToImageView(logoUrl: String?) {
        Glide.with(requireContext())
            .load(logoUrl)
            .placeholder(R.drawable.placeholder)
            .into(binding.employerImage)
    }

    private fun showLoader() {
        binding.detailsLoaderProgressBar.visibility = View.VISIBLE
        binding.detailsMainScreenConstraintLayout.visibility = View.GONE
        binding.detailsErrorMessageLinearLayout.visibility = View.GONE
    }

    private fun showContent() {
        binding.detailsLoaderProgressBar.visibility = View.GONE
        binding.detailsMainScreenConstraintLayout.visibility = View.VISIBLE
        binding.detailsErrorMessageLinearLayout.visibility = View.GONE
    }

    private fun showErrorMessage() {
        binding.detailsLoaderProgressBar.visibility = View.GONE
        binding.detailsMainScreenConstraintLayout.visibility = View.GONE
        binding.detailsErrorMessageLinearLayout.visibility = View.VISIBLE
    }

    private fun setFabIcon(isFavorite: Boolean) {
        binding.favoritesIcon.setImageResource(
            if (isFavorite) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off
        )
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities =
                connectivityManager.activeNetwork ?: return false
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(networkCapabilities)
                    ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.currentVacancyFull?.let { viewModel.checkFavoriteStatus(it) }
    }
}