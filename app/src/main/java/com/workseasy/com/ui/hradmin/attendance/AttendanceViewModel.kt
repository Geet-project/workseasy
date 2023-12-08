package com.workseasy.com.ui.hradmin.attendance

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.workseasy.com.network.NewResponse
import com.workseasy.com.ui.hradmin.attendance.response.Attendance3Response
import com.workseasy.com.ui.hradmin.attendance.response.AttendanceWithLocationDto
import com.workseasy.com.ui.hradmin.attendance.response.AttendanceWithLocationResponse
import com.workseasy.com.ui.hradmin.attendance.response.AutoAttendanceDto
import com.workseasy.com.ui.hradmin.attendance.response.DailyAttendanceResponse
import com.workseasy.com.ui.hradmin.attendance.response.GetAttendanceWithLocationResponse
import com.workseasy.com.ui.hradmin.attendance.response.MonthlyAttendanceResponse
import com.workseasy.com.ui.hradmin.attendance.response.PaginationResponse
import com.workseasy.com.utils.NetworkHelper
import kotlinx.coroutines.launch

class AttendanceViewModel : com.workseasy.com.base.BaseViewModel() {
    private val _attendance1Response: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.NewResponse>> =
        MutableLiveData()
    val attendance1Response: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.NewResponse>> =
        _attendance1Response

    private val _attendance2Response: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.NewResponse>> =
        MutableLiveData()
    val attendance2Response: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.NewResponse>> =
        _attendance2Response

    private val _attendance3Response: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.NewResponse>> =
        MutableLiveData()
    val attendance3Response: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.NewResponse>> =
        _attendance3Response

    private val _paginationResponseDaily: MutableLiveData<com.workseasy.com.network.DataState<PaginationResponse>> =
        MutableLiveData()
    val paginationResponseDaily: LiveData<com.workseasy.com.network.DataState<PaginationResponse>> =
        _paginationResponseDaily

    private val _paginationResponseMonthly: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<MonthlyAttendanceResponse>>> =
        MutableLiveData()
    val paginationResponseMonthly: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<MonthlyAttendanceResponse>>> =
        _paginationResponseMonthly

    private val _paginationResponseAttendance3: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<Attendance3Response>>> =
        MutableLiveData()
    val paginationResponseAttendance3: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<Attendance3Response>>> =
        _paginationResponseAttendance3

    private val _attendanceWithLocResponse: MutableLiveData<com.workseasy.com.network.DataState<NewResponse>> =
        MutableLiveData()
    val attendanceWithLocResponse: LiveData<com.workseasy.com.network.DataState<NewResponse>> =
        _attendanceWithLocResponse

    private val _attendancePunchOutResponse: MutableLiveData<com.workseasy.com.network.DataState<NewResponse>> =
        MutableLiveData()
    val attendancePunchOutResponse: LiveData<com.workseasy.com.network.DataState<NewResponse>> =
        _attendancePunchOutResponse

    private val _getAttendanceWithLocResponse: MutableLiveData<com.workseasy.com.network.DataState<GetAttendanceWithLocationResponse>> =
        MutableLiveData()
    val getAttendanceWithLocResponse: LiveData<com.workseasy.com.network.DataState<GetAttendanceWithLocationResponse>> =
        _getAttendanceWithLocResponse

    private val _postAutoAttendanceResponse: MutableLiveData<com.workseasy.com.network.DataState<AttendanceWithLocationResponse>> =
        MutableLiveData()
    val postAutoAttendanceResponse: LiveData<com.workseasy.com.network.DataState<AttendanceWithLocationResponse>> =
        _postAutoAttendanceResponse

    var repository: com.workseasy.com.repository.AttendanceRepository? = null
    private var networkHelper: NetworkHelper? = null

    init {
        repository = com.workseasy.com.repository.AttendanceRepository()
    }

