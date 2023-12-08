package com.workseasy.com.utils

import android.app.Application
import android.content.Context

class ApplicationClass: Application() {
    override fun onCreate() {
        super.onCreate()
        ApplicationClass.appContext = applicationContext
    }

    companion object {
        lateinit var appContext: Context
    }
}