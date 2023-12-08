package com.workseasy.com.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi


open class NetworkHelper constructor(
     private val context: Context
) {

    @RequiresApi(Build.VERSION_CODES.M)
    open fun isNetworkConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities =
            connectivityManager.activeNetwork ?: return false
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(
                networkCapabilities) ?: return false

        return when {
            activeNetwork.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(
                NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(
                NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}