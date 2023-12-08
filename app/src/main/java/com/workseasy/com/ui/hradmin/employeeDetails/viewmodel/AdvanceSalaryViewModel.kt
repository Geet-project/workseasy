package com.workseasy.com.ui.hradmin.employeeDetails.viewmodel

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.workseasy.com.ui.hradmin.employeeDetails.response.AdvSalaryListDto
import com.workseasy.com.ui.hradmin.employeeDetails.response.AdvanceSalary
import com.workseasy.com.ui.hradmin.employeeDetails.response.ApprovalSalaryDto
import com.workseasy.com.utils.NetworkHelper
import kotlinx.coroutines.launch

class AdvanceSalaryViewModel : com.workseasy.com.base.BaseViewModel(){
    private  val _advlist: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<AdvSalaryListDto>>> = MutableLiveData()
    val advlist: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<AdvSalaryListDto>>> = _advlist

    private  val _advsubmitResponse: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<AdvSalaryListDto>>> = MutableLiveData()
    val advsubmitResponse: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<AdvSalaryListDto>>> = _advsubmitResponse

    private  val _approvallistRespone: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<ApprovalSalaryDto>>> = MutableLiveData()
    val approvallistRespone: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<ApprovalSalaryDto>>> = _approvallistRespone

    private  val _updatesalaryResponse: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<AdvanceSalary>>> = MutableLiveData()
    val updatesalaryResponse: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<AdvanceSalary>>> = _updatesalaryResponse

    var repository: com.workseasy.com.repository.AdvanceSalaryRequestRepository?=null
    private var networkHelper: NetworkHelper?=null
    init {
        repository = com.workseasy.com.repository.AdvanceSalaryRequestRepository()
    }

    @SuppressLint("NewApi")
    fun getLeaveRequest(employee_id: Int?,context: Context
    )= viewModelScope.launch {
        _advlist.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _advlist.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getAdvRequest(employee_id)
                )
            } catch (exception: Exception) {
                _advlist.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _advlist.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun advSubmit(employee_id: Int?,
                    amount: String?,
                    maximumdeduction: String?,
                    reason: String?,context: Context
    )= viewModelScope.launch {
        _advsubmitResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _advsubmitResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.advRequest(
                        employee_id,
                        amount,
                        reason, maximumdeduction
                    )
                )
            } catch (exception: Exception) {
                _advsubmitResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _advsubmitResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun advApprovalSalaryList(employee_id: Int?,
                  context: Context
    )= viewModelScope.launch {
        _approvallistRespone.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _approvallistRespone.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getApprovalSalaryList(
                        employee_id,
                    )
                )
            } catch (exception: Exception) {
                _approvallistRespone.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _approvallistRespone.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun updateSalaryRequest(advance_salary_id: Int?,status: String?, remark: String?,context: Context
    )= viewModelScope.launch {
        _updatesalaryResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _updatesalaryResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.updateSalaryRequest(advance_salary_id, status, remark)
                )
            } catch (exception: Exception) {
                _updatesalaryResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _updatesalaryResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }
}