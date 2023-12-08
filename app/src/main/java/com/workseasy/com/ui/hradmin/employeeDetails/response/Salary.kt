package com.workseasy.com.ui.hradmin.employeeDetails.response

data class Salary (
    val attendance: Attendance,
    val date: String,
    val deducation: Deducation,
    val earning: Earning,
    val month: String,
    val net_payble: Double,
    val total: Double,
    val year: Int
)