package com.workseasy.com.ui.purchase.response

data class QuotationApprovalListResponse(
    val code: Int,
    val `data`: ArrayList<QuotationApprovalListDataItem>,
    val message: String
)