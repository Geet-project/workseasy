package com.workseasy.com.network

sealed class DataState<out R> {
    data class Success<out T>(val data: T) : com.workseasy.com.network.DataState<T>()
    data class Error(val exception: Exception) : com.workseasy.com.network.DataState<Nothing>()
    object Loading : com.workseasy.com.network.DataState<Nothing>()

}