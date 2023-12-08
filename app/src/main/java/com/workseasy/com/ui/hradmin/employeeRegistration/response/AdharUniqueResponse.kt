package com.workseasy.com.ui.hradmin.employeeRegistration.response

data class AdharUniqueResponse(
    val isUniqueAadhar: Boolean,
    val message: String,
    val status: Int
)