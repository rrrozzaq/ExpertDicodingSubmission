package com.rrrozzaq.core.utils

sealed class Async<out T> {
    data object Loading : Async<Nothing>()
    data class Success<out T>(val data: T) : Async<T>()
    data class Error(val message: String) : Async<Nothing>()
}
