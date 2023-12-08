package com.workseasy.com.repository

import com.hr.demoapp.network.RemoteDataSource

class NotificationRepository {
    private  val client = RemoteDataSource.getInterface()
    suspend fun getNotiList()
            = client!!.getNotification()
}