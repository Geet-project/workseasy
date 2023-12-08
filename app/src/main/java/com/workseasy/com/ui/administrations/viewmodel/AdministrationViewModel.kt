package com.workseasy.com.ui.administrations.viewmodel

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.workseasy.com.ui.cms.response.CmsDto
import com.workseasy.com.utils.NetworkHelper
import kotlinx.coroutines.launch

class AdministrationViewModel : com.workseasy.com.base.BaseViewModel(){
    private  val _changepasswrdResponse: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.NewResponse>> = MutableLiveData()
    val changepasswordResponse: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.NewResponse>> = _changepasswrdResponse


    private  val _changepinResponse: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.NewResponse>> = MutableLiveData()
    val changepinResponse: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.NewResponse>> = _changepinResponse

    private  val _cmsResponse: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<CmsDto>>> = MutableLiveData()
    val cmsResponse: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<CmsDto>>> = _cmsResponse

    var repository: com.workseasy.com.repository.AdministrationRepository?=null
    private var networkHelper: NetworkHelper?=null
    init {
        repository = com.workseasy.com.repository.AdministrationRepository()
    }

    @SuppressLint("NewApi")
    fun changePassword(
                       oldpass: String,
                       newpass: String,
                       confirmpass: String,
                       context: Context
    )= viewModelScope.launch {
        _changepasswrdResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _changepasswrdResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.changePassword(oldpass, newpass, confirmpass)
                )
            } catch (exception: Exception) {
                _changepasswrdResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _changepasswrdResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun changePin(
        oldpin: String,
        newpin: String,
        confirmpin: String,
        context: Context
    )= viewModelScope.launch {
        _changepinResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _changepinResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.changeMPin(oldpin, newpin, confirmpin)
                )
            } catch (exception: Exception) {
                _changepinResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _changepinResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun cmsApi(
        page: String?,
        context: Context
    )= viewModelScope.launch {
        _cmsResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _cmsResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.cmsApi(page)
                )
            } catch (exception: Exception) {
                _cmsResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _cmsResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }
}