package com.workseasy.com.ui.accounts

data class PaymentResponse(
    val beneficiary_name: String,
    val created_at: String,
    val employee_code: String,
    val id: Int,
    val payment_date: String,
    val payment_head: String,
    val payment_mode: String,
    val payment_status: String,
    val remark: String,
    val transaction_ref: String,
    val updated_at: String
)