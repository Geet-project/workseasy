package com.workseasy.com.ui.hradmin.employeeRegistration.response

data class FamilyDetailResponse(
    val code: Int,
    val `data`: ArrayList<AddFamilyData>,
    val message: String,
    val relationMasterData: List<FamilyRelationMasterData>
)