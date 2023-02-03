package com.test_crypto.domain


sealed class DataState<out R> {
    object Loading : DataState<Nothing>()
    data class Success<out T>(val data: T) : DataState<T>()
    data class Error(val error: Failure) : DataState<Nothing>()
}

fun DataState<*>.isSuccess() = this is DataState.Success
fun DataState<*>.isError() = this is DataState.Error
fun <T> DataState<T>.getOrElse(default : T) : T {
    return if(this is DataState.Success){
         data
    } else {
        default
    }
}

fun <T> DataState<T>.getOrNull() : T? {
    return if(this is DataState.Success){
        data
    } else {
        null
    }
}

object Empty