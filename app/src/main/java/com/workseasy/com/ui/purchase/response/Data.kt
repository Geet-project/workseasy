package com.workseasy.com.ui.purchase.response

data class Data(
    val grades: List<Grade>,
    val makes: List<Make>,
    val paymentTerms: List<PaymentTerm>,
    val pr_details: PrDetailsX,
    val previousQuotations: List<Any>,
    val quotationId: Int,
    val vendors: List<Vendor>
)