    @SuppressLint("NewApi")
    fun add_daily_attendance(
        date: String?,
        punch_in: String?,
        punch_out: String?,
        employee_id: Int?,
        context: Context
    ) = viewModelScope.launch {
        _attendance1Response.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _attendance1Response.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.add_daily_attendance(date, punch_in, punch_out, employee_id)
                )
            } catch (exception: Exception) {
                _attendance1Response.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _attendance1Response.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun add_monthly_attendance(
        wd: String?,
        ot: String?,
        pd: String?,
        el: String?,
        cl: String?,
        year: String?,
        month: String?,
        sl: String?,
        hd: String?,
        wo: String?,
        employee_id: Int?,
        context: Context
    ) = viewModelScope.launch {
        _attendance2Response.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _attendance2Response.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.add_monthly_attendance(
                        wd,
                        ot,
                        pd,
                        el,
                        cl,
                        year,
                        month,
                        sl,
                        hd,
                        wo,
                        employee_id
                    )
                )
            } catch (exception: Exception) {
                _attendance2Response.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _attendance2Response.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }


    @SuppressLint("NewApi")
    fun add_monthly_attendance2(
        ot: String?,
        pd: String?,
        year: String?,
        month: String?,
        employee_id: Int?,
        context: Context
    ) = viewModelScope.launch {
        _attendance3Response.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _attendance3Response.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.add_monthly_attendance2(ot, pd, year, month, employee_id)
                )
            } catch (exception: Exception) {
                _attendance3Response.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _attendance3Response.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }


    @SuppressLint("NewApi")
    fun attendancePaginationDaily(
        page: String?,
        employee_id: Int?,
        type: String?,
        context: Context
    ) = viewModelScope.launch {
        _paginationResponseDaily.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _paginationResponseDaily.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.attendancePaginationDaily(page, employee_id, type)
                )
            } catch (exception: Exception) {
                _paginationResponseDaily.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _paginationResponseDaily.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun attendancePaginationMonthly(
        page: String?,
        employee_id: Int?,
        type: String?,
        context: Context
    ) = viewModelScope.launch {
        _paginationResponseMonthly.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _paginationResponseMonthly.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.attendancePaginationMonthly(page, employee_id, type)
                )
            } catch (exception: Exception) {
                _paginationResponseMonthly.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _paginationResponseMonthly.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun attendancePaginationAttendance3(
        page: String?,
        employee_id: Int?,
        type: String?,
        context: Context
    ) = viewModelScope.launch {
        _paginationResponseAttendance3.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _paginationResponseAttendance3.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.attendancePaginationAttendance3(page, employee_id, type)
                )
            } catch (exception: Exception) {
                _paginationResponseAttendance3.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _paginationResponseMonthly.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun attendancewithLocation(
        attendanceWithLocationDto: AttendanceWithLocationDto,
        context: Context
    ) = viewModelScope.launch {
        _attendanceWithLocResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _attendanceWithLocResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.attendancewithLocation(attendanceWithLocationDto)
                )
            } catch (exception: Exception) {
                _attendanceWithLocResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _attendanceWithLocResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun attendancewithLocationPunchOut(
        attendanceWithLocationDto: AttendanceWithLocationDto,
        context: Context
    ) = viewModelScope.launch {
        _attendancePunchOutResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _attendancePunchOutResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.attendancewithLocation(attendanceWithLocationDto)
                )
            } catch (exception: Exception) {
                _attendancePunchOutResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _attendancePunchOutResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun getattendancewithLocation(
        employee_id: Int,
        date: String,
        context: Context
    ) = viewModelScope.launch {
        _getAttendanceWithLocResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _getAttendanceWithLocResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getattendancewithLocation(employee_id, date)
                )
            } catch (exception: Exception) {
                _getAttendanceWithLocResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _getAttendanceWithLocResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun employeeAutoAttendance(
        autoAttendanceDto: AutoAttendanceDto,
        context: Context
    ) = viewModelScope.launch {
        _postAutoAttendanceResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _postAutoAttendanceResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.employeeAutoAttendance(autoAttendanceDto)
                )
            } catch (exception: Exception) {
                _postAutoAttendanceResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _postAutoAttendanceResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }


}