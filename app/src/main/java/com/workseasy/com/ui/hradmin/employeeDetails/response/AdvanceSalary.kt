package com.workseasy.com.ui.hradmin.employeeDetails.response

data class AdvanceSalary(
    val amount: String,
    val date: String,
    val designation: Any,
    val employee_code: Any,
    val employee_id: Int,
    val id: Int,
    val maximum_deduction_per_month: String,
    val mob_no: String,
    val name: String,
    val photo: String,
    val reason: String,
    val salary_payable_till_date: String,
    val status: String,
    val type: String,
    val remark: String
)