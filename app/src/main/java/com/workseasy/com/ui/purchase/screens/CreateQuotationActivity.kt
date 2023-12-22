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
import com.hr.hrmanagement.databinding.ActivityCreateQuotationBinding
import com.workseasy.com.ui.hradmin.attendance.response.AutoAttendanceDto
import com.workseasy.com.ui.purchase.response.CreateQuotationDto
import com.workseasy.com.ui.purchase.response.Grade
import com.workseasy.com.ui.purchase.response.ItemData
import com.workseasy.com.ui.purchase.response.Make
import com.workseasy.com.ui.purchase.response.PaymentTerm
import com.workseasy.com.ui.purchase.response.Vendor
import com.workseasy.com.ui.purchase.viewmodel.QuotationViewModel
import com.workseasy.com.ui.purchase.viewmodel.RaisePrViewModel
import java.util.Calendar

class CreateQuotationActivity : AppCompatActivity() {
    lateinit var binding: ActivityCreateQuotationBinding
    val viewModel: RaisePrViewModel by viewModels()
    val quotationViewModel: QuotationViewModel by viewModels()

    var pr_id: Int? = 0
    var makeDto: List<Make>? = null
    var gradeDto: List<Grade>? = null
    var paymentTermDto: List<PaymentTerm>? = null
    var vendorDto: List<Vendor>? = null


    var makeArray = mutableListOf<String>()
    var gradeArray = mutableListOf<String>()
    var gstArray = mutableListOf<String>()
    var paymentTermArray = mutableListOf<String>()
    var vendorArray = mutableListOf<String>()


    var selectedGst: String? = null
    var selectedMake: String? = null
    var selectedGrade: String? = null
    var selectedPaymentTerm: String? = null
    var selectedVendor: String? = null


