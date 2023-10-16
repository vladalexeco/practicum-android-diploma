package ru.practicum.android.diploma.core.util

object CurrencyLogoCreator {
    /**
     * Валюта вакансии приходит с сервера String?, в связи с чем невозможно передать в функцию объект
     * Currecy.
     */

    fun getSymbol(currency: String?): String {
        return when (currency) {
            Currency.AZERBAIJANI_MANAT.currency -> "\u20bc"
            Currency.BELORUS_RUBLE.currency -> "\u0072"
            Currency.EURO.currency -> "\u20ac"
            Currency.GEORGIAN_LARI.currency -> "\u20be"
            Currency.KYRGYZSTANI_SOM.currency -> "\u043b"
            Currency.KAZAKHSTAN_TENGE.currency -> "\u043b"
            Currency.RUSSIAN_RUBLE.currency -> "\u20bd"
            Currency.USA_DOLLAR.currency -> "\u0024"
            Currency.UZBEK_SUM.currency -> "\u043b"
            Currency.UKRAINE_HRYVNIA.currency -> "\u20b4"
            else -> ""
        }
    }
}