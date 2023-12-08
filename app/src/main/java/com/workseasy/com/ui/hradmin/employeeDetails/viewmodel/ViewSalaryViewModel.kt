package com.workseasy.com.ui.hradmin.employeeDetails.viewmodel

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.workseasy.com.ui.hradmin.employeeDetails.response.ViewSalaryDto
import com.workseasy.com.utils.NetworkHelper
import kotlinx.coroutines.launch

class ViewSalaryViewModel : com.workseasy.com.base.BaseViewModel(){
    private  val _salarylist: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<ViewSalaryDto>>> = MutableLiveData()
    val salarylist: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<ViewSalaryDto>>> = _salarylist

    var repository: com.workseasy.com.repository.ViewSalaryRepository?=null
    private var networkHelper: NetworkHelper?=null
    init {
        repository = com.workseasy.com.repository.ViewSalaryRepository()
    }

    @SuppressLint("NewApi")
    fun viewSalaryList(employee_id: Int?,context: Context
    )= viewModelScope.launch {
        _salarylist.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _salarylist.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.viewSalary(employee_id)
                )
            } catch (exception: Exception) {
                _salarylist.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _salarylist.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }


}