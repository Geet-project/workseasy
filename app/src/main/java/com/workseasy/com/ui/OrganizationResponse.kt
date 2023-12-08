package com.workseasy.com.ui

data class OrganizationResponse(
    val companies: ArrayList<Company>,
    val message: String,
    val status: Int
)