    var deliverydatestr: String = ""
    var quotationId: String = ""
    var quantity: String? = ""
    var progressDialog:ProgressDialog?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_quotation)
        pr_id = intent.getIntExtra("pr_id", 0)
        quantity = intent.getStringExtra("quantity")
        progressDialog = ProgressDialog(this)
        binding.qtyEt.setText(quantity)

        binding.backBtn.setOnClickListener {
            finish()
        }
        gstArray.add(0, "Inclusive")
        gstArray.add(1, "Extra")
        gstArray.add(2, "N/A")
        binding.gstSpinner.setItems(gstArray)

        apiCallForQuotationPr(pr_id)
        responseHandle()

            binding.gstSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", "" + oldIndex + oldItem + newIndex + newText)
            selectedGst = newText
        }

        binding.makeSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", "" + oldIndex + oldItem + newIndex + newText)
            selectedMake = newText
        }

        binding.gradeSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", "" + oldIndex + oldItem + newIndex + newText)
            selectedGrade = newText
        }

        binding.paymentTermsSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", "" + oldIndex + oldItem + newIndex + newText)
            selectedPaymentTerm = newText
        }

        binding.vendorSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", "" + oldIndex + oldItem + newIndex + newText)
            selectedVendor = newText
        }

        binding.SubmitBtn.setOnClickListener {
            var disc = binding.discEt.text.toString();
            var qty = binding.qtyEt.text.toString()
            var unit = binding.unitEt.text.toString()
            var rate = binding.rateEt.text.toString()
            var fraight = binding.fraightEt.text.toString()
            var rmk = binding.rmkEt.text.toString();
            callApiForCreateQuotation(
                CreateQuotationDto(
                    deliverydatestr,
                    disc, fraight, selectedGst!!, selectedMake!!, selectedPaymentTerm!!, qty,
                    quotationId,
                    rate, unit, selectedVendor!!
                )
            )
            createQuotationResponseHandle()
        }

        binding.tvdate.setOnClickListener {
            var today = Calendar.getInstance()
            var c = Calendar.getInstance()
            var year = c.get(Calendar.YEAR)
            var month = c.get(Calendar.MONTH)
            var day = c.get(Calendar.DAY_OF_MONTH)
            var dayofmonth = ""
            var monthofyear = ""
            var selectedyear = ""

            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    selectedyear = year.toString()
                    monthofyear = monthOfYear.toString()
                    dayofmonth = dayOfMonth.toString()

                    if (monthofyear.toInt() < 8)
                        monthofyear = "0" + (monthofyear.toInt() + 1);
                    else
                        monthofyear = (monthofyear.toInt() + 1).toString();

                    if (dayofmonth.toString().length == 1) {
                        dayofmonth = "0" + dayofmonth;
                    }
                    deliverydatestr =
                        dayofmonth.toString() + "-" + monthofyear + "-" + year.toString()
                    binding.tvdate.setText(deliverydatestr)
                },
                year,
                month,
                day
            )
            dpd.show()
        }

    }

    fun apiCallForQuotationPr(prid: Int?) {
        viewModel?.getPrListForQuotation(this, 8)
    }

    fun responseHandle() {
        viewModel?.quotationPrResponse?.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.status == 200) {
                        binding.progressbar.visibility = View.GONE
                        if (dataSate.data.data!!.pr_details != null) {
                            var prdetails = dataSate.data.data!!.pr_details
                            binding.scrollviewlayout.visibility = View.VISIBLE
                            makeDto = dataSate.data.data.makes;
                            gradeDto = dataSate.data.data.grades;
                            paymentTermDto = dataSate.data.data.paymentTerms;
                            vendorDto = dataSate.data.data.vendors;
                            setMakeSpinner(makeDto!!);
                            setGradeSpinner(gradeDto!!)
                            setPaymentSpinner(paymentTermDto!!)
                            setVendorSpinner(vendorDto!!)
                            quotationId = dataSate.data.data.quotationId.toString()


                        } else {
                            binding.scrollviewlayout.visibility = View.GONE
                        }
                    } else {
                        binding.progressbar.visibility = View.GONE
                    }
                }

                is com.workseasy.com.network.DataState.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }

                is com.workseasy.com.network.DataState.Error -> {
                    binding.progressbar.visibility = View.GONE
//                    Toast.makeText(activity, "" + dataSate.exception, Toast.LENGTH_SHORT)
//                        .show()
                }
            }
        }
    }

    fun setMakeSpinner(makeList: List<Make>) {
        makeArray.clear()
        for (i in 0 until makeList!!.size) {
            makeArray?.add(makeList[i].name)
        }
        binding.makeSpinner.setItems(makeArray!!.toList())

    }

    fun setGradeSpinner(gradeList: List<Grade>) {
        gradeArray.clear()
        for (i in 0 until gradeList!!.size) {
            gradeArray?.add(gradeList[i].name)
        }
        binding.gradeSpinner.setItems(gradeArray!!.toList())

    }

    fun setPaymentSpinner(paymentTermList: List<PaymentTerm>) {
        paymentTermArray.clear()
        for (i in 0 until paymentTermList!!.size) {
            paymentTermArray?.add(paymentTermList[i].name)
        }
        binding.paymentTermsSpinner.setItems(paymentTermArray!!.toList())

    }

    fun setVendorSpinner(vendorList: List<Vendor>) {
        vendorArray.clear()
        for (i in 0 until vendorList!!.size) {
            vendorArray?.add(vendorList[i].name)
        }
        binding.vendorSpinner.setItems(vendorArray!!.toList())

    }

    fun callApiForCreateQuotation(createQuotationDto: CreateQuotationDto) {
        quotationViewModel.createQuotation(createQuotationDto, this)
    }

    fun createQuotationResponseHandle() {
        quotationViewModel?.createquotationResponse?.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.code == 200) {
                        Toast.makeText(
                            this,
                            "Data Saved Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        progressDialog!!.dismiss()
                        finish()
                    } else {
                        progressDialog!!.dismiss()
                        Toast.makeText(
                            this,dataSate.data.message ,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                is com.workseasy.com.network.DataState.Loading -> {
                    progressDialog!!.show();
                    progressDialog!!.setMessage("Please Wait")

                }

                is com.workseasy.com.network.DataState.Error -> {
                    binding.progressbar.visibility = View.GONE
                }
            }
        }
    }

}