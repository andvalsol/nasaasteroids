package com.udacity.asteroidradar.api

import java.lang.Exception

// Result class that will handle incoming network operations
sealed class Result<out T: Any> {
    data class Success<out T: Any>(val value: T): Result<T>()
    data class Error<out T: Any>(val exception: Exception): Result<T>()
    object Loading: Result<Nothing>()
}