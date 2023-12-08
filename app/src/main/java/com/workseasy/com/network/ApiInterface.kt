package com.hr.demoapp.network

import com.workseasy.com.network.GenericResponse
import com.workseasy.com.ui.OrganizationResponse
import com.workseasy.com.ui.accounts.PaymentResponse
import com.workseasy.com.ui.cms.response.CmsDto
import com.workseasy.com.ui.hradmin.employeeRegistration.response.AdharUniqueResponse
import com.workseasy.com.ui.hradmin.attendance.response.Attendance3Response
import com.workseasy.com.ui.hradmin.attendance.response.AttendanceWithLocationDto
import com.workseasy.com.ui.hradmin.attendance.response.AttendanceWithLocationResponse
import com.workseasy.com.ui.hradmin.attendance.response.AutoAttendanceDto
import com.workseasy.com.ui.hradmin.attendance.response.DailyAttendanceResponse
import com.workseasy.com.ui.hradmin.attendance.response.GetAttendanceWithLocationResponse
import com.workseasy.com.ui.hradmin.attendance.response.MonthlyAttendanceResponse
import com.workseasy.com.ui.hradmin.attendance.response.PaginationResponse
import com.workseasy.com.ui.hradmin.employeeDetails.response.*
import com.workseasy.com.ui.hradmin.employeeRegistration.response.*
import com.workseasy.com.ui.hradmin.employeelist.response.EmpListDto
import com.workseasy.com.ui.login.request.LoginRequest
import com.workseasy.com.ui.login.response.DashboardResponse
import com.workseasy.com.ui.login.response.LoginData
import com.workseasy.com.ui.notification.NotificationResponse
import com.workseasy.com.ui.purchase.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import com.workseasy.com.ui.purchase.response.QuotationResponse as QuotationResponse1

interface ApiInterface {
    @POST("api/login")
    suspend fun managerLogin(
        @Body loginRequest: LoginRequest
    ): com.workseasy.com.network.GenericResponse<LoginData>

    @GET("api/employee_list")
    suspend fun getEmployeeList (
        @Query("company_id") companyid: String,
        @Query("branch_id") branchid: String
    ): com.workseasy.com.network.GenericResponse<EmpListDto>

    @Multipart
    @POST("api/employee_registration/step-1")
    suspend fun employeeRegistrationStep1(
        @Part("company_id") company_id: RequestBody,
        @Part("name") name: RequestBody,
        @Part("father_name") father_name: RequestBody,
        @Part photo: MultipartBody.Part?,
        @Part("date_of_birth") date_of_birth: RequestBody,
        @Part("email") email: RequestBody,
        @Part("mob_no") mob_no: RequestBody,
        @Part("aadhar_number") aadhar_number: RequestBody,
        @Part("pan_number") pan_number: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part adharfrontPhoto: MultipartBody.Part?,
        @Part adharbackPhoto: MultipartBody.Part?,
        @Part panPhoto: MultipartBody.Part?,
        @Part("husband") husband: RequestBody,
        @Part("age") age: RequestBody,
        @Part("pay_code") paycode: RequestBody,
        @Part("designation") designation: RequestBody,
        @Part("department") department: RequestBody,
        @Part("resume") resume: RequestBody,
        @Part("date_of_joining") date_of_joining: RequestBody,
        @Part("bank_name") bank_name: RequestBody,
        @Part("bank_ac_no") bank_ac_no: RequestBody,
        @Part("ifsc") ifsc: RequestBody
    ): com.workseasy.com.network.GenericResponse<SignupDto>

    @FormUrlEncoded
    @POST("api/employee_registration/step-2")
    suspend fun employeeRegistrationStep2(
        @Field("employee_id") employee_id: String,
        @Field("present_address") present_address: String,
        @Field("permanent_address") permanent_address: String,
        @Field("permant_pincode") permant_pincode: String,
        @Field("present_address_pincode") present_address_pincode: String,
        @Field("permant_state") permant_state: String,
        @Field("permant_district") permant_district: String,
        @Field("state_id") state_id: String,
        @Field("city_id") city_id: String,
    ): com.workseasy.com.network.GenericResponse<SignupDto>

