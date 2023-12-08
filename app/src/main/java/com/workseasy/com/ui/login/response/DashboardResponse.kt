package com.workseasy.com.ui.login.response

import com.workseasy.com.ui.hradmin.employeeDetails.response.AdvanceSalary

data class DashboardResponse(
    val `data`:  ArrayList<DataX>?,
    val message: String,
    val status: Int
)