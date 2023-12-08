package com.workseasy.com.ui.hradmin.employeeRegistration.viewmodel

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.workseasy.com.network.NewResponse
import com.workseasy.com.ui.hradmin.employeeDetails.response.EmpDetailResponse
import com.workseasy.com.ui.hradmin.employeeDetails.response.Step3ResponseData
import com.workseasy.com.ui.hradmin.employeeRegistration.response.*
import com.workseasy.com.utils.NetworkHelper
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class SignupViewModel : com.workseasy.com.base.BaseViewModel() {
    private val _signupResponse: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<SignupDto>>> =
        MutableLiveData()
    val signupResponse: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<SignupDto>>> =
        _signupResponse

    private val _editResponse: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<SignupDto>>> =
        MutableLiveData()
    val editResponse: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<SignupDto>>> =
        _editResponse

    private val _stateResponse: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<StateDto>>> =
        MutableLiveData()
    val stateResponse: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<StateDto>>> =
        _stateResponse


    private val _districtResponse: MutableLiveData<com.workseasy.com.network.DataState<DistrictResponse>> =
        MutableLiveData()
    val districtResponse: LiveData<com.workseasy.com.network.DataState<DistrictResponse>> =
        _districtResponse

    private val _cityResponse: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<CityDto>>> =
        MutableLiveData()
    val cityResponse: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<CityDto>>> =
        _cityResponse

    private val _spinnerResponse: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<AssetDto>>> =
        MutableLiveData()
    val spinnerResponse: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<AssetDto>>> =
        _spinnerResponse

    private val _step5ListResponse: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponseList<Step5Response>>> =
        MutableLiveData()
    val step5ListResponse: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponseList<Step5Response>>> =
        _step5ListResponse

    private val _step5delete: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponseList<Step5Response>>> =
        MutableLiveData()
    val step5delete: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponseList<Step5Response>>> =
        _step5delete

    private val _empData: MutableLiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<EmpDetailResponse>>> =
        MutableLiveData()
    val empData: LiveData<com.workseasy.com.network.DataState<com.workseasy.com.network.GenericResponse<EmpDetailResponse>>> =
        _empData

    private val _masterDataResponse: MutableLiveData<com.workseasy.com.network.DataState<MasterDataResponse>> =
        MutableLiveData()
    val masterDataResponse: LiveData<com.workseasy.com.network.DataState<MasterDataResponse>> =
        _masterDataResponse

    private val _bankNameResponse: MutableLiveData<com.workseasy.com.network.DataState<BankNameResponse>> =
        MutableLiveData()
    val bankNameResponse: LiveData<com.workseasy.com.network.DataState<BankNameResponse>> =
        _bankNameResponse


    private val _step3Response: MutableLiveData<com.workseasy.com.network.DataState<Step3ResponseData>> =
        MutableLiveData()
    val step3Response: LiveData<com.workseasy.com.network.DataState<Step3ResponseData>> =
        _step3Response

    private val _workExpResponse: MutableLiveData<com.workseasy.com.network.DataState<NewResponse>> =
        MutableLiveData()
    val workExpResponse: LiveData<com.workseasy.com.network.DataState<NewResponse>> =
        _workExpResponse

    private val _getworkExpResponse: MutableLiveData<com.workseasy.com.network.DataState<WorkExpResponse>> =
        MutableLiveData()
    val getworkExpResponse: LiveData<com.workseasy.com.network.DataState<WorkExpResponse>> =
        _getworkExpResponse

    private val _familyDetalResponse: MutableLiveData<com.workseasy.com.network.DataState<NewResponse>> =
        MutableLiveData()
    val familyDetailResponse: LiveData<com.workseasy.com.network.DataState<NewResponse>> =
        _familyDetalResponse

    private val _getFamilyDetailResponse: MutableLiveData<com.workseasy.com.network.DataState<FamilyDetailResponse>> =
        MutableLiveData()
    val getFamilyDetailResponse: LiveData<com.workseasy.com.network.DataState<FamilyDetailResponse>> =
        _getFamilyDetailResponse


    private val _getSalaryDetailResponse: MutableLiveData<com.workseasy.com.network.DataState<SalaryDetailResponse>> =
        MutableLiveData()
    val getSalaryDetailResponse: LiveData<com.workseasy.com.network.DataState<SalaryDetailResponse>> =
        _getSalaryDetailResponse

    private val _deleteFamilyResponse: MutableLiveData<com.workseasy.com.network.DataState<DeleteResponse>> =
        MutableLiveData()
    val deleteFamilyDetailResponse: LiveData<com.workseasy.com.network.DataState<DeleteResponse>> =
        _deleteFamilyResponse


    private val _deleteWorkExpResponse: MutableLiveData<com.workseasy.com.network.DataState<DeleteResponse>> =
        MutableLiveData()
    val deleteWorkExpResponse: LiveData<com.workseasy.com.network.DataState<DeleteResponse>> =
        _deleteWorkExpResponse



    var repository: com.workseasy.com.repository.SignUpRepository? = null
    private var networkHelper: NetworkHelper? = null

    init {
        repository = com.workseasy.com.repository.SignUpRepository()
    }





    @SuppressLint("NewApi")
    fun step1Register(
        company_id: RequestBody,
        name: RequestBody,
        father_name: RequestBody,
        photo: MultipartBody.Part?,
        date_of_birth: RequestBody,
        email: RequestBody,
        mob_no: RequestBody,
        aadhar_number: RequestBody,
        pan_number: RequestBody,
        gender: RequestBody,
        aadhar_front_photo: MultipartBody.Part?,
        aadhar_back_photo: MultipartBody.Part?,
        pan_front_photo: MultipartBody.Part?,
        husband: RequestBody,
        age: RequestBody,
        paycode: RequestBody,
        dept: RequestBody,
        designation: RequestBody,
        resume: RequestBody,
        dateofjoining: RequestBody,
        bank_name: RequestBody,
        bank_ac_no: RequestBody,
        ifsc: RequestBody,
        context: Context
    ) = viewModelScope.launch {
        _signupResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _signupResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.step1EmpReg(
                        company_id,
                        name,
                        father_name,
                        photo,
                        date_of_birth,
                        email,
                        mob_no,
                        aadhar_number,
                        pan_number,
                        gender,
                        aadhar_front_photo,
                        aadhar_back_photo,
                        pan_front_photo,
                        husband,
                        age,
                        paycode,
                        dept,
                        designation,
                        resume,
                        dateofjoining,
                        bank_name,
                        bank_ac_no,
                        ifsc
                    )
                )
            } catch (exception: Exception) {
                _signupResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _signupResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }


    @SuppressLint("NewApi")
    fun step2Register(
        employee_id: String,
        present_address: String,
        permanent_address: String,
        permant_pincode: String,
        present_address_pincode: String,
        permant_state: String,
        permant_district: String,
        state_id: String,
        city_id: String,
        context: Context
    ) = viewModelScope.launch {
        _signupResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _signupResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.step2EmpReg(
                        employee_id,
                        present_address,
                        permanent_address,
                        permant_pincode,
                        present_address_pincode,
                        permant_state,
                        permant_district,
                        state_id,
                        city_id,
                    )
                )
            } catch (exception: Exception) {
                _signupResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _signupResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun step3Register(
        employee_id: Int,
        grade: String,
        uan_no: String,
        esic_no: String,
        pf_no: String,
        shift_name: String,
        payment_mode: String,
        reason_of_leaving
        : String,
        date_of_leaving: String,
        latearrivaltime: String,
        earlydeparturetime: String,
        presentmarkingtime: String,
        halfhourtime: String,
        first_weekly_off: String,
        second_weekly_off: String,
        category: String,context: Context
    ) = viewModelScope.launch {
        _signupResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _signupResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.step3EmpReg(
                        employee_id,
                        grade,
                        uan_no,
                        esic_no,
                        pf_no,
                        shift_name,
                        payment_mode,
                        reason_of_leaving,
                        date_of_leaving,
                        latearrivaltime,
                        earlydeparturetime,
                        presentmarkingtime,
                        halfhourtime,
                        first_weekly_off,
                        second_weekly_off,
                        category
                    )
                )
            } catch (exception: Exception) {
                _signupResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _signupResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun step4Register(
        employee_id: Int,
        epf: String,
        vpf: String,
        esic: String,
        lwf: String,
        tds: String,
        pt: String,
        el: String,
        cl: String,
        sl: String,
        bonus: String,
        ex_gratia: String,
        ot: String,
        wage_calc: String,
        crader: String,
        otType: String,
        elNegative:String,
        clNegative: String,
        sl_negative: String,
        epf_limit: String,
        epf_emp_limit: String,
        context: Context
    ) = viewModelScope.launch {
        _signupResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _signupResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.step4EmpReg(
                        employee_id,
                        epf,
                        vpf,
                        esic,
                        lwf,
                        tds,
                        pt,
                        el,
                        cl,
                        sl,
                        bonus,
                        ot,
                        ex_gratia,
                        wage_calc,
                        crader,
                        otType,
                        elNegative,
                        clNegative,
                        sl_negative,
                        epf_limit,
                        epf_emp_limit
                    )
                )
            } catch (exception: Exception) {
                _signupResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _signupResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }

    }

    @SuppressLint("NewApi")
    fun step5Register(
        employee_id: Int,
        basic_salary: Int,
        da: String, context: Context
    ) = viewModelScope.launch {
        _signupResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _signupResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.step5EmpReg(
                        employee_id,
                        basic_salary,
                        da
                    )
                )
            } catch (exception: Exception) {
                _signupResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _signupResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }


    @SuppressLint("NewApi")
    fun getStateList(
        token: String, context: Context
    ) = viewModelScope.launch {
        _stateResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _stateResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getStateList(token)
                )
            } catch (exception: Exception) {
                _stateResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _stateResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun getDistrictList(
        stateid: Int, context: Context
    ) = viewModelScope.launch {
        _districtResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _districtResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getDistrictList(stateid)
                )
            } catch (exception: Exception) {
                _districtResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _districtResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }


    @SuppressLint("NewApi")
    fun getCityList(
        stateid: Int, context: Context
    ) = viewModelScope.launch {
        _cityResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _cityResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getCityList(stateid)
                )
            } catch (exception: Exception) {
                _cityResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _cityResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }


    @SuppressLint("NewApi")
    fun getAssets(
        type: String, context: Context
    ) = viewModelScope.launch {
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
            _cityResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun step5List(
        id: Int, context: Context
    ) = viewModelScope.launch {
        _step5ListResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _step5ListResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.step5List(id)
                )
            } catch (exception: Exception) {
                _step5ListResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _step5ListResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun step5delete(
        id: Int, context: Context
    ) = viewModelScope.launch {
        _step5delete.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _step5delete.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.step5delete(id)
                )
            } catch (exception: Exception) {
                _step5delete.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _step5delete.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun empData(
        id: Int, context: Context
    ) = viewModelScope.launch {
        _empData.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _empData.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getEmployeeData(id)
                )
            } catch (exception: Exception) {
                _empData.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _empData.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun step1Edit(
        employee_id: Int,
        name: String,
        father_name: String,
        date_of_birth: String,
        email: String,
        mob_no: String,
        aadhar_number: String,
        pan_number: String,
        gender: String,
        age: String,
        paycode: String,
        dept: String,
        designation: String,
        religion: String,
        qualification: String,
        bank_name: String,
        bank_ac_no: String,
        ifsc: String,
        context: Context
    ) = viewModelScope.launch {
        _editResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _editResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.employeeEdit(
                        employee_id,
                        name,
                        father_name,
                        date_of_birth,
                        email,
                        mob_no,
                        aadhar_number,
                        pan_number,
                        gender,
                        age,
                        paycode,
                        dept,
                        designation,
                        religion,
                        qualification,
                        bank_name,
                        bank_ac_no,
                        ifsc
                    )
                )
            } catch (exception: Exception) {
                _editResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _editResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun getMasterData(
        context: Context
    ) = viewModelScope.launch {
        _masterDataResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _masterDataResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getMasterData()
                )
            } catch (exception: Exception) {
                _masterDataResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _masterDataResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun getBankFromIfsc(
        ifsc: String, context: Context,
    ) = viewModelScope.launch {
        _bankNameResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _bankNameResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getBankFromIfsc(ifsc)
                )
            } catch (exception: Exception) {
                _bankNameResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _bankNameResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun getStep3Data(
        employee_id: String, context: Context,
    ) = viewModelScope.launch {
        _workExpResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _step3Response.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getStep3Data(employee_id)
                )
            } catch (exception: Exception) {
                _step3Response.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _step3Response.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }


    @SuppressLint("NewApi")
    fun addWorkExp(
        workExpDto: WorkExpDto, context: Context,
    ) = viewModelScope.launch {
        _workExpResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _workExpResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.addWorkExp(workExpDto)
                )
            } catch (exception: Exception) {
                _workExpResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _workExpResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun getWorkExp(
        employee_id: Int, context: Context,
    ) = viewModelScope.launch {
        _getworkExpResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _getworkExpResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getWorkExp(employee_id)
                )
            } catch (exception: Exception) {
                _getworkExpResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _getworkExpResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun addFamilyDetail(
        familyDto: AddFamilyDto, context: Context,
    ) = viewModelScope.launch {
        _familyDetalResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _familyDetalResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.addFamilyDetail(familyDto)
                )
            } catch (exception: Exception) {
                _familyDetalResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _familyDetalResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }


    @SuppressLint("NewApi")
    fun getFamilyDetail(
        employee_id: Int, context: Context,
    ) = viewModelScope.launch {
        _getFamilyDetailResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _getFamilyDetailResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getFamilyDetail(employee_id)
                )
            } catch (exception: Exception) {
                _getFamilyDetailResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _getFamilyDetailResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }


    @SuppressLint("NewApi")
    fun getSalaryDetail(
        employee_id: Int, context: Context,
    ) = viewModelScope.launch {
        _getSalaryDetailResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _getSalaryDetailResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.getSalaryDetail(employee_id)
                )
            } catch (exception: Exception) {
                _getSalaryDetailResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _getSalaryDetailResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun deleteFamilyId(
        familyId: Int?, context: Context,
    ) = viewModelScope.launch {
        _deleteFamilyResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _deleteFamilyResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.deleteFamilyId(familyId.toString())
                )
            } catch (exception: Exception) {
                _deleteFamilyResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _deleteFamilyResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }

    @SuppressLint("NewApi")
    fun deleteWorkExp(
        workExpId: Int?, context: Context,
    ) = viewModelScope.launch {
        _deleteWorkExpResponse.postValue(com.workseasy.com.network.DataState.Loading)
        networkHelper = NetworkHelper(context)
        if (networkHelper!!.isNetworkConnected()) {
            try {
                _deleteWorkExpResponse.value = com.workseasy.com.network.DataState.Success(
                    data = repository!!.deleteWorkExpereince(workExpId.toString())
                )
            } catch (exception: Exception) {
                _deleteWorkExpResponse.value = com.workseasy.com.network.DataState.Error(
                    exception = exception
                )
            }
        } else {
            _deleteWorkExpResponse.postValue(
                com.workseasy.com.network.DataState.Error(
                    exception = NetworkErrorException("Connection Error")
                )
            )
        }
    }



}