package com.workseasy.com.ui.purchase.response

data class ConsumptionItemResponse(
    val code: Int,
    val data: List<ConsumptionDataItem>,
    val message: String
)