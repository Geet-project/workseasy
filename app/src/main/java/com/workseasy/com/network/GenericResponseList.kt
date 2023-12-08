package com.workseasy.com.network

class GenericResponseList<T> (
    val data: ArrayList<T>,
    val status: Int,
    val error: String?,
    val message: String?
)