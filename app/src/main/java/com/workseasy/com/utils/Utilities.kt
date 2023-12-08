package com.workseasy.com.utils

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import com.hr.hrmanagement.R

open class Utilities {
    lateinit var mDialog: ProgressDialog

    open fun successToast(context: Context, message: String) {
        val toast1 : Toast?=null
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(
            R.layout.custom_success_toast,
            (context as Activity).findViewById(R.id.successToast)
        )
        val toast: com.workseasy.com.utils.custom.CustomTextView
        toast = layout.findViewById(R.id.toastMessage)
        toast.text = message
        toast1!!.setGravity(Gravity.BOTTOM, 0, 40)
        toast1!!.duration = Toast.LENGTH_LONG
        toast1!!.view = layout
        toast1!!.show()
    }

    open fun errorToast(context: Context, message: String) {
        val toast1 : Toast?=null

        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(
            R.layout.custom_toast_error_layout,
            (context as Activity).findViewById(R.id.toast_layout_error_root)
        )
        val toast: com.workseasy.com.utils.custom.CustomTextView
        toast = layout.findViewById(R.id.toastTextView)
        toast.text = message
        toast1!!.setGravity(Gravity.BOTTOM, 0, 40)
        toast1!!.duration = Toast.LENGTH_LONG
        toast1!!.view = layout
        toast1!!.show()
    }
    fun showLoading() {
//        hud.show()
        mDialog.show()
    }

    fun hideLoading() {
//        hud.dismiss()
        mDialog.hide()
    }


}