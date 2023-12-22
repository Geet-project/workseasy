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

class RaisePrViewModel : com.workseasy.com.base.BaseViewModel(){
    private  val _raiseprSubmitResponse: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<PrSubmitResponse>>> = MutableLiveData()
    val raiseprSubmitResponse: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<PrSubmitResponse>>> = _raiseprSubmitResponse

    private  val _spinnerResponse: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<AssetDto>>> = MutableLiveData()
    val spinnerResponse: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<AssetDto>>> = _spinnerResponse

    private  val _prlistResponse: MutableLiveData<com.workseasy.com.network.DataState<PRListResponseX>> = MutableLiveData()
    val prListResponse: LiveData<com.workseasy.com.network.DataState<PRListResponseX>> = _prlistResponse

    private  val _approvedprListResponse: MutableLiveData<com.workseasy.com.network.DataState<PRListResponseX>> = MutableLiveData()
    val approvedprListResponse: LiveData<com.workseasy.com.network.DataState<PRListResponseX>> = _approvedprListResponse

    private  val _viewprResponse: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<Pr>>> = MutableLiveData()
    val viewprResponse: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<Pr>>> = _viewprResponse

    private  val _updatePrResponse: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<Pr>>> = MutableLiveData()
    val updatePrResponse: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<Pr>>> = _updatePrResponse

    private  val _grnstoreResponse: MutableLiveData<com.workseasy.com.network.DataState<CreateQuotationResponse>> = MutableLiveData()
    val grnstoreResponse: LiveData<com.workseasy.com.network.DataState<CreateQuotationResponse>> = _grnstoreResponse

    private  val _consumptionstoreResponse: MutableLiveData<com.workseasy.com.network.DataState<CreateQuotationResponse>> = MutableLiveData()
    val consumptionstoreResponse: LiveData<com.workseasy.com.network.DataState<CreateQuotationResponse>> = _consumptionstoreResponse

    private val _quotationResponse: MutableLiveData<com.workseasy.com.network.DataState<QuotationApprovedResponse>> = MutableLiveData()
    val quotationResponse : LiveData<com.workseasy.com.network.DataState<QuotationApprovedResponse>> = _quotationResponse

    private val _quotationPrResponse: MutableLiveData<com.workseasy.com.network.DataState<QuotationPrResponse>> = MutableLiveData()
    val quotationPrResponse : LiveData<com.workseasy.com.network.DataState<QuotationPrResponse>> = _quotationPrResponse

    private val _quotationUpdateResponse: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.NewResponse>> = MutableLiveData()
    val quotationUpdateResponse : LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.NewResponse>> = _quotationUpdateResponse

    private val _getItemListResponse: MutableLiveData<com.workseasy.com.network.DataState<GetItemListResponse>> = MutableLiveData()
    val getItemListResponse : LiveData<com.workseasy.com.network.DataState<GetItemListResponse>> = _getItemListResponse

    private val _getAssetsListResponse : MutableLiveData<com.workseasy.com.network.DataState<GetItemAssetsResponse>> = MutableLiveData()
    val getAssetsListResponse : LiveData<com.workseasy.com.network.DataState<GetItemAssetsResponse>> = _getAssetsListResponse

    private  val _grnItemDataResponse: MutableLiveData<com.workseasy.com.network.DataState<GrnItemDataResponse>> = MutableLiveData()
    val grnItemDataResponse: LiveData<com.workseasy.com.network.DataState<GrnItemDataResponse>> = _grnItemDataResponse

    private  val _grnPrListDataResponse: MutableLiveData<com.workseasy.com.network.DataState<GrnPoListData>> = MutableLiveData()
    val grnPrListDataResponse: LiveData<com.workseasy.com.network.DataState<GrnPoListData>> = _grnPrListDataResponse

    private  val _consumptionListDataResponse: MutableLiveData<com.workseasy.com.network.DataState<ConsumptionItemResponse>> = MutableLiveData()
    val consumptionListDataResponse: LiveData<com.workseasy.com.network.DataState<ConsumptionItemResponse>> = _consumptionListDataResponse

    private  val _quotationListResponse: MutableLiveData<com.workseasy.com.network.DataState<QuotationApprovalListResponse>> = MutableLiveData()
    val quotationListResponse: LiveData<com.workseasy.com.network.DataState<QuotationApprovalListResponse>> = _quotationListResponse


