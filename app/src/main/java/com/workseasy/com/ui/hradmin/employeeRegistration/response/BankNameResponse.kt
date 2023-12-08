package com.workseasy.com.ui.hradmin.employeeRegistration.response

data class BankNameResponse(
    val ADDRESS: String,
    val BANK: String,
    val BANKCODE: String,
    val BRANCH: String,
    val CENTRE: String,
    val CITY: String,
    val CONTACT: String,
    val DISTRICT: String,
    val IFSC: String,
    val IMPS: Boolean,
    val ISO3166: String,
    val MICR: String,
    val NEFT: Boolean,
    val RTGS: Boolean,
    val STATE: String,
    val SWIFT: Any,
    val UPI: Boolean
)