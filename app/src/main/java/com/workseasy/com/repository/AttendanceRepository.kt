package com.workseasy.com.repository

import com.hr.demoapp.network.RemoteDataSource
import com.workseasy.com.ui.hradmin.attendance.response.AttendanceWithLocationDto
import com.workseasy.com.ui.hradmin.attendance.response.AutoAttendanceDto
import com.workseasy.com.ui.purchase.response.CreateQuotationDto

class AttendanceRepository {
    private val client = RemoteDataSource.getInterface()
    suspend fun add_daily_attendance(
        date: String?,
        punch_in: String?,
        punch_out: String?,
        employee_id: Int?,
    ) = client!!.add_daily_attendance(date, punch_in, punch_out, employee_id)

    suspend fun add_monthly_attendance(
        wd: String?,
        ot: String?,
        pd: String?,
        el: String?,
        cl: String?,
        year: String?,
        month: String?,
        sl: String?,
        hd: String?,
        wo: String?,
        employee_id: Int?
    ) = client!!.add_monthly_attendance(wd, ot, pd, el, cl, year, month, sl, hd, wo, employee_id)


    suspend fun add_monthly_attendance2(
        ot: String?,
        pd: String?,
        year: String?,
        month: String?,
        employee_id: Int?
    ) = client!!.add_monthly_attendance2(ot, pd, year, month, employee_id)

    suspend fun attendancePaginationDaily(page: String?, employee_id: Int?, type: String?) =
        client!!.attendancePaginationDaily(page, employee_id, type)

    suspend fun attendancePaginationMonthly(page: String?, employee_id: Int?, type: String?) =
        client!!.attendancePaginationMonthly(page, employee_id, type)

    suspend fun attendancePaginationAttendance3(page: String?, employee_id: Int?, type: String?) =
        client!!.attendancePaginationAttendance3(page, employee_id, type)

    suspend fun attendancewithLocation(attendanceWithLocationDto: AttendanceWithLocationDto) =
        client!!.attendancewithLocation(attendanceWithLocationDto)

    suspend fun getattendancewithLocation(
        employee_id: Int,
        date: String
    ) = client!!.getattendancewithLocation(employee_id, date)

    suspend fun employeeAutoAttendance(
        autoAttendanceDto: AutoAttendanceDto
    ) = client!!.employeeAutoAttendance(autoAttendanceDto)

    suspend fun createQuotation(
        createQuotationDto: CreateQuotationDto
    ) = client!!.createQuotation(createQuotationDto)
}