package com.workseasy.com.ui.hradmin.employeelist.response

data class Employee(
    val designation: String,
    val employee_code: String,
    val id: Int,
    val mob_no: String,
    val name: String,
    val photo: String,
    val form_type: Int,
    val department: String,
    val payCode: String
)