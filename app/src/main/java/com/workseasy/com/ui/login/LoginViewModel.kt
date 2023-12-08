package com.workseasy.com.ui.login

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.workseasy.com.ui.hradmin.employeeRegistration.response.AdharDataResponse
import com.workseasy.com.ui.hradmin.employeeRegistration.response.AdharUniqueDto
import com.workseasy.com.ui.hradmin.employeeRegistration.response.AdharUniqueResponse
import com.workseasy.com.ui.hradmin.employeeRegistration.response.SignupDto

import com.workseasy.com.ui.login.request.LoginRequest
import com.workseasy.com.ui.login.response.DashboardResponse
import com.workseasy.com.ui.login.response.LoginData
import com.workseasy.com.utils.NetworkHelper
import kotlinx.coroutines.launch

open class LoginViewModel: com.workseasy.com.base.BaseViewModel() {
    private  val _loginResponse: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<LoginData>>> = MutableLiveData()
    val loginResponse: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<LoginData>>> = _loginResponse

    private  val _successResponse: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<SignupDto>>> = MutableLiveData()
    val sucessResponse: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<SignupDto>>> = _successResponse

    private  val _adharcheckResponse: MutableLiveData<com.workseasy.com.network.DataState<AdharUniqueResponse>> = MutableLiveData()
    val adharcheckResponse: LiveData<com.workseasy.com.network.DataState<AdharUniqueResponse>> = _adharcheckResponse

    private  val _adhardetailResponse: MutableLiveData<com.workseasy.com.network.DataState<AdharDataResponse>> = MutableLiveData()
    val adhardetailResponse: LiveData<com.workseasy.com.network.DataState<AdharDataResponse>> = _adhardetailResponse

    var repository: com.workseasy.com.repository.LoginRepository?=null
    private var networkHelper: NetworkHelper?=null
    init {
        repository = com.workseasy.com.repository.LoginRepository()
    }

    @SuppressLint("NewApi")
    fun managerLogin(loginRequest: LoginRequest, context: Context)= viewModelScope.launch {
        _loginResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _loginResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.loginManager(loginRequest)
                )
            } catch (exception: Exception) {
                _loginResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _loginResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }

    }

    @SuppressLint("NewApi")
    fun checkEmployee(adharnumber: String, context: Context)= viewModelScope.launch {
        _successResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _successResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.checkEmployee(adharnumber)
                )
            } catch (exception: Exception) {
                _successResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _successResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun checkAdhar(adharUniqueDto: AdharUniqueDto, context: Context)= viewModelScope.launch {
        _adharcheckResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _adharcheckResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.checkAdhar(adharUniqueDto)
                )
            } catch (exception: Exception) {
                _adharcheckResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _adharcheckResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }


    @SuppressLint("NewApi")
    fun getAdharDetail(adharnumber: String, context: Context)= viewModelScope.launch {
        _adhardetailResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _adhardetailResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getAdharDetail(adharnumber)
                )
            } catch (exception: Exception) {
                _adhardetailResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _adhardetailResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

}