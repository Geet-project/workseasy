package com.workseasy.com.ui.hradmin.employeeRegistration

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivitySalaryStructureBinding
import com.workseasy.com.ui.hradmin.employeeRegistration.adapter.earningadapter
import com.workseasy.com.ui.hradmin.employeeRegistration.adapter.familyadapter
import com.workseasy.com.ui.hradmin.employeeRegistration.viewmodel.SignupViewModel

class SalaryStructureActivity : AppCompatActivity() {
    var employee_id: Int = 0
    lateinit var  binding: ActivitySalaryStructureBinding
    val viewModel: SignupViewModel by viewModels()
    lateinit var progressDialog: ProgressDialog
    lateinit var earningadapter: earningadapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_salary_structure)
        employee_id = intent.getIntExtra("employee_id", 0)
        progressDialog = ProgressDialog(this)
        earningadapter = earningadapter(this)
        apiCallForFamilyDetailList(employee_id);
        responseHandle()
        binding.backBtn.setOnClickListener {
            finish()
        }

        val myArray: Array<String> = resources.getStringArray(R.array.salarytype)
        binding.SalaryTypeSpinner.setItems(myArray.toList())
    }

    fun apiCallForFamilyDetailList(employee_id: Int)
    {
        viewModel.getSalaryDetail(employee_id!!,this)
    }

    fun responseHandle()
    {
        viewModel.getSalaryDetailResponse.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
                    if(dataSate.data.code==200)
                    {
                        binding.epfvalue.setText(dataSate.data.finalData.deductionEpf.toString())
                        binding.vpfvalue.setText(dataSate.data.finalData.deductionVpf.toString())
                        binding.esicvalue.setText(dataSate.data.finalData.deductionEsic.toString())
                        binding.lwfvalue.setText(dataSate.data.finalData.deductionLwf.toString())
                        binding.ptvalue.setText(dataSate.data.finalData.pt_amount.toString())
                        binding.ptvalue.setText(dataSate.data.finalData.pt_amount.toString())
                        binding.epfvaluebenefit.setText(dataSate.data.finalData.benefitEpf.toString())
                        binding.esicValueBenefit.setText(dataSate.data.finalData.benefitEsic.toString())
                        binding.epsvaluebenefit.setText(dataSate.data.finalData.benefitEps.toString())
                        binding.adminvaluebenefit.setText(dataSate.data.finalData.benefitAdmin.toString())
                        binding.lwfValueBenefit.setText(dataSate.data.finalData.benefitLwf.toString())
                        binding.bonusbenefitvalue.setText(dataSate.data.finalData.benefitBonus.toString())
                        binding.monthlynetPaymentvalue.setText(dataSate.data.annual_ctc_data.monthly_net_payment.toString())
                        binding.monthlyBenefitsTv.setText(dataSate.data.annual_ctc_data.monthly_benefits.toString())
                        binding.monthlyCtcTv.setText(dataSate.data.annual_ctc_data.monthly_ctc.toString())
                        binding.AnnualCtcTv.setText(dataSate.data.annual_ctc_data.annual_ctc.toString())

                        if(dataSate.data.earningListData.size>0)
                        {
                            progressDialog?.dismiss()
                            earningadapter.setData(dataSate.data.earningListData)
                            binding.earningListRecycler.adapter  = earningadapter
                            binding.earningListRecycler.visibility = View.VISIBLE

                        }else{
                            progressDialog?.dismiss()
                            binding.earningListRecycler.visibility = View.GONE
                            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        progressDialog?.dismiss()
                        Toast.makeText(this, dataSate.data.message, Toast.LENGTH_SHORT).show()
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

}