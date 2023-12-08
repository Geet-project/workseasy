package com.workseasy.com.ui.accounts

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityPaymentBinding
import com.workseasy.com.ui.hradmin.employeeRegistration.response.Asset
import com.workseasy.com.utils.GenericTextWatcher

class PaymentActivity : AppCompatActivity() {
    lateinit var binding: ActivityPaymentBinding
    val viewModel: PaymentViewModel by viewModels()
    var selectedPaymentStatus : String=""
    var selectedPayMode : String = ""
    var progressDialog: ProgressDialog?=null
    var assetDto: List<Asset>?=null
    var assetArray = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment)
        progressDialog= ProgressDialog(this)
        setListeners()
        responseHandle()
        setValidationListeners()
        callApiForSpinnerAsset("payment status")
        callApiForSpinnerAsset("Payment Mode")
        spinnerAssetresponseHandle()

    }
    fun setListeners()
    {
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.Paymentspinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
            selectedPaymentStatus = newText
            binding.paymentStatusError.visibility = View.GONE
        }
        binding.payModespinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
            selectedPayMode = newText
            binding.paymodeError.visibility = View.GONE
        }
        binding.nextBtn.setOnClickListener {
            var beneficiaryname = binding.BeneficiaryEt.text.toString()
            var emp_code= binding.EmpCodeEt.text.toString()
            var paymentHead = binding.PaymentHeadEt.text.toString()
            var paymentDate = binding.PaymentDateEt.text.toString()
            var transRef = binding.TransactionEt.text.toString()
            var remarks = binding.RemarkEt.text.toString()

            if(beneficiaryname.equals(""))
            {
                binding.BeneficiaryEt.requestFocus()
                binding.beneficaryError.visibility = View.VISIBLE
                binding.beneficaryError.text = "*Please enter Beneficiary Name"
            }else if(emp_code.equals(""))
            {
                binding.EmpCodeEt.requestFocus()
                binding.empCodeError.visibility = View.VISIBLE
                binding.empCodeError.text = "*Please enter Employee Code"
            }else if(paymentHead.equals(""))
            {
                binding.PaymentHeadEt.requestFocus();
                binding.headError.visibility = View.VISIBLE
                binding.headError.text = "*Please enter Payment Head"
            }else if(selectedPaymentStatus.equals(""))
            {
                binding.paymentStatusError.visibility = View.VISIBLE
                binding.paymentStatusError.text = "*Please select Payment Status"
            }else if(paymentDate.equals(""))
            {
                binding.PaymentDateEt.requestFocus()
                binding.paymentDateError.visibility = View.VISIBLE
                binding.paymentDateError.text = "*Please enter Payment Date"
            }else if(selectedPayMode.equals(""))
            {
                binding.paymodeError.visibility = View.VISIBLE
                binding.paymodeError.text = "*Please select Payment Mode"
            }else if(transRef.equals(""))
            {
                binding.trasRefError.visibility = View.VISIBLE
                binding.trasRefError.text = "*Please enter Transaction Ref."
                binding.TransactionEt.requestFocus()
            }else if(remarks.equals(""))
            {
                binding.RemarkEt.requestFocus();
                binding.remarksError.visibility = View.VISIBLE
                binding.remarksError.text = "*Please enter Remarks"
            }else{
                apiCallForPaymentSubmit(beneficiaryname, emp_code, paymentHead,selectedPaymentStatus, paymentDate,selectedPayMode,
                transRef, remarks)
            }
        }
    }

    fun apiCallForPaymentSubmit(beneficiary_name: String,
                                employee_code: String,
                                payment_head: String,
                                payment_status: String,
                                payment_date: String,
                                payment_mode: String,
                                transaction_ref: String,
                                remark: String)
    {
        viewModel.paymentPost(beneficiary_name, employee_code, payment_head, payment_status, payment_date,
            payment_mode, transaction_ref, remark,this)
    }

    fun responseHandle()
    {
        viewModel?.paymentupdateResponse?.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
                    if(dataSate.data.status==200)
                    {
                        progressDialog?.dismiss()
                        Toast.makeText(this, "Payment Form Submitted", Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        progressDialog?.dismiss()
                        Toast.makeText(this, dataSate.data.error, Toast.LENGTH_SHORT).show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    progressDialog?.setMessage("Please Wait..")
                    progressDialog?.show()
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressDialog?.dismiss()
                    Toast.makeText(this, ""+dataSate.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun setValidationListeners()
    {
        binding.BeneficiaryEt.addTextChangedListener(GenericTextWatcher(binding.BeneficiaryEt, null, binding.beneficaryError))
        binding.EmpCodeEt.addTextChangedListener(GenericTextWatcher(binding.EmpCodeEt, null, binding.empCodeError))
        binding.PaymentHeadEt.addTextChangedListener(GenericTextWatcher(binding.PaymentHeadEt, null, binding.headError))
        binding.PaymentDateEt.addTextChangedListener(GenericTextWatcher(binding.PaymentDateEt, null, binding.paymentDateError))
        binding.TransactionEt.addTextChangedListener(GenericTextWatcher(binding.TransactionEt, null, binding.trasRefError))
        binding.RemarkEt.addTextChangedListener(GenericTextWatcher(binding.RemarkEt, null, binding.remarksError))
    }

    fun callApiForSpinnerAsset(type: String)
    {
        viewModel.getAssets(type, this)
    }

    fun spinnerAssetresponseHandle()
    {
        viewModel.spinnerResponse.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
                    if(dataSate.data.status==200)
                    {
                        progressDialog?.dismiss()
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
    }

    fun setAssetSpinner(assetList: List<Asset>, type: String)
    {
        assetArray.clear()
        for (i in 0 until assetList!!.size) {
            assetArray?.add(assetList[i].name)
        }

        if(type.equals("Payment Status"))
        {
            binding.Paymentspinner.setItems(assetArray!!.toList() )
        }else if(type.equals("Payment Mode"))
        {
            binding.payModespinner.setItems(assetArray!!.toList())
        }
    }
}
