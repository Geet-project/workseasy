package com.workseasy.com.ui.hradmin.employeeDetails.response

data class LeaveRequestSubmitDto(
    val created_at: String,
    val employee_id: String,
    val from_date: String,
    val id: Int,
    val reason: String,
    val status: String,
    val to_date: String,
    val updated_at: String
)