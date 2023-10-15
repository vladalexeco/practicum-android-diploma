package ru.practicum.android.diploma.feature.details.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.util.CurrencyLogoCreator
import ru.practicum.android.diploma.core.util.DataTransmitter
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.feature.details.presentation.DataState
import ru.practicum.android.diploma.feature.details.presentation.viewmodels.VacancyViewModel
import ru.practicum.android.diploma.feature.search.domain.models.Salary
import ru.practicum.android.diploma.feature.search.domain.models.VacancyFull

class VacancyFragment : Fragment() {

    private var currentVacancyFull: VacancyFull? = null

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

        currentVacancyFull?.id?.let{DataTransmitter.postId(it)}

        initListeners()

        viewModel.dataState.observe(viewLifecycleOwner) { dataState ->
            render(dataState)
        }
    }

    private fun initListeners() {
        binding.vacancyContactEmailValue.setOnClickListener {
            currentVacancyFull?.contacts?.email?.let { email ->
                viewModel.onContactEmailClicked(email)
            }
        }
        binding.vacancyContactPhoneValue.setOnClickListener {
            currentVacancyFull?.contacts?.phones?.get(0)
                ?.let { phone -> viewModel.onContactPhoneClicked(phone.number) }
        }
        binding.sharingIcon.setOnClickListener { currentVacancyFull?.applyAlternateUrl?.let { alternateUrl ->
            viewModel.onShareVacancyClicked(alternateUrl)
            }
        }

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
            }
        }

    }

    @SuppressLint("ResourceAsColor")
    private fun setContentToViews(vacancyFull: VacancyFull) {

        currentVacancyFull = vacancyFull

        // Наименование вакансии
        binding.vacancyName.text = vacancyFull.name

        // Предлагаемая заработанная плата
        if (vacancyFull.salary == null) {
            binding.salary.text = "Зарплата не указана"
        } else {
            val salary: Salary = vacancyFull.salary

            var currencySymbol = CurrencyLogoCreator.getSymbol(salary.currency)

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
        binding.scheduleValue.text = "${vacancyFull.employment?.name}, ${vacancyFull.schedule?.name}"

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
}