package com.workseasy.com.ui.hradmin.employeeRegistration.response

data class AddFamilyData(
    val dob: String,
    val employee_id: Int,
    val id: Int,
    val is_default: Any,
    val name: String,
    val relation: String
)