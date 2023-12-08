package com.workseasy.com.repository

import com.hr.demoapp.network.RemoteDataSource
import com.workseasy.com.ui.hradmin.employeeRegistration.response.AdharUniqueDto
import com.workseasy.com.ui.login.request.LoginRequest

class LoginRepository {
    private  val client = RemoteDataSource.getInterface()
    suspend fun loginManager(loginRequest: LoginRequest) = client!!.managerLogin(loginRequest)
    suspend fun checkEmployee(adharnumber: String) = client!!.checkEmployee(adharnumber)
    suspend fun checkAdhar(adharUniqueDto: AdharUniqueDto) = client!!.checkAdhar(adharUniqueDto)
    suspend fun getAdharDetail(adharnumber: String) = client!!.getAdharDetail(adharnumber)

}
