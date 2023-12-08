package com.workseasy.com.ui.accounts

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.workseasy.com.ui.hradmin.employeeRegistration.response.AssetDto
import com.workseasy.com.utils.NetworkHelper
import kotlinx.coroutines.launch

class PaymentViewModel: com.workseasy.com.base.BaseViewModel() {
    private  val _paymentupdateResponse: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<PaymentResponse>>> = MutableLiveData()
    val paymentupdateResponse: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<PaymentResponse>>> = _paymentupdateResponse

    private  val _spinnerResponse: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<AssetDto>>> = MutableLiveData()
    val spinnerResponse: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<AssetDto>>> = _spinnerResponse

    var repository: com.workseasy.com.repository.PaymentRepository?=null
    private var networkHelper: NetworkHelper?=null
    init {
        repository = com.workseasy.com.repository.PaymentRepository()
    }

    @SuppressLint("NewApi")
    fun paymentPost(beneficiary_name: String,
                employee_code: String,
                payment_head: String,
                payment_status: String,
                payment_date: String,
                payment_mode: String,
                transaction_ref: String,
                remark: String,
                context: Context
    )= viewModelScope.launch {
        _paymentupdateResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _paymentupdateResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.paymentInsert( beneficiary_name, employee_code, payment_head, payment_status, payment_date,
                        payment_mode, transaction_ref, remark)
                )
            } catch (exception: Exception) {
                _paymentupdateResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _paymentupdateResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun getAssets(type: String, context: Context
    )= viewModelScope.launch {
        _spinnerResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _spinnerResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getSpinnerType(type)
                )
            } catch (exception: Exception) {
                _spinnerResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _spinnerResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }


}