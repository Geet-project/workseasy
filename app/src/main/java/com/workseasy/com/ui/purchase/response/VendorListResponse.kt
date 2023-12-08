package com.workseasy.com.ui.purchase.response

data class VendorListResponse(
    val code: Int,
    val `data`: List<VendorListData>,
    val message: String
)