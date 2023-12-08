package com.workseasy.com.ui.purchase.response

data class PRListResponseX(
    val `data`: ArrayList<PrData>,
    val message: String,
    val status: Int
)