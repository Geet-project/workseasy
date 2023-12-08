package com.hr.demoapp.network

import android.app.Application
import android.content.Context
import com.workseasy.com.utils.ApplicationClass
import com.workseasy.com.utils.SharedPref
import hilt_aggregated_deps._dagger_hilt_android_internal_modules_ApplicationContextModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RemoteDataSource {
    val BASE_URL = "http://erp.workseasy.in/"
    public final var retrofit: Retrofit? = null
    var authToken: String?=null
    var sharedPref: SharedPref?=null

    fun getInterface(): ApiInterface? {
        sharedPref = SharedPref(ApplicationClass.appContext)
        authToken = sharedPref!!.getData(SharedPref.TOKEN, "").toString()
          val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor {
                 chain -> chain.proceed(chain.request().newBuilder().also {
                     it.addHeader("Authorization", "Bearer $authToken")
            }.build())
            }
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit?.create(ApiInterface::class.java)
    }
}