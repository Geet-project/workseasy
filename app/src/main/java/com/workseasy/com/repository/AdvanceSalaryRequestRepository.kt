package com.workseasy.com.repository

import com.hr.demoapp.network.RemoteDataSource

class AdvanceSalaryRequestRepository {
    private  val client = RemoteDataSource.getInterface()
    suspend fun advRequest(employee_id:Int?,
                           amount: String?,
                           reason: String?, maximum_deduction_per_month:String?,
                                ) = client!!.advRequestSubmit(employee_id, amount, reason, maximum_deduction_per_month)

    suspend fun getAdvRequest(employee_id: Int?) = client!!.getAdvRequest(employee_id)

    suspend fun getApprovalSalaryList(employee_id: Int?) = client!!.getApprovalSalaryRequest(employee_id)

    suspend fun updateSalaryRequest(advance_salary_id: Int?, remark: String?, status: String?)= client!!.updatesalaryRequest(advance_salary_id, status, remark)


}