    @FormUrlEncoded
    @POST("api/employee_registration/step-3")
    suspend fun employeeRegistrationStep3(
        @Field("employee_id") employee_id: Int,
        @Field("grade") grade: String,
        @Field("uan_no") uan_no: String,
        @Field("esic_no") esic_no: String,
        @Field("pf_no") pf_no: String,
        @Field("shift_name") shift_name: String,
        @Field("payment_mode") payment_mode: String,
        @Field("reason_of_leaving") reason_of_leaving: String,
        @Field("date_of_leaving") date_of_leaving: String,
        @Field("latearrivaltime") latearrivaltime: String,
        @Field("earlydeparturetime") earlydeparturetime: String,
        @Field("presentmarkingtime") presentmarkingtime: String,
        @Field("halfhourtime") halfhourtime: String,
        @Field("first_weekly_off") first_weekly_off: String,
        @Field("second_weekly_off") second_weekly_off: String,
        @Field("crader") category: String,
    ): com.workseasy.com.network.GenericResponse<SignupDto>

    @FormUrlEncoded
    @POST("api/employee_registration/step-4")
    suspend fun employeeRegistrationStep4(
        @Field("employee_id") employee_id: Int,
        @Field("epf") epf: String,
        @Field("vpf") vpf: String,
        @Field("esic") esic: String,
        @Field("lwf") lwf: String,
        @Field("tds") tds: String,
        @Field("pt") pt: String,
        @Field("el") el: String,
        @Field("cl") cl: String,
        @Field("sl") sl: String,
        @Field("bonus") bonus: String,
        @Field("ex_gratia") ex_gratia: String,
        @Field("ot") ot: String,
        @Field("wage_calculation") wage_calc: String,
        @Field("crader") crader: String,
        @Field("ot_type") ot_type: String,
        @Field("el_negative") el_negative: String,
        @Field("cl_negative") cl_negative: String,
        @Field("sl_negative") sl_negative: String,
        @Field("epf_limit") epf_limit: String,
        @Field("epf_emp_limit") epf_emp_limit: String,
    ): com.workseasy.com.network.GenericResponse<SignupDto>

    @FormUrlEncoded
    @POST("api/employee_registration/step-5")
    suspend fun employeeRegistrationStep5(
        @Field("employee_id") employee_id: Int,
        @Field("earning_type") basic_salary: Int,
        @Field("value") da: String,
    ): com.workseasy.com.network.GenericResponse<SignupDto>

    @GET("api/state")
    suspend fun getState(): com.workseasy.com.network.GenericResponse<StateDto>

    @GET("api/get-district")
    suspend fun getDistrict(@Query("state_id") stateId: Int) : DistrictResponse

    @GET("api/city")
    suspend fun getCity(@Query("state_id") stateid: Int): com.workseasy.com.network.GenericResponse<CityDto>

    @GET("api/asset")
    suspend fun getAssets(@Query("type") type: String): com.workseasy.com.network.GenericResponse<AssetDto>

    @FormUrlEncoded
    @POST("api/employee_leave")
    suspend fun leaveRequestSubmit(
        @Field("from_date") from_date: String?,
        @Field("to_date") to_date: String?,
        @Field("reason") reason: String?,
        @Field("employee_id") employee_id: Int?
    ): com.workseasy.com.network.GenericResponse<LeaveRequestSubmitDto>

    @GET("api/employee_leave")
    suspend fun getLeaveRequest(
        @Query("employee_id") employee_id: Int?
    ): com.workseasy.com.network.GenericResponse<LeaveRequestListDto>

    @GET("api/advance_salary")
    suspend fun getAdvRequest(
        @Query("employee_id") employee_id: Int?
    ): com.workseasy.com.network.GenericResponse<AdvSalaryListDto>


    @FormUrlEncoded
    @POST("api/advance_salary")
    suspend fun advRequestSubmit(
        @Field("employee_id") employee_id: Int?,
        @Field("amount") from_date: String?,
        @Field("reason") reason: String?,
        @Field("maximum_deduction_per_month") to_date: String?,
    ): com.workseasy.com.network.GenericResponse<AdvSalaryListDto>

    @GET("api/employee_leave_request")
    suspend fun getApprovalLeaveRequest(@Query("employee_id") employee_id: Int?): com.workseasy.com.network.GenericResponse<ApprovalRequestDto>


