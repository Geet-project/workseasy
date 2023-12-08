package com.workseasy.com.repository

import com.hr.demoapp.network.RemoteDataSource

class ViewSalaryRepository {
    private  val client = RemoteDataSource.getInterface()
    suspend fun viewSalary(employee_id: Int?) = client!!.getviewSalaryList(employee_id)
}