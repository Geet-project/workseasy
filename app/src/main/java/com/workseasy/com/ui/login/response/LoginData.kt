package com.workseasy.com.ui.login.response

data class LoginData (
    val email: String,
    val mobile: String,
    val name: String,
    val token: String,
    val user_id: Int,
    val error: String,
    val image: String,
    val leave_type: String,
    val salary_type: String,
    val attendance_type: String

    )