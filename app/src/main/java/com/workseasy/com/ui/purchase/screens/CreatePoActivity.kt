package com.workseasy.com.ui.purchase.screens

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.hr.hrmanagement.R
import androidx.lifecycle.viewModelScope
import com.hr.hrmanagement.databinding.ActivityCreatePoBinding
import com.workseasy.com.ui.purchase.response.*
import com.workseasy.com.ui.purchase.viewmodel.CreatePoViewModel
import com.workseasy.com.ui.purchase.viewmodel.RaisePrViewModel

class CreatePoActivity : AppCompatActivity() {
    lateinit var binding: ActivityCreatePoBinding
    var progressBar: ProgressDialog? = null
    var vendorDto: List<VendorListData>? = null
    var quotationDto: List<QuotationPoListData>? = null
    var conditionDto: List<CondionDataItem>? = null
    var previousconditionDto :List<PreviousConditionDataItem>?=null


    var vendorArray = mutableListOf<String>()
    var vendorIdArray = mutableListOf<Int>()
    var viewModel: CreatePoViewModel ?=null
    var selectVendorItem: String?= null
    var selectedVendorId: Int?= null

    var assetId = mutableListOf<Int>()
    var quotationArray = ArrayList<String>()
    var quotationIdArray = ArrayList<Int>()
    var selectedQuotationIdArray = ArrayList<Int>()
    var selectedQuotationPositionArray = ArrayList<Int>()

    var remakrsArray = ArrayList<String>()
    var remakrsIdArray = ArrayList<Int>()
    var selectedRemarksIdArray = ArrayList<Int>()
    var selectedRemarksPositionArray = ArrayList<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_po)
        progressBar = ProgressDialog(this)
        viewModel = ViewModelProviders.of(this).get(CreatePoViewModel::class.java)
        callApiForVendorList()
        vendorListResponseHandle()

        binding.vendorSpinner.setOnSpinnerItemSelectedListener <String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
            selectVendorItem = newText
            var index = vendorArray.indexOf(selectVendorItem)
            selectedVendorId = vendorIdArray.get(index)
            callApiForQuotationList(selectedVendorId)
            quotationResponseHandle()
            binding.itemError.visibility = View.GONE
        }

        binding.SubmitBtn.setOnClickListener {
            for (i in 0 until selectedQuotationPositionArray!!.size) {
                selectedQuotationIdArray.add(quotationIdArray.get(selectedQuotationPositionArray.get(i)))
            }

            for (i in 0 until selectedRemarksPositionArray!!.size) {
                selectedRemarksIdArray.add(remakrsIdArray.get(selectedRemarksPositionArray.get(i)))
            }
            viewModel!!.createPoDto(CreatePoDto(selectedQuotationIdArray,selectedRemarksIdArray,selectedVendorId.toString()), this)
            createPoResponseHandle()
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    fun callApiForVendorList() {
        viewModel!!.getVendorList(this)
    }


    fun vendorListResponseHandle() {
        viewModel!!.vendorListResponse.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.code == 200) {
                        progressBar?.dismiss()
                        vendorDto = dataSate.data.data
                        setVendorSpinner(vendorDto!!)
                    } else {
                        progressBar?.dismiss()
                        Toast.makeText(this, dataSate.data.message, Toast.LENGTH_SHORT).show()
                    }
                }

                is com.workseasy.com.network.DataState.Loading -> {
                    progressBar?.show()
                    progressBar?.setMessage("Please Wait..")
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressBar?.dismiss()
                    Toast.makeText(this, "" + dataSate.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun setVendorSpinner(vendorList: List<VendorListData>) {
        vendorArray.clear()
        vendorIdArray.clear()
        for (i in 0 until vendorList!!.size) {
            vendorArray?.add(vendorList[i].name)
            vendorIdArray?.add(vendorList[i].id)
        }
        binding.vendorSpinner.setItems(vendorArray!!.toList())
    }

    fun setQuotationSpinner(quotationPoList: List<QuotationPoListData>) {
        quotationArray.clear()
        for (i in 0 until quotationPoList!!.size) {
            quotationArray?.add(quotationPoList[i].name)
            quotationIdArray?.add(quotationPoList[i].quotation_id)
        }

        val testDataList = quotationArray
        with(binding) {
            multiSelectSpinner.buildCheckedSpinner(testDataList) {
                    selectedPositionList, displayString ->
                selectedQuotationPositionArray = selectedPositionList
            }
        }
    }

    fun setRemarksSpinner(quotationPoList: List<CondionDataItem>) {
        remakrsArray.clear()
        for (i in 0 until quotationPoList!!.size) {
            remakrsArray?.add(quotationPoList[i].name)
            remakrsIdArray?.add(quotationPoList[i].id)
        }

        val testDataList = remakrsArray
        with(binding) {
            careRemarksSpinner.buildCheckedSpinner(testDataList) {
                    selectedPositionList, displayString ->
                selectedRemarksPositionArray = selectedPositionList
            }
        }


    }

    fun callApiForQuotationList(vendorId: Int?) {
        viewModel!!.getPoQuotationList(vendorId,this)
    }

    fun quotationResponseHandle() {
        viewModel!!.quotationListResponse.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.code == 200) {
                        progressBar?.dismiss()
                        quotationDto = dataSate.data.data
                        conditionDto = dataSate.data.condition
                        setQuotationSpinner(quotationDto!!)
                        setRemarksSpinner(conditionDto!!)
//                        previousconditionDto = dataSate.data.previousConditions;
                    } else {
                        progressBar?.dismiss()
                        Toast.makeText(this, dataSate.data.message, Toast.LENGTH_SHORT).show()
                    }
                }

                is com.workseasy.com.network.DataState.Loading -> {
                    progressBar?.show()
                    progressBar?.setMessage("Please Wait..")
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressBar?.dismiss()
                    Toast.makeText(this, "" + dataSate.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun createPoResponseHandle() {
        viewModel!!.createPoResponse.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.code == 200) {
                        progressBar?.dismiss()
                        Toast.makeText(this, dataSate.data.message, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, ApprovePoActivity::class.java)
//                    .putExtra("formtype", 5)
                        startActivity(intent)
                    } else {
                        progressBar?.dismiss()
                        Toast.makeText(this, dataSate.data.message, Toast.LENGTH_SHORT).show()
                    }
                }

                is com.workseasy.com.network.DataState.Loading -> {
                    progressBar?.show()
                    progressBar?.setMessage("Please Wait..")
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressBar?.dismiss()
                    Toast.makeText(this, "" + dataSate.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}