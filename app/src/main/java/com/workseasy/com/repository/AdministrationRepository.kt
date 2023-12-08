package com.workseasy.com.repository

import com.hr.demoapp.network.RemoteDataSource

class AdministrationRepository() {
    private  val client = RemoteDataSource.getInterface()
    suspend fun changePassword(oldpass: String,newpass: String, confirmpass: String)
    = client!!.changePassword(oldpass, newpass, confirmpass)

    suspend fun changeMPin(oldpin: String,newpin: String, confirmpin: String)
            = client!!.changeMPin(oldpin, newpin, confirmpin)

    suspend fun cmsApi(page: String?)
            = client!!.cmsApi(page)


}