package com.workseasy.com.repository

import com.hr.demoapp.network.RemoteDataSource


class HomeRepo {
    private  val client = RemoteDataSource.getInterface()
    suspend fun orgList() = client!!.getOrganizationName()
    suspend fun updateOrg(companyid: String) = client!!.updateOrganization(companyid)
    suspend fun getPermissions(userid: String) = client!!.getPermissions(userid)

}