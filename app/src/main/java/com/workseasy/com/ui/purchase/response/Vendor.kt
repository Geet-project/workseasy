package com.workseasy.com.ui.purchase.response

data class Vendor(
    val bank_ac: String,
    val bank_name: String,
    val category: String,
    val created_at: String,
    val email: String,
    val gst: String,
    val id: Int,
    val ifsc: String,
    val mobile: String,
    val name: String,
    val pan: String,
    val pincode: String,
    val updated_at: String,
    val vendor_address: String,
    val vendor_code: String,
    val vendor_name: String
)