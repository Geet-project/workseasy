package com.workseasy.com.ui.hradmin.employeeRegistration.response

data class AddFamilyDto(
    val Relation: String,
    val employee_id: Int,
    val name: String,
    val dob: String
)