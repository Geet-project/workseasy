package com.workseasy.com.ui.purchase.response

data class CreateQuotationDto(
    val delivery_time: String,
    val disc: String,
    val fraight: String,
    val gst: String,
    val make: String,
    val paymentmode: String,
    val quantity: String,
    val quotation_id: String,
    val rate: String,
    val unit: String,
    val vendor: String
)