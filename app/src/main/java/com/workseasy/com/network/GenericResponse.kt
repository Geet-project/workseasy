package com.workseasy.com.network

data class GenericResponse<T>  (
    val data: T,
    val status: Int,
    val error: String?,
    val message: String?
)