package ru.practicum.android.diploma.core.util

import ru.practicum.android.diploma.BuildConfig

const val BASE_URL = "https://api.hh.ru"

const val token: String = BuildConfig.HH_ACCESS_TOKEN

// Shared Preferences
const val FILTER_SETTINGS_SP = "FILTER_SETTINGS_SP"
const val COUNTRY_KEY = "COUNTRY_KEY"
const val AREA_KEY = "REGION_ID_KEY"
const val INDUSTRY_KEY = "INDUSTRY_ID_KEY"
const val EXPECTED_SALARY_KEY = "EXPECTED_SALARY_KEY"
const val NOT_SHOW_WITHOUT_SALARY_KEY = "NOT_SHOW_WITHOUT_SALARY_KEY"

//server code responses
const val STATUS_CODE_SUCCESS = 200
const val STATUS_CODE_SERVER_ERROR = 500
const val STATUS_CODE_BAD_REQUEST = 400
const val STATUS_CODE_NO_NETWORK_CONNECTION = -1
