package com.vlk.application.common

sealed class BaseResult<out T : Any, out U : Any> {
    data class Success<T : Any>(val data: T) : BaseResult<T, Nothing>()
    data class Error<U : Any>(val error: U, val message: String? = null) : BaseResult<Nothing, U>()
}