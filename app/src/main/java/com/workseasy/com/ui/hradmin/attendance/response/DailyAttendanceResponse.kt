package com.workseasy.com.ui.hradmin.attendance.response

data class DailyAttendanceResponse(
    val date: String,
    val designation: String,
    val emp_code: Any?,
    val employee_id: Int,
    val id: Int,
    val mob_no: String,
    val name: String,
    val photo: String,
    val punch_in: String,
    val punch_out: String
)