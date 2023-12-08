package com.workseasy.com.ui.purchase.viewmodel

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.workseasy.com.network.DataState
import com.workseasy.com.network.GenericResponse
import com.workseasy.com.repository.PoRepository
import com.workseasy.com.ui.purchase.response.CreatePoDto
import com.workseasy.com.ui.purchase.response.CreateQuotationResponse
import com.workseasy.com.ui.purchase.response.PrSubmitResponse
import com.workseasy.com.ui.purchase.response.QuotationPoListResponse
import com.workseasy.com.ui.purchase.response.VendorListResponse
import com.workseasy.com.utils.NetworkHelper
import kotlinx.coroutines.launch

class CreatePoViewModel: com.workseasy.com.base.BaseViewModel() {
    private  val _vendorListResponse: MutableLiveData<DataState<VendorListResponse>> = MutableLiveData()
    val vendorListResponse: LiveData<DataState<VendorListResponse>> = _vendorListResponse

    private  val _quotationListResponse: MutableLiveData<DataState<QuotationPoListResponse>> = MutableLiveData()
    val quotationListResponse: LiveData<DataState<QuotationPoListResponse>> = _quotationListResponse

    private  val _createPoResponse: MutableLiveData<DataState<CreateQuotationResponse>> = MutableLiveData()
    val createPoResponse: LiveData<DataState<CreateQuotationResponse>> = _createPoResponse


    var repository: PoRepository?=null

    private var networkHelper: NetworkHelper?=null

    init {
        repository = PoRepository()
    }


    @SuppressLint("NewApi")
    fun getVendorList(context: Context) = viewModelScope.launch {
        _vendorListResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _vendorListResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getVendorList()
                )
            } catch (exception: Exception) {
                _vendorListResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _vendorListResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }


    @SuppressLint("NewApi")
    fun getPoQuotationList(vendorId: Int?, context: Context
    )= viewModelScope.launch {
        _quotationListResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _quotationListResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getQuotationListPo(vendorId)
                )
            } catch (exception: Exception) {
                _quotationListResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _quotationListResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun createPoDto(createPoDto: CreatePoDto, context: Context
    )= viewModelScope.launch {
        _createPoResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _createPoResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.createPoDto(createPoDto)
                )
            } catch (exception: Exception) {
                _createPoResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _createPoResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

}