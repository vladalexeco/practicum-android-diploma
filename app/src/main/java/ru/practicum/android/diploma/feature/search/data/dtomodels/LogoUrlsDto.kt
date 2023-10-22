package ru.practicum.android.diploma.feature.search.data.dtomodels

import com.google.gson.annotations.SerializedName

data class LogoUrlsDto( @SerializedName("240")
                        val logo240: String?,
                        @SerializedName("90")
                        val logo90: String?,
                        val original: String?)
