package ru.practicum.android.diploma.feature.details.presentation.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.util.CurrencyLogoCreator
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.feature.details.presentation.DataState
import ru.practicum.android.diploma.feature.details.presentation.viewmodels.VacancyViewModel
import ru.practicum.android.diploma.feature.search.domain.models.Salary
import ru.practicum.android.diploma.feature.search.domain.models.VacancyFull
import ru.practicum.android.diploma.feature.similar_vacancies.presentation.fragments.SimilarVacanciesFragment

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

        val vacancyId = requireArguments().getString(VACANCY_ID)
        viewModel.getDetailedVacancyData(vacancyId)
        getVacancy(vacancyId)
        viewModel.getVacancyById(vacancyId)

        initListeners()

        viewModel.dataState.observe(viewLifecycleOwner) { dataState ->
            render(dataState)
        }

        binding.similarVacanciesButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_vacancyFragment_to_similarVacanciesFragment,
                SimilarVacanciesFragment.createArgs(vacancyId)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.currentVacancyFull?.let { viewModel.checkFavoriteStatus(it) }
    }

    private fun getVacancy(vacancyId: String?) {
        if (!isNetworkAvailable(requireContext())) {
            vacancyId?.let { id ->
                viewModel.dataState.observe(viewLifecycleOwner) { vacancy ->
                    vacancy?.also {
                        viewModel.getVacancyById(id)
                        binding.similarVacanciesButton.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun initListeners() = with(binding) {
        vacancyContactEmailValue.setOnClickListener {
            viewModel.currentVacancyFull?.contacts?.email?.let { email ->
                viewModel.onContactEmailClicked(email)
            }
        }
        vacancyContactPhoneValue.setOnClickListener {
            viewModel.currentVacancyFull?.contacts?.phones?.get(0)
                ?.let { phone -> viewModel.onContactPhoneClicked(phone.number) }
        }
        sharingIcon.setOnClickListener {
            viewModel.currentVacancyFull?.applyAlternateUrl?.let { alternateUrl ->
                viewModel.onShareVacancyClicked(alternateUrl)
            }
        }
        vacancyDetailsBackArrowImageview.setOnClickListener {
            findNavController().popBackStack()
        }
        favoritesIcon.setOnClickListener {
            viewModel.currentVacancyFull?.let { vacancyFull ->
                viewModel.onFavoriteButtonClick(vacancyFull)
            }
        }
    }

    private fun render(dataState: DataState) {

        when (dataState) {
            is DataState.Loading -> showLoader()
            is DataState.Failed -> showErrorMessage()
            is DataState.VacancyByIdReceived -> dataState.vacancy?.let { setContentToViews(it) }
            is DataState.DataReceived -> {
                setContentToViews(vacancyFull = dataState.data)
                showContent()
                setFabIcon(dataState.isFavorite)
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun setContentToViews(vacancyFull: VacancyFull) {
        viewModel.currentVacancyFull = vacancyFull

        // Наименование вакансии
        binding.vacancyName.text = vacancyFull.name

        setSalaryData(vacancyFull)
        setEmployerAndVacancyDescriptionData(vacancyFull)
        setContactsData(vacancyFull)
    }

    private fun setSalaryData(vacancyFull: VacancyFull) {
        // Предлагаемая заработанная плата
        if (vacancyFull.salary == null) {
            binding.salary.text = getString(R.string.message_salary_not_pointed)
        } else {
            val salary: Salary = vacancyFull.salary
            val currencySymbol = CurrencyLogoCreator.getSymbol(salary.currency)
            val message = if (salary.to == null && salary.from != null) {
                getString(R.string.salary_template_from, salary.from, currencySymbol)
            } else if (salary.to != null && salary.from == null) {
                getString(R.string.salary_template_to, salary.to, currencySymbol)
            } else {
                getString(R.string.salary_template_from_to, salary.from, salary.to, currencySymbol)
            }

            binding.salary.text = message
        }
    }

    private fun setEmployerAndVacancyDescriptionData(vacancyFull: VacancyFull) {
        // Логотип работодателя
        setLogoToImageView(vacancyFull.employer?.logoUrls?.original)

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
        binding.vacancyDescriptionValue.setText(
            Html.fromHtml(
                vacancyFull.description?.addSpaces(),
                FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM
            )
        )

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
    }

    private fun setContactsData(vacancyFull: VacancyFull) {
        if (vacancyFull.contacts == null) {
            binding.contactsContainer.visibility = View.GONE
        } else {
            binding.apply {
                // Контактное лицо
                vacancyContactPersonValue.text = vacancyFull.contacts.name

                // Email
                vacancyContactEmailValue.text = vacancyFull.contacts.email

                // Телефон
                vacancyContactPhoneValue.text = vacancyFull.contacts.phones[0].formatted

                // Комментарий
                vacancyPhoneCommentValue.text =
                    vacancyFull.contacts.phones[0].comment.toString()
            }
        }
    }

    private fun String.addSpaces(): String {
        return this.replace(Regex("<li>\\s<p>|<li>"), "<li>\u00A0")
    }

    private fun setLogoToImageView(logoUrl: String?) {
        Glide.with(requireContext())
            .load(logoUrl)
            .placeholder(R.drawable.placeholder)
            .into(binding.employerImage)
    }

    private fun showLoader() {
        binding.apply {
            detailsLoaderProgressBar.visibility = View.VISIBLE
            detailsMainScreenConstraintLayout.visibility = View.GONE
            detailsErrorMessageLinearLayout.visibility = View.GONE
        }
    }

    private fun showContent() {
        binding.apply {
            detailsLoaderProgressBar.visibility = View.GONE
            detailsMainScreenConstraintLayout.visibility = View.VISIBLE
            detailsErrorMessageLinearLayout.visibility = View.GONE
        }
    }

    private fun showErrorMessage() {
        binding.apply {
            detailsLoaderProgressBar.visibility = View.GONE
            detailsMainScreenConstraintLayout.visibility = View.GONE
            detailsErrorMessageLinearLayout.visibility = View.VISIBLE
        }
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun createArgs(vacancyId: String?) = bundleOf(VACANCY_ID to vacancyId)
        private const val VACANCY_ID = "VACANCY_ID"
    }
}