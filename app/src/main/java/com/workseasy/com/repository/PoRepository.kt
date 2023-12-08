package com.workseasy.com.repository

import com.hr.demoapp.network.RemoteDataSource
import com.workseasy.com.ui.purchase.response.CreatePoDto

class PoRepository {
    private val client = RemoteDataSource.getInterface()

    suspend fun getVendorList() = client!!.getVendorsList()

    suspend fun getQuotationListPo(vendorId : Int?) = client!!.getQuotationListPo(vendorId)

    suspend fun createPoDto(createPoDto: CreatePoDto) = client!!.createPo(createPoDto)




}