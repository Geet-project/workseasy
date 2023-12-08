package com.workseasy.com.network

data class NewResponse  (
    val code: Int,
    val status: Int,
    val error: String,
    val message: String
)