    @FormUrlEncoded
    @POST("api/employee_leave_request")
    suspend fun updateleaveRequest(
        @Field("leave_id") leave_id: Int?,
        @Field("status") status: String?,
        @Field("remark") remark: String?
    ): com.workseasy.com.network.GenericResponse<LeaveRequestSubmitDto>

    @GET("api/view_salary")
    suspend fun getviewSalaryList(@Query("employee_id") employee_id: Int?): com.workseasy.com.network.GenericResponse<ViewSalaryDto>


    @GET("api/advance_salary_request")
    suspend fun getApprovalSalaryRequest(@Query("employee_id") employee_id: Int?): com.workseasy.com.network.GenericResponse<ApprovalSalaryDto>

    @FormUrlEncoded
    @POST("api/advance_salary_request")
    suspend fun updatesalaryRequest(
        @Field("advance_salary_id") leave_id: Int?,
        @Field("status") status: String?,
        @Field("remark") remark: String?
    ): com.workseasy.com.network.GenericResponse<AdvanceSalary>

    @FormUrlEncoded
    @POST("api/change-password")
    suspend fun changePassword(
        @Field("old_password") oldpassword: String?,
        @Field("password") password: String,
        @Field("confirm_password") confirm_password: String?
    ): com.workseasy.com.network.NewResponse

    @FormUrlEncoded
    @POST("api/change-mpin")
    suspend fun changeMPin(
        @Field("old_mpin") oldpassword: String?,
        @Field("mpin") password: String,
        @Field("confirm_mpin") confirm_password: String?
    ): com.workseasy.com.network.NewResponse

    @GET("api/page")
    suspend fun cmsApi(@Query("page") page: String?): com.workseasy.com.network.GenericResponse<CmsDto>

    @FormUrlEncoded
    @POST("api/add_daily_attendance")
    suspend fun add_daily_attendance(
        @Field("date") date: String?,
        @Field("punch_in") punch_in: String?,
        @Field("punch_out") punch_out: String?,
        @Field("employee_id") employee_id: Int?
    ): com.workseasy.com.network.NewResponse


    @FormUrlEncoded
    @POST("api/add_monthly_attendance")
    suspend fun add_monthly_attendance(
        @Field("wd") wd: String?,
        @Field("ot") ot: String?,
        @Field("pd") pd: String?,
        @Field("el") el: String?,
        @Field("cl") cl: String?,
        @Field("year") year: String?,
        @Field("month") month: String?,
        @Field("sl") sl: String?,
        @Field("hd") hd: String?,
        @Field("wo") wo: String?,
        @Field("employee_id") employee_id: Int?,
    ): com.workseasy.com.network.NewResponse

    @FormUrlEncoded
    @POST("api/add_monthly_attendance2")
    suspend fun add_monthly_attendance2(
        @Field("ot") ot: String?,
        @Field("wd") pd: String?,
        @Field("year") year: String?,
        @Field("month") sl: String?,
        @Field("employee_id") employee_id: Int?,
    ): com.workseasy.com.network.NewResponse


    @GET("api/pagination")
    suspend fun attendancePaginationDaily(
        @Query("page") page: String?,
        @Query("employee_id") employee_id: Int?,
        @Query("type") type: String?
    ): PaginationResponse

    @GET("api/pagination")
    suspend fun attendancePaginationMonthly(
        @Query("page") page: String?,
        @Query("employee_id") employee_id: Int?,
        @Query("type") type: String?
    ): com.workseasy.com.network.GenericResponse<MonthlyAttendanceResponse>

    @GET("api/pagination")
    suspend fun attendancePaginationAttendance3(
        @Query("page") page: String?,
        @Query("employee_id") employee_id: Int?,
        @Query("type") type: String?
    ): com.workseasy.com.network.GenericResponse<Attendance3Response>

    @GET("api/pagination2")
    suspend fun leavePagination(
        @Query("page") page: String?,
        @Query("employee_id") employee_id: Int?,
        @Query("type") type: String?
    ): com.workseasy.com.network.GenericResponse<PaginationListDto>

