package com.workseasy.com.ui.purchase.response

data class GetItemAssetsResponse(
    val code: Int,
    val `data`: AssetsData,
    val message: String
)