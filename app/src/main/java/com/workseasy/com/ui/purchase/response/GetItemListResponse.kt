package com.workseasy.com.ui.purchase.response

data class GetItemListResponse(
    val code: Int,
    val `data`: List<ItemData>,
    val message: String
)