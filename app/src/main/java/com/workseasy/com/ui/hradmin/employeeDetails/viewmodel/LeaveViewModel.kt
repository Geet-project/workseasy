package com.workseasy.com.ui.hradmin.employeeDetails.viewmodel

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.workseasy.com.ui.hradmin.employeeDetails.response.ApprovalRequestDto
import com.workseasy.com.ui.hradmin.employeeDetails.response.LeaveRequestListDto
import com.workseasy.com.ui.hradmin.employeeDetails.response.LeaveRequestSubmitDto
import com.workseasy.com.ui.hradmin.employeeDetails.response.PaginationListDto
import com.workseasy.com.utils.NetworkHelper
import kotlinx.coroutines.launch

class LeaveViewModel : com.workseasy.com.base.BaseViewModel(){
    private  val _leavesubmitResponse: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<LeaveRequestSubmitDto>>> = MutableLiveData()
    val leavesubmitResponse: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<LeaveRequestSubmitDto>>> = _leavesubmitResponse

    private  val _leavelist: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<LeaveRequestListDto>>> = MutableLiveData()
    val leavelist: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<LeaveRequestListDto>>> = _leavelist

    private  val _approveleavelist: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<ApprovalRequestDto>>> = MutableLiveData()
    val approveleavelist: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<ApprovalRequestDto>>> = _approveleavelist

    private  val _updateleaveResponse: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<LeaveRequestSubmitDto>>> = MutableLiveData()
    val updateLeaveResponse: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<LeaveRequestSubmitDto>>> = _updateleaveResponse

    private  val _leavepaginationResponse: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<PaginationListDto>>> = MutableLiveData()
    val leavepaginationResponse: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<PaginationListDto>>> = _leavepaginationResponse



    var repository: com.workseasy.com.repository.LeaveRequestRepository?=null
    private var networkHelper: NetworkHelper?=null
    init {
        repository = com.workseasy.com.repository.LeaveRequestRepository()
    }

    @SuppressLint("NewApi")
    fun leaveSubmit(employee_id: Int?,
                    from_date: String?,
                    to_date: String?,
                    reason: String?,context: Context
    )= viewModelScope.launch {
        _leavesubmitResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _leavesubmitResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.leaveRequest(
                    from_date,
                    to_date,
                    reason, employee_id)
                )
            } catch (exception: Exception) {
                _leavesubmitResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _leavesubmitResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun getLeaveRequest(employee_id: Int?,context: Context
    )= viewModelScope.launch {
        _leavelist.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _leavelist.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getLeaveRequest(employee_id)
                )
            } catch (exception: Exception) {
                _leavelist.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _leavelist.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun getApprovalRequestList(employee_id: Int?,context: Context
    )= viewModelScope.launch {
        _approveleavelist.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _approveleavelist.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getApprovalRequestList(employee_id)
                )
            } catch (exception: Exception) {
                _approveleavelist.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _approveleavelist.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun updateLeaveRequest(leave_id: Int?,status: String?, remark: String?,context: Context
    )= viewModelScope.launch {
        _updateleaveResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _updateleaveResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.updateLeaveRequest(leave_id, status, remark)
                )
            } catch (exception: Exception) {
                _updateleaveResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _updateleaveResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun leavePaginationList(employee_id: Int?,page: String?, type: String?,context: Context
    )= viewModelScope.launch {
        _leavepaginationResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _leavepaginationResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.leavePagination(page, employee_id, type)
                )
            } catch (exception: Exception) {
                _leavepaginationResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _leavepaginationResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

}