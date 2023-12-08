package com.workseasy.com.ui.notification

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.workseasy.com.utils.NetworkHelper
import kotlinx.coroutines.launch

class NotificationViewModel: com.workseasy.com.base.BaseViewModel() {
    private  val _notificationResponse: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<NotificationResponse>>> = MutableLiveData()
    val notificationResponse: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<NotificationResponse>>> = _notificationResponse

    var repository: com.workseasy.com.repository.NotificationRepository?=null
    private var networkHelper: NetworkHelper?=null
    init {
        repository = com.workseasy.com.repository.NotificationRepository()
    }

    @SuppressLint("NewApi")
    fun notiList(
                    context: Context
    )= viewModelScope.launch {
        _notificationResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _notificationResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getNotiList()
                )
            } catch (exception: Exception) {
                _notificationResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _notificationResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }


}