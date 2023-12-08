package com.workseasy.com.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

open class SharedPref {
    lateinit var mPrefs: SharedPreferences
    lateinit var mEditor: SharedPreferences.Editor
    var user = "users"

    constructor(context: Context?) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        mEditor = mPrefs?.edit()!!
    }

    @Synchronized
    fun saveData(key: String?, value: Any?) {
        val prefsEditor = mPrefs.edit()
        when (value) {
            is String? ->  { prefsEditor.putString(key, value) }
            is Int ->  { prefsEditor.putInt(key, value) }
            is Boolean ->  { prefsEditor.putBoolean(key, value) }
            is Float ->  { prefsEditor.putFloat(key, value) }
            is Long ->  { prefsEditor.putLong(key, value) }
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
        prefsEditor.apply()
    }

    @Synchronized
    fun getData(key: String?,defaultValue: Any?): Any? {

        return when (defaultValue) {
            is String? ->  {
                mPrefs.getString(key, defaultValue)
            }
            is Int ->  {
                mPrefs.getInt(key, defaultValue)
            }
            is Boolean ->  {
                mPrefs.getBoolean(key, defaultValue)
            }
            is Float ->  {
                mPrefs.getFloat(key, defaultValue)
            }
            is Long ->  {
                mPrefs.getLong(key, defaultValue)
            }
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    @Synchronized
    fun clearData(key: String?) {
        mPrefs.edit()?.remove(key)?.apply()
    }


    @Synchronized
    fun clearPreference()
    {
        val editor = mPrefs.edit()
        editor?.clear()
        editor?.apply()
    }
    companion object {
        const val PREFERENCE_NAME = "meverSharedPref"
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
        const val USER_ID = "user_id"
        const val USER_NAME = "user_name"
        const val EMAIL_iD = "email_id"
        const val MOBILE_NO = "mobile_no"
        const val IMAGE = "image"
        const val isLogin = "isLogin"
        const val COMPANYID = "companyid"
        const val BRANCHID = "branchid"
        const val NewOld = "new_old"
        const val TOKEN = "token"
        const val LEAVE_TYPE = "leave_type"
        const val SALARY_TYPE = "salary_type"
        const val ATTENDANCE_TYPE = "attendance_type"
        const val LOGGED_IN_USER_DATA = "LOGGED_IN_USER_DATA"
        const val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"
        private var preference: SharedPref? = null
    }
}