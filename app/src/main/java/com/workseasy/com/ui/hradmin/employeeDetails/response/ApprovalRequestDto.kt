package com.workseasy.com.ui.hradmin.employeeDetails.response

data class ApprovalRequestDto(
    val leaves: ArrayList<LeaveRequestListData>,
    val leaves_count: LeavesCount
)