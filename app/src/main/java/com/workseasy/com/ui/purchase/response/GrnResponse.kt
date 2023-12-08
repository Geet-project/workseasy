package com.workseasy.com.ui.purchase.response

data class GrnResponse(
    val bill_no: String,
    val bill_photo: String,
    val created_at: String,
    val id: Int,
    val item: String,
    val po: String,
    val quntity: String,
    val receipt_date: String,
    val remark: String,
    val updated_at: String
)