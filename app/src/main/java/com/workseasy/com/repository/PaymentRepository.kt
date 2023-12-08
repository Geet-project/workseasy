package com.workseasy.com.repository

import com.hr.demoapp.network.RemoteDataSource

class PaymentRepository {
    private  val client = RemoteDataSource.getInterface()
    suspend fun paymentInsert(beneficiary_name: String,
                              employee_code: String,
                              payment_head: String,
                              payment_status: String,
                              payment_date: String,
                              payment_mode: String,
                              transaction_ref: String,
                              remark: String)
            = client!!.paymentPost(beneficiary_name, employee_code, payment_head, payment_status, payment_date,
    payment_mode, transaction_ref, remark)

    suspend fun getSpinnerType(type: String) = client!!.getAssets(type)

}