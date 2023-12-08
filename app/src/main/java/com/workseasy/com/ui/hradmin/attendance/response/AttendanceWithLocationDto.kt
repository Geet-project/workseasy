package com.workseasy.com.ui.hradmin.attendance.response

data class AttendanceWithLocationDto(
    val date: String,
    val employee_id: Int,
    val location: String,
    val punch_in: String,
    val punch_out: String,
    val punch_type: String,
    val type: String
)