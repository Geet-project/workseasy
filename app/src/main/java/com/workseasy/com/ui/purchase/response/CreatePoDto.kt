package com.workseasy.com.ui.purchase.response

data class CreatePoDto(
    val quotationList: ArrayList<Int>,
    val conditions: ArrayList<Int>,
    val vendorId: String
)