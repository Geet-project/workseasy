package com.workseasy.com.ui.purchase.viewmodel

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.workseasy.com.ui.hradmin.employeeRegistration.response.AssetDto
import com.workseasy.com.ui.purchase.response.*
import com.workseasy.com.utils.NetworkHelper
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class QuotationViewModel : com.workseasy.com.base.BaseViewModel(){
    private  val _createquotationResponse: MutableLiveData<com.workseasy.com.network.DataState<CreateQuotationResponse>> = MutableLiveData()
    val createquotationResponse: LiveData<com.workseasy.com.network.DataState<CreateQuotationResponse>> = _createquotationResponse

    var repository: com.workseasy.com.repository.PurchaseRepository?=null
    private var networkHelper: NetworkHelper?=null
    init {
        repository = com.workseasy.com.repository.PurchaseRepository()
    }
    @SuppressLint("NewApi")
    fun createQuotation(createQuotationDto: CreateQuotationDto,
                context: Context
    )= viewModelScope.launch {
        _createquotationResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _createquotationResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.createQuotation( createQuotationDto)
                )
            } catch (exception: Exception) {
                _createquotationResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _createquotationResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }
}