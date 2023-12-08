package com.workseasy.com.ui.purchase.screens

import android.app.DatePickerDialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityConsumptionEntryBinding
import com.workseasy.com.ui.hradmin.employeeRegistration.response.Asset
import com.workseasy.com.ui.purchase.viewmodel.RaisePrViewModel
import com.workseasy.com.utils.GenericTextWatcher
import java.util.*

class ConsumptionEntryActivity : AppCompatActivity() {
    lateinit var _binding: ActivityConsumptionEntryBinding
    val binding get() = _binding!!
    var assetDto: List<Asset>?=null
    var assetArray = mutableListOf<String>()
    var selectedItem: String?=null
    var progressDialog: ProgressDialog?=null
    var consumptionDate: String ?=null



    val viewModel: RaisePrViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this,R.layout.activity_consumption_entry)
        progressDialog = ProgressDialog(this)
        setListeners()
        setValidationListeners()
        callApiForSpinnerAsset("Item")
        spinnerAssetresponseHandle()


    }

    fun setListeners() {
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.consumptionDateTv.setOnClickListener { val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // this never ends while debugging
                    // Display Selected date in textbox
                    consumptionDate = dayOfMonth.toString() + "-" + (monthOfYear+1).toString() + "-" + year.toString()
                    binding.consumptionDateTv.setText(consumptionDate)
                    binding.consumptionDateError.visibility= View.GONE
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        binding.itemSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
            selectedItem = newText
            binding.itemError.visibility = View.GONE
        }

        binding.SubmitBtn.setOnClickListener {
            var quantity = binding.QtyEt.text.toString()
            var RemarkValue = binding.RemarkEt.text.toString()

            if(selectedItem==null || selectedItem.equals(""))
            {
                binding.itemError.setText("*Please Select Item")
                binding.itemError.visibility = View.VISIBLE
            }else if(quantity.equals(""))
            {
                binding.QtyEt.requestFocus();
                binding.qtyError.setText("*Please enter Quantity")
                binding.qtyError.visibility = View.VISIBLE
            }else if(consumptionDate==null){
                binding.consumptionDateError.setText("*Please enter Consumption Date.")
                binding.consumptionDateError.visibility = View.VISIBLE
            }else if(RemarkValue.equals(""))
            {
                binding.RemarkEt.requestFocus();
                binding.remarkError.setText("*Please enter Remark")
                binding.remarkError.visibility = View.VISIBLE
            }else{
                callApi(selectedItem!!, quantity, consumptionDate!!, RemarkValue)
                responseHandle()
            }
        }

    }

    fun setValidationListeners()
    {
        binding.QtyEt.addTextChangedListener(GenericTextWatcher(binding.QtyEt, null, binding.qtyError))
        binding.RemarkEt.addTextChangedListener(GenericTextWatcher(binding.RemarkEt, null, binding.remarkError))
    }

    fun callApi(item: String,
                quantity: String,
                date: String,
                remark: String,)
    {
        viewModel.consumptionStore(item, quantity, date, remark, this)
    }

    fun responseHandle()
    {
        viewModel.consumptionstoreResponse.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
                    if(dataSate.data.status==200)
                    {
                        Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show()
                        progressDialog?.dismiss()
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
                    Toast.makeText(this,""+ dataSate.exception, Toast.LENGTH_SHORT).show()
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
        if(type.equals("Item"))
        {
            binding.itemSpinner.setItems(assetArray!!.toList())
        }

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
                        progressDialog!!.dismiss()
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

}