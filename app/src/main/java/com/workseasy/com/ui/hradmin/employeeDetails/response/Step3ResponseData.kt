package com.workseasy.com.ui.hradmin.employeeDetails.response

data class Step3ResponseData(
    val category: List<Category>,
    val code: Int,
    val companyShift: List<String>,
    val gradeList: List<String>,
    val message: String,
    val paymentMode: List<String>,
    val userData: UserData,
    val weekDays: List<WeekDay>
)