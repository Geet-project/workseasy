package com.workseasy.com.ui.purchase.response

import java.util.concurrent.locks.Condition

data class QuotationPoListResponse(
    val code: Int,
    val `data`: List<QuotationPoListData>,
    val message: String,
    val condition: List<CondionDataItem>,
    val previousConditions: List<PreviousConditionDataItem>
)