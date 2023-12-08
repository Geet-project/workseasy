package com.workseasy.com.ui.login.request

import com.google.gson.annotations.SerializedName

data class LoginRequest
    (@SerializedName("mobile") val mobile: String,
     @SerializedName("mpin") val mpin: String)