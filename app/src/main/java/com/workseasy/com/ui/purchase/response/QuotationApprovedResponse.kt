package com.workseasy.com.ui.purchase.response

data class QuotationApprovedResponse(
    val code: Int,
    val `data`: ArrayList<QuotationApprovedListData?>,
    val message: String
)