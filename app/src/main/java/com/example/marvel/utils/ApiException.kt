package com.example.marvel.utils

sealed class ApiException<T> {

    data class Success <T>(val value: T) : ApiException<T>()
    class Loading <T> : ApiException<T>()
    class Empty<T> :ApiException<T>()
    data class Error<T>(val message: String?): ApiException<T>()

}