    var repository: com.workseasy.com.repository.PurchaseRepository?=null
    private var networkHelper: NetworkHelper?=null
    init {
        repository = com.workseasy.com.repository.PurchaseRepository()
    }
    @SuppressLint("NewApi")
    fun raisePr(item: RequestBody,
                make: RequestBody,
                gender: RequestBody,
                delivery: RequestBody,
                quantity: RequestBody,
                unit: RequestBody,
                remark: RequestBody,
                photo : MultipartBody.Part?,
                context: Context
    )= viewModelScope.launch {
        _raiseprSubmitResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _raiseprSubmitResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.raisePr( item, make, gender, delivery, quantity, unit, remark, photo)
                )
            } catch (exception: Exception) {
                _raiseprSubmitResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _raiseprSubmitResponse.postValue(
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

    @SuppressLint("NewApi")
    fun getPrList( context: Context
    )= viewModelScope.launch {
        _prlistResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _prlistResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getPrList()
                )
            } catch (exception: Exception) {
                _prlistResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _prlistResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun getApprovedPrList( context: Context, status: String
    )= viewModelScope.launch {
        _approvedprListResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _approvedprListResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getApprovedPrList(status)
                )
            } catch (exception: Exception) {
                _approvedprListResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _approvedprListResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }


    @SuppressLint("NewApi")
    fun viewPr( pr_id: Int?, context: Context
    )= viewModelScope.launch {
        _viewprResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _viewprResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.viewPr(pr_id)
                )
            } catch (exception: Exception) {
                _viewprResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _viewprResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }


    @SuppressLint("NewApi")
    fun updatePr( pr_id: Int?, status: String?, remark: String? ,context: Context
    )= viewModelScope.launch {
        _updatePrResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _updatePrResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.updatePr(pr_id, status, remark)
                )
            } catch (exception: Exception) {
                _updatePrResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _updatePrResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun grnStore( po : RequestBody,
                  item: RequestBody,
                  remark: RequestBody,
                  bill_no: RequestBody,
                  receipt_date: RequestBody,
                  quantity: RequestBody,
                  photo: MultipartBody.Part?,context: Context
    )= viewModelScope.launch {
        _grnstoreResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _grnstoreResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.grnStore(po, item, remark, bill_no, receipt_date, quantity, photo)
                )
            } catch (exception: Exception) {
                _grnstoreResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _grnstoreResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun consumptionStore(item: String,
                         quantity: String,
                         date: String,
                         remark: String,context: Context
    )= viewModelScope.launch {
        _consumptionstoreResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _consumptionstoreResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.consumptionStore(item,quantity, date, remark)
                )
            } catch (exception: Exception) {
                _consumptionstoreResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _consumptionstoreResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun quotationList(context: Context
    )= viewModelScope.launch {
        _quotationResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _quotationResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getQuotationList()
                )
            } catch (exception: Exception) {
                _quotationResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _quotationResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun getPrListForQuotation(context: Context,
                              pr_id: Int?
    )= viewModelScope.launch {
        _quotationPrResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _quotationPrResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getPrListForQuotation(pr_id)
                )
            } catch (exception: Exception) {
                _quotationPrResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _quotationPrResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun quotationUpdate(
        quotation_id: Int?,
        status: String,
        context: Context
    )= viewModelScope.launch {
        _quotationUpdateResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _quotationUpdateResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.updateQuotation(quotation_id, status)
                )
            } catch (exception: Exception) {
                _quotationUpdateResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _quotationUpdateResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun getItemList(
        context: Context
    )= viewModelScope.launch {
        _getItemListResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _getItemListResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getItemList()
                )
            } catch (exception: Exception) {
                _getItemListResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _getItemListResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun getAssetsItemList(
        itemId:Int,
        context: Context
    )= viewModelScope.launch {
        _getAssetsListResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _getAssetsListResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getPrAssetsList(itemId)
                )
            } catch (exception: Exception) {
                _getAssetsListResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _getAssetsListResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun getGrnItemData(context: Context) = viewModelScope.launch {
        _grnItemDataResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _grnItemDataResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getGrnItemData()
                )
            } catch (exception: Exception) {
                _grnItemDataResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _grnItemDataResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun getGrnPoData(poId: Int,context: Context) = viewModelScope.launch {
        _grnPrListDataResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _grnPrListDataResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getGrnPoData(poId)
                )
            } catch (exception: Exception) {
                _grnPrListDataResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _grnPrListDataResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun getConsumptionItemData(context: Context)= viewModelScope.launch {
        _consumptionListDataResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _consumptionListDataResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getConsumptionItemData()
                )
            } catch (exception: Exception) {
                _consumptionListDataResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _consumptionListDataResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun getQuotationForApproval(context: Context)= viewModelScope.launch {
        _quotationListResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _quotationListResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getQuotationForApproval()
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
}