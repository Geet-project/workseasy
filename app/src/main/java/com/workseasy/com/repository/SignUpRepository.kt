package com.workseasy.com.repository

import com.hr.demoapp.network.RemoteDataSource
import com.workseasy.com.ui.hradmin.employeeRegistration.response.AddFamilyDto
import com.workseasy.com.ui.hradmin.employeeRegistration.response.WorkExpDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field

class SignUpRepository {
    private val client = RemoteDataSource.getInterface()
    suspend fun step1EmpReg(
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
        ifsc: RequestBody
    ) = client!!.employeeRegistrationStep1(
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
        designation,
        dept,
        resume,
        dateofjoining,
        bank_name,
        bank_ac_no,
        ifsc
    )

    suspend fun employeeEdit(
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
        pay_code: String,
        department: String,
        designation: String,
        religion: String,
        qualification: String,
        bank_name: String,
        bank_ac_no: String,
        ifsc: String,
    ) = client!!.employeeEdit(
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
        pay_code,
        department,
        designation,
        religion,
        qualification,
        bank_name,
        bank_ac_no,
        ifsc

    )

    suspend fun step2EmpReg(
        employee_id: String,
        present_address: String,
        permanent_address: String,
        permant_pincode: String,
        present_address_pincode: String,
        permant_state: String,
        permant_district: String,
        state_id: String,
        city_id: String,
    ) = client!!.employeeRegistrationStep2(
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

    suspend fun getStateList(token: String) = client!!.getState()

    suspend fun getDistrictList(stateid: Int) = client!!.getDistrict(stateid)

    suspend fun getCityList(stateid: Int) = client!!.getCity(stateid)
    suspend fun getSpinnerType(type: String) = client!!.getAssets(type)
    suspend fun step3EmpReg(
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
        category: String
        ) = client!!.employeeRegistrationStep3(
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

    suspend fun step4EmpReg(
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
        ot: String,
        ex_gratia: String,
        wage_calc: String,
        crader: String,
        otType: String,
        elNegative:String,
        clNegative: String,
        sl_negative: String,
        epf_limit: String,
        epf_emp_limit: String
        ) = client!!.employeeRegistrationStep4(
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
        ex_gratia,
        ot,
        wage_calc,
        crader,
        otType,
        elNegative,
        clNegative,
        sl_negative,
        epf_limit,
        epf_emp_limit

    )

    suspend fun step5EmpReg(
        employee_id: Int,
        basic_salary: Int,
        da: String
    ) = client!!.employeeRegistrationStep5(
        employee_id,
        basic_salary,
        da,
    )

    suspend fun step5List(
        id: Int
    ) = client!!.step5List(
        id
    )

    suspend fun step5delete(
        id: Int
    ) = client!!.deleteStep5(
        id
    )

    suspend fun getEmployeeData(emp_id: Int) = client!!.getEmployeeData(emp_id);

    suspend fun getMasterData() = client!!.getMasterData();

    suspend fun getBankFromIfsc(ifsc: String) = client!!.getBankFromIfsc(ifsc);

    suspend fun getStep3Data(employee_id: String) = client!!.getStep3Data(employee_id);

    suspend fun addWorkExp(workExpDto: WorkExpDto) = client!!.addWorkExp(workExpDto);
    suspend fun getWorkExp(employee_id: Int) = client!!.getWorkExp(employee_id);
    suspend fun addFamilyDetail(familyDto: AddFamilyDto) = client!!.addFamilyDetail(familyDto);
    suspend fun getFamilyDetail(employee_id: Int) = client!!.getFamilyDetail(employee_id);

    suspend fun getSalaryDetail(employee_id: Int) = client!!.getSalaryDetails(employee_id);


    suspend fun deleteFamilyId(familyId: String) = client!!.deleteFamilyId(familyId);

    suspend fun deleteWorkExpereince(workexpid: String) = client!!.deleteWorkExperience(workexpid);



}