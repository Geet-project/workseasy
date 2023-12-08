package com.workseasy.com.repository

import com.hr.demoapp.network.RemoteDataSource

class EmployeeListRepository {
    private  val client = RemoteDataSource.getInterface()
    suspend fun employeeList(companyid: String, branchid: String) = client!!.getEmployeeList(companyid,
    branchid)

    suspend fun empSearch(search: String) = client!!.empSearch(search)
}