    @Multipart
    @POST("api/pr/store")
    suspend fun raisePr (
        @Part("item") item: RequestBody?,
        @Part("make") make: RequestBody?,
        @Part("grade") gender: RequestBody?,
        @Part("delivery") delivery: RequestBody?,
        @Part("quantity") quantity: RequestBody?,
        @Part("unit") unit: RequestBody?,
        @Part("remark") remark: RequestBody?,
        @Part passbook: MultipartBody.Part?,
    ): com.workseasy.com.network.GenericResponse<PrSubmitResponse>

    @GET("api/assets/item_list")
    suspend fun getItemsList(): GetItemListResponse

    @GET("api/pr/assets")
    suspend fun getPrAssetsList(@Query("itemId") itemId:Int): GetItemAssetsResponse

    @GET("api/pr/list")
    suspend fun getPrList(): PRListResponseX

    @GET("api/pr/list")
    suspend fun getApprovedPrList(@Query("status") status: String?): PRListResponseX

    @GET("api/pr/view")
    suspend fun viewPr(@Query("pr_id") pr_id: Int?): com.workseasy.com.network.GenericResponse<Pr>

    @FormUrlEncoded
    @POST("api/pr/status")
    suspend fun updatePrStatus(
        @Field("pr_id") pr_id: Int?,
        @Field("status") status: String?,
        @Field("remark") remark: String?
    ): com.workseasy.com.network.GenericResponse<Pr>

    @Multipart
    @POST("api/grn/store")
    suspend fun grnStore(
        @Part("po") po: RequestBody,
        @Part("item") item: RequestBody,
        @Part("remark") remark: RequestBody,
        @Part("bill_no") bill_no: RequestBody,
        @Part("receipt_date") receipt_date: RequestBody,
        @Part("quntity") quntity: RequestBody,
        @Part photo: MultipartBody.Part?,
    ): com.workseasy.com.network.GenericResponse<GrnResponse>

    @FormUrlEncoded
    @POST("api/consumption/store")
    suspend fun consumptionStore(
        @Field("item") item: String?,
        @Field("quantity") quantity: String?,
        @Field("date") status: String?,
        @Field("remark") remark: String?
    ): com.workseasy.com.network.GenericResponse<Pr>

    @GET("api/quotation/getQuotationForApproval")
    suspend fun getQuotationList(): QuotationApprovedResponse

    @GET("api/quotation/view")
    suspend fun getPrListForQuotation(@Query("pr_id") pr_id: Int?): QuotationPrResponse

    @FormUrlEncoded
    @POST("api/quotation/status")
    suspend fun quotationUpdate(
        @Field("quotation_id") quotation_id: Int?, @Field("status") status: String
    ): com.workseasy.com.network.NewResponse

    @FormUrlEncoded
    @POST("api/payment")
    suspend fun paymentPost(
        @Field("beneficiary_name") beneficiary_name: String,
        @Field("employee_code") employee_code: String,
        @Field("payment_head") payment_head: String,
        @Field("payment_status") payment_status: String,
        @Field("payment_date") payment_date: String,
        @Field("payment_mode") payment_mode: String,
        @Field("transaction_ref") transaction_ref: String,
        @Field("remark") remark: String
    ): com.workseasy.com.network.GenericResponse<PaymentResponse>

    @GET("api/notification")
    suspend fun getNotification(): com.workseasy.com.network.GenericResponse<NotificationResponse>

    @GET("api/employee_search")
    suspend fun empSearch(@Query("search") search: String): com.workseasy.com.network.GenericResponse<EmpListDto>

    @GET("api/employee_registration/step-5/list")
    suspend fun step5List(@Query("employee_id") id: Int): com.workseasy.com.network.GenericResponseList<Step5Response>

    @FormUrlEncoded
    @POST("api/employee_registration/step-5/delete")
    suspend fun deleteStep5(@Field("employee_salary_id") id: Int): com.workseasy.com.network.GenericResponseList<Step5Response>


    @GET("api/organization")
    suspend fun getOrganizationName(): OrganizationResponse

    @GET("api/employee")
    suspend fun getEmployeeData(@Query("employee_id") emp_id: Int): com.workseasy.com.network.GenericResponse<EmpDetailResponse>

