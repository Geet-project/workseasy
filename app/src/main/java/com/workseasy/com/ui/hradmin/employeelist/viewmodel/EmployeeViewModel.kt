package com.workseasy.com.ui.hradmin.employeelist.viewmodel

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.workseasy.com.ui.hradmin.employeelist.response.EmpListDto
import com.workseasy.com.utils.NetworkHelper
import kotlinx.coroutines.launch

class EmployeeViewModel: com.workseasy.com.base.BaseViewModel() {
    private  val _emplistResponse: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<EmpListDto>>> = MutableLiveData()
    val emplistResponse: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<EmpListDto>>> = _emplistResponse

    private  val _empSearchResponse: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<EmpListDto>>> = MutableLiveData()
    val empSearchResponse: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<EmpListDto>>> = _empSearchResponse
    var repository: com.workseasy.com.repository.EmployeeListRepository?=null
    private var networkHelper: NetworkHelper?=null
    init {
        repository = com.workseasy.com.repository.EmployeeListRepository()
    }

    @SuppressLint("NewApi")
    fun employeeList(
        companyid: String,
        branchid: String,
        context: Context
    )= viewModelScope.launch {
        _emplistResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _emplistResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.employeeList(companyid, branchid )
                )
            } catch (exception: Exception) {
                _emplistResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _emplistResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }

    }

    @SuppressLint("NewApi")
    fun empSearch(search: String, context: Context
    )= viewModelScope.launch {
        _empSearchResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _empSearchResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.empSearch(search )
                )
            } catch (exception: Exception) {
                _empSearchResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _empSearchResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }

    }


}
