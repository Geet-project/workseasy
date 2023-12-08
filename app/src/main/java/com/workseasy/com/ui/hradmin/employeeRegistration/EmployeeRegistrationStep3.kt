package com.workseasy.com.ui.hradmin.employeeRegistration

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityEmployeeRegistrationStep3Binding
import com.workseasy.com.ui.hradmin.employeeRegistration.response.Asset
import com.workseasy.com.ui.hradmin.employeeRegistration.viewmodel.SignupViewModel
import java.util.*


class EmployeeRegistrationStep3 : AppCompatActivity() {
    lateinit var binding: ActivityEmployeeRegistrationStep3Binding
    val viewModel: SignupViewModel by viewModels()
    var assetDto: List<Asset>?=null
    var assetArray = mutableListOf<String>()
    var assetId = mutableListOf<Int>()
    lateinit var progressDialog: ProgressDialog
    var employee_id: Int= 0
    var selectedWage: String ?=null
    var selectedPaymentMode : String?=null
    var joiningDate: String ?=""
    var leavingDate: String ?=""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee_registration_step3)
        employee_id= intent.getIntExtra("employee_id", 0)
        progressDialog = ProgressDialog(this)
        callApiForSpinnerAsset("Bank")
        callApiForSpinnerAsset("Wage Calculation")
        callApiForSpinnerAsset("Payment Mode")
        responseHandle()
        setListners()
    }

    fun setListners()
    {
        binding.savebtn.setOnClickListener {
            var paycode         = binding.PayCodeET.text.toString()
            var pcanumber       = binding.PCAEt.text.toString()
            var uannumber       = binding.UanEt.text.toString()
            var pfno            = binding.PFEt.text.toString()
            var esic            = binding.ESICET.text.toString()
            var reasonofleaving = binding.ReasonLeavingEt.text.toString()
            val joiningDateReq  = joiningDate!!
            val leavingDateReq  = leavingDate!!
            val wageReq         = selectedWage.toString()
            val paymentReq      = selectedPaymentMode.toString()
//            callApiForStep3(employee_id, paycode, uannumber, esic, pfno, joiningDateReq, paymentReq, wageReq,
//            reasonofleaving, pcanumber,leavingDateReq)
        }

        binding.DobJoinEt.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // this never ends while debugging
                    // Display Selected date in textbox
                    joiningDate = dayOfMonth.toString() + "-" + (monthOfYear+1).toString() + "-" + year.toString()
                    binding.DobJoinEt.setText(joiningDate)
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        binding.DobLeaveEt.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // this never ends while debugging
                    // Display Selected date in textbox
                    leavingDate = dayOfMonth.toString() + "-" + (monthOfYear+1).toString() + "-" + year.toString()

                    binding.DobLeaveEt.setText(leavingDate)
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    fun setAssetSpinner(assetList: List<Asset>, type: String)
    {
        assetArray.clear()
        assetId.clear()
        for (i in 0 until assetList!!.size) {
            assetArray?.add(assetList[i].name)
            assetId?.add(assetList[i].id)
        }

        if(type.equals("Wage Calculation"))
        {
            binding.wageSpinner.setItems(assetArray!!.toList() )
        }else if(type.equals("Payment Mode"))
        {
            binding.PaymentModeSpinner.setItems(assetArray!!.toList())
        }

    }

    fun callApiForSpinnerAsset(type: String)
    {
        viewModel.getAssets(type, this)
    }

//    fun callApiForStep3(employee_id: Int,
//                        pay_code: String,
//                        uan_no: String,
//                        esic_no: String,
//                        pf_no: String,
//                        date_of_joining: String,
//                        payment_mode: String,
//                        wage_calculation
//                        : String,
//                        reason_of_leaving: String,
//                        pca_no: String,
//                        date_of_leaving : String)
//    {
//        viewModel.step3Register(
//            employee_id,
//            pay_code,
//            uan_no,
//            esic_no,
//            pf_no,
//            date_of_joining,
//            payment_mode,
//            wage_calculation,
//            reason_of_leaving,
//            pca_no,
//            date_of_leaving, this)
//    }

    fun responseHandle()
    {
        viewModel.spinnerResponse.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
                    if(dataSate.data.status==200)
                    {
                        progressDialog.dismiss()
                        assetDto = dataSate.data.data.assets
                        setAssetSpinner(assetDto!!, dataSate.data.data.type)
                    }else{
                        Toast.makeText(this, dataSate.data.error, Toast.LENGTH_SHORT).show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    progressDialog?.show()
                    progressDialog?.setMessage("Please Wait..")
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressDialog?.dismiss()
                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.signupResponse.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
                    if(dataSate.data.status==200)
                    {
                        progressDialog?.dismiss()
                        Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, EmployeeRegistrationStep4::class.java)
                        intent.putExtra("employee_id", dataSate.data.data.id)
                        startActivity(intent)
                        finish()
                    }else{
                        progressDialog?.dismiss()
                        Toast.makeText(this, dataSate.data.error, Toast.LENGTH_SHORT).show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    progressDialog?.show()
                    progressDialog?.setMessage("Please Wait..")
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressDialog?.dismiss()
                    Toast.makeText(this, ""+dataSate.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }



    override fun onBackPressed() {

    }







}