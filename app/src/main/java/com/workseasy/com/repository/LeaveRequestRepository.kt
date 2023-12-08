package com.workseasy.com.repository

import com.hr.demoapp.network.RemoteDataSource

class LeaveRequestRepository {
    private  val client = RemoteDataSource.getInterface()
    suspend fun leaveRequest(leaveDateFrom: String?,
    leaveDateTo: String?, reason:String?,
    employee_id:Int?) = client!!.leaveRequestSubmit(leaveDateFrom, leaveDateTo, reason, employee_id)

    suspend fun getLeaveRequest(employee_id: Int?) = client!!.getLeaveRequest(employee_id)

    suspend fun getApprovalRequestList(employee_id: Int?) = client!!.getApprovalLeaveRequest(employee_id)

    suspend fun updateLeaveRequest(leave_id: Int?, status: String?, remark: String?)= client!!.updateleaveRequest(leave_id, status, remark)

    suspend fun leavePagination(page: String?, employee_id: Int?, type: String?)
    = client!!.leavePagination(page, employee_id, type)
}