    @FormUrlEncoded
    @POST("api/employee_edit")
    suspend fun employeeEdit(
        @Field("employee_id") employee_id: Int,
        @Field("name") name: String,
        @Field("father_name") father_name: String,
        @Field("date_of_birth") date_of_birth: String,
        @Field("email") email: String,
        @Field("mob_no") mob_no: String,
        @Field("aadhar_number") aadhar_number: String,
        @Field("pan_number") pan_number: String,
        @Field("gender") gender: String,
        @Field("age") age: String,
        @Field("pay_code") paycode: String,
        @Field("designation") designation: String,
        @Field("department") department: String,
        @Field("religion") religion: String,
        @Field("qualification") qualification: String,
        @Field("bank_name") bank_name: String,
        @Field("bank_ac_no") bank_ac_no: String,
        @Field("ifsc") ifsc: String
    ): com.workseasy.com.network.GenericResponse<SignupDto>

    @FormUrlEncoded
    @POST("api/organization/update")
    suspend fun updateOrganization(@Field("company_id") company_id: String): GenericResponse<SignupDto>

    @FormUrlEncoded
    @POST("api/check/employee")
    suspend fun checkEmployee(@Field("aadhar_number") aadhar_number: String): GenericResponse<SignupDto>

    @POST("api/aadhar-unique")
    suspend fun checkAdhar(
        @Body adharUniqueDto: AdharUniqueDto
    ): AdharUniqueResponse

    @GET("api/master_data/step-1")
    suspend fun getMasterData(): MasterDataResponse

    @GET("api/user/permission/{userid}")
    suspend fun getPermissions(@Path("userid") id: String): DashboardResponse

    @GET("api/employee_step_1")
    suspend fun getAdharDetail(@Query("aadhar_number") adharnumber: String): AdharDataResponse

    @GET
    suspend fun getBankFromIfsc(@Url url: String): BankNameResponse

    @GET("api/employee-data/step3")
    suspend fun getStep3Data(@Query("employee_id") employee_id: String): Step3ResponseData

    @GET("api/get-employee-work-experience")
    suspend fun getWorkExp(@Query("employee_id") employee_id: Int): WorkExpResponse

    @POST("api/add-employee-work-experience")
    suspend fun addWorkExp(
        @Body workExpDto: WorkExpDto
    ): com.workseasy.com.network.NewResponse

    @POST("api/employee-family-details")
    suspend fun addFamilyDetail(
        @Body addFamilyDto: AddFamilyDto
    ): com.workseasy.com.network.NewResponse

    @GET("api/employee-family-details")
    suspend fun getFamilyDetail(@Query("employee_id") employee_id: Int): FamilyDetailResponse

    @GET("api/salary_details")
    suspend fun getSalaryDetails(@Query("employee_id") employee_id: Int): SalaryDetailResponse

    @DELETE("api/delete-family-details")
    suspend fun deleteFamilyId(@Query("id") familyId: String): DeleteResponse

    @DELETE("api/delete-work-experience")
    suspend fun deleteWorkExperience(@Query("id") familyId: String): DeleteResponse

    @POST("api/attendance-with-location")
    suspend fun attendancewithLocation(
        @Body attendanceWithLocationDto: AttendanceWithLocationDto
    ): com.workseasy.com.network.NewResponse

    @GET("api/get-attendance-with-location")
    suspend fun getattendancewithLocation (
        @Query("employee_id") employee_id: Int,
        @Query("date") date: String
    ): GetAttendanceWithLocationResponse

    @POST("api/employee-auto-attendance")
    suspend fun employeeAutoAttendance (
        @Body autoAttendanceDto: AutoAttendanceDto
    ): AttendanceWithLocationResponse


    @POST("api/quotation/store")
    suspend fun createQuotation(
        @Body createQuotationDto: CreateQuotationDto
    ): CreateQuotationResponse

    @GET("api/assets/vendors_list")
    suspend fun getVendorsList(): VendorListResponse

    @GET("api/po/getApprovedQuotationList")
    suspend fun getQuotationListPo(@Query("vendorId") vendorId : Int?): QuotationPoListResponse

    @POST("api/po/createPO")
    suspend fun createPo (
        @Body createPoDto: CreatePoDto
    ): CreateQuotationResponse


}