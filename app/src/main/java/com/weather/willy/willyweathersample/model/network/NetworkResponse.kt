package com.weather.willy.willyweathersample.model.network

sealed class NetworkResponse<out R> {

    data class Success<out T>(val data: T) : NetworkResponse<T>()

    data class Failure(val errorResponse: ErrorResponse? = null) : NetworkResponse<Nothing>()

    object Loading : NetworkResponse<Nothing>()
}

val NetworkResponse<*>.succeeded
    get() = this is NetworkResponse.Success && data != null