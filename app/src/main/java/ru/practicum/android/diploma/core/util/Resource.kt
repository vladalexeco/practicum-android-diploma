package ru.practicum.android.diploma.core.util

sealed class Resource<T>(val data: T? = null, val errorCode: Int? = null) {
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(errorCode: Int) : Resource<T>(errorCode = errorCode)
}