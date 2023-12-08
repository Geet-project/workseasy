package com.workseasy.com.ui.hradmin.employeeRegistration.response

data class WorkExpDto(
    val date_of_join: String,
    val date_of_leave: String,
    val designation: String,
    val employee_id: Int,
    val name: String,
    val reason_of_leave: String
)