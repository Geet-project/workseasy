package com.workseasy.com.ui.purchase.response

data class GrnItemDataResponse(
    val code: Int,
    val `data`: List<GrnItemDataItem>,
    val message: String
)