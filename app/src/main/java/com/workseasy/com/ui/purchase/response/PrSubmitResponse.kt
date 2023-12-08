package com.workseasy.com.ui.purchase.response

data class PrSubmitResponse(
    val created_at: String,
    val delivery: String,
    val gender: String,
    val id: Int,
    val image: Image,
    val item: String,
    val make: String,
    val quantity: String,
    val remark: String,
    val unit: String,
    val updated_at: String
)