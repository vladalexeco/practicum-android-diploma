package ru.practicum.android.diploma.feature.search.domain.models

import com.google.gson.annotations.SerializedName

data class Employer (val accreditedItEmployer: Boolean,
                        val alternateUrl: String,
                        val id: String,
                        val logoUrls: LogoUrls,
                        val name: String,
                        val trusted: Boolean,
                        val url: String,
                        val vacanciesUrl: String)