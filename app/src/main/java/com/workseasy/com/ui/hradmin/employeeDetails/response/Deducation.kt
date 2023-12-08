package com.workseasy.com.ui.hradmin.employeeDetails.response

data class Deducation(
    val adv: Double,
    val epf: Double,
    val esic: Double,
    val lwf: Double,
    val tds: Double,
    val total: Double,
    val vpf: Double,
    val pt: Double,
)