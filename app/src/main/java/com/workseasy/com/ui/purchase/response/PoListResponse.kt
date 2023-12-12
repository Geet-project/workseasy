package com.workseasy.com.ui.purchase.response

data class PoListResponse(
    val data: ArrayList<PoDataItem>,
    val code: Int,
    val message: String
)