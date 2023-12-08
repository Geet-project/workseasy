package com.workseasy.com.ui.hradmin.employeeRegistration.response

data class SalaryDetailResponse(
    val annual_ctc_data: AnnualCtcData,
    val code: Int,
    val earningListData: ArrayList<EarningData>,
    val finalData: FinalData,
    val message: String
)