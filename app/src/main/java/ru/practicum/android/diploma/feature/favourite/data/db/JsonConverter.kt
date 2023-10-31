package ru.practicum.android.diploma.feature.favourite.data.db

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object JsonConverter {
    inline fun <reified T> itemToJson(item: T): String = Gson().toJson(item)

    inline fun <reified T> itemListToJson(items: List<T>?): String = Gson().toJson(items)

    inline fun <reified T> jsonToItem(json: String): T = Gson().fromJson(json, T::class.java)

    inline fun <reified T> jsonToItemList(json: String): List<T> =
        Gson().fromJson(json, object : TypeToken<List<T>>() {}.type)
}