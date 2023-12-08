package com.workseasy.com

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.workseasy.com.network.GenericResponse
import com.workseasy.com.ui.OrganizationResponse
import com.workseasy.com.ui.hradmin.employeeRegistration.response.SignupDto
import com.workseasy.com.ui.login.response.DashboardResponse
import com.workseasy.com.utils.NetworkHelper
import kotlinx.coroutines.launch

class HomeViewModel: com.workseasy.com.base.BaseViewModel() {
    private  val _homeResponse: MutableLiveData<com.workseasy.com.network.DataState<OrganizationResponse>> = MutableLiveData()
    val homeResponse: LiveData<com.workseasy.com.network.DataState<OrganizationResponse>> = _homeResponse

    private  val _orgResponse: MutableLiveData<com.workseasy.com.network.DataState<GenericResponse<SignupDto>>> = MutableLiveData()
    val orgResponse: LiveData<com.workseasy.com.network.DataState<GenericResponse<SignupDto>>> = _orgResponse


    private  val _permissionResponse: MutableLiveData<com.workseasy.com.network.DataState<DashboardResponse>> = MutableLiveData()
    val permissionResponse: LiveData<com.workseasy.com.network.DataState<DashboardResponse>> = _permissionResponse

    var repository: com.workseasy.com.repository.HomeRepo?=null
    private var networkHelper: NetworkHelper?=null
    init {
        repository = com.workseasy.com.repository.HomeRepo()
    }

    @SuppressLint("NewApi")
    fun orgList(context: Context)= viewModelScope.launch {
        _homeResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)

        if (networkHelper!!.isNetworkConnected()) {
            try {
                _homeResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.orgList()
                )
            } catch (exception: Exception) {
                _homeResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _homeResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun orgUpdate(companyid: String, context: Context)= viewModelScope.launch {
        _orgResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _orgResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.updateOrg(companyid)
                )
            } catch (exception: Exception) {
                _orgResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _orgResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun getPermissions(id: String, context: Context)= viewModelScope.launch {
        _permissionResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _permissionResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getPermissions(id)
                )
            } catch (exception: Exception) {
                _permissionResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _permissionResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }
}