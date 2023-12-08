package com.workseasy.com.ui.hradmin.employeeRegistration

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityHrDetailsBinding
import com.workseasy.com.ui.hradmin.employeeDetails.response.Category
import com.workseasy.com.ui.hradmin.employeeDetails.response.WeekDay
import com.workseasy.com.ui.hradmin.employeeRegistration.viewmodel.SignupViewModel
import java.util.Calendar

class HrDetailsActivity : AppCompatActivity() { 
    val viewModel: SignupViewModel by viewModels()
    lateinit var binding: ActivityHrDetailsBinding
    var employee_id: Int = 0
    lateinit var progressDialog: ProgressDialog
    var weeklyoffArray = mutableListOf<String>()
    var categoryArray = mutableListOf<String>()

    var selectedGrade: String = ""
    var selectedPaymentMode: String = ""
    var selectedShift: String = ""
    var selectedFirstWeeklyOff: String = ""
    var selectedSecondWeeklyOff: String = ""
    var selectedCategory: String = ""

    var dobstr: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hr_details)
        employee_id = intent.getIntExtra("employee_id", 0)
        progressDialog = ProgressDialog(this)

        callApiForData(employee_id.toString())
        responseHandle()

        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.LateArrTv.setOnClickListener {
            tiemPicker(binding.LateArrTv)
        }

        binding.earlydeptv.setOnClickListener {
            tiemPicker(binding.earlydeptv)
        }

        binding.halfDayTv.setOnClickListener {
            tiemPicker(binding.halfDayTv)
        }

        binding.presentMarkingTv.setOnClickListener {
            tiemPicker(binding.presentMarkingTv)
        }

        binding.presentMarkingTv.setOnClickListener {
            tiemPicker(binding.presentMarkingTv)
        }


        binding.tvdoL.setOnClickListener {
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

                    if (monthofyear.toString().length == 1) {
                        monthofyear = "0" + (monthofyear.toInt() + 1);
                    }

                    if (dayofmonth.toString().length == 1) {
                        dayofmonth = "0" + dayofmonth;
                    }

                    dobstr = dayofmonth.toString() + "-" + monthofyear + "-" + year.toString()
                    binding.tvdoL.setText(dobstr)
                },
                year,
                month,
                day
            )
            dpd.getDatePicker().setMaxDate(System.currentTimeMillis())
            dpd.show()
        }


        binding.savebtn.setOnClickListener {
            var pfno = binding.PFEt.text.toString()
            var uannumber = binding.UanEt.text.toString()
            var esic = binding.etEsic.text.toString()
            var reasonofleaving = binding.ReasonLeavingEt.text.toString()
//            if (pfno.equals("")) {
//                binding.pferror.visibility = View.VISIBLE
//                binding.pferror.setText("*Please enter pf number")
//                binding.PFEt.requestFocus()
//            } else if (uannumber.equals("")) {
//                binding.uanerror.visibility = View.VISIBLE
//                binding.uanerror.setText("*Please enter uan number")
//                binding.UanEt.requestFocus()
//            } else if (esic.equals("")) {
//                binding.esicError.visibility = View.VISIBLE
//                binding.esicError.setText("*Please enter esic number")
//                binding.etEsic.requestFocus()
//            } else {
//
//            }

            callApiForStep3(
                employee_id,
                selectedGrade,
                uannumber,
                esic,
                pfno,
                selectedShift,
                selectedPaymentMode,
                reasonofleaving,
                binding.tvdoL.text.toString(),
                binding.LateArrTv.text.toString(),
                binding.earlydeptv.text.toString(),
                binding.presentMarkingTv.text.toString(),
                binding.halfDayTv.text.toString(),
                selectedFirstWeeklyOff,
                selectedSecondWeeklyOff,
                selectedCategory
            )
            responseHandle()

        }

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.gradeSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", "" + oldIndex + oldItem + newIndex + newText)
            selectedGrade = newText
        }

        binding.paymentSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", "" + oldIndex + oldItem + newIndex + newText)
            selectedPaymentMode = newText
        }

        binding.shiftSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", "" + oldIndex + oldItem + newIndex + newText)
            selectedShift = newText
        }

        binding.firstlyOffSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", "" + oldIndex + oldItem + newIndex + newText)
            selectedFirstWeeklyOff = newText
        }

        binding.secondWeeklyOffSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", "" + oldIndex + oldItem + newIndex + newText)
            selectedSecondWeeklyOff = newText
        }

        binding.categorySpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", "" + oldIndex + oldItem + newIndex + newText)
            selectedCategory = newText
        }


    }

    fun callApiForData(employee_id: String) {
        viewModel.getStep3Data(employee_id, this)
    }

    fun responseHandle() {
        viewModel.step3Response.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.code == 200) {
                        progressDialog.dismiss()
                        setPaymentModeSpinner(dataSate.data.paymentMode)
                        setGradeSpinner(dataSate.data.gradeList)
                        setCompanyShiftSpinner(dataSate.data.companyShift)
                        setWeeklyOffSpinner(dataSate.data.weekDays)
                        setCategorySpinner(dataSate.data.category)
                    } else {
                        Toast.makeText(this, dataSate.data.message, Toast.LENGTH_SHORT).show()
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

        viewModel.signupResponse.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.status == 200) {
                        progressDialog?.dismiss()
                        Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, EmployeeRegistrationStep4::class.java)
                        intent.putExtra("employee_id", dataSate.data.data.id)
                        startActivity(intent)
                        finish()
                    } else {
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
                    Toast.makeText(this, "" + dataSate.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun setPaymentModeSpinner(paymentList: List<String>) {
        binding.paymentSpinner.setItems(paymentList)
    }

    fun setGradeSpinner(gradeList: List<String>) {
        binding.gradeSpinner.setItems(gradeList)
    }

    fun setCompanyShiftSpinner(shiftList: List<String>) {
        binding.shiftSpinner.setItems(shiftList)
    }

    fun setCategorySpinner(category: List<Category>) {
        for (i in 0 until category!!.size) {
            categoryArray?.add(category[i].value)
        }
        binding.categorySpinner.setItems(categoryArray)
    }

    fun setWeeklyOffSpinner(weeklyList: List<WeekDay>) {
        for (i in 0 until weeklyList!!.size) {
            weeklyoffArray?.add(weeklyList[i].value)
        }
        binding.secondWeeklyOffSpinner.setItems(weeklyoffArray)
        binding.firstlyOffSpinner.setItems(weeklyoffArray)
    }

    private fun tiemPicker(editText: TextView) {
        // Get Current Time
        val c = Calendar.getInstance()
        var mHour = c[Calendar.HOUR_OF_DAY]
        var mMinute = c[Calendar.MINUTE]
        var am_pm: String


        // Launch Time Picker Dialog
        val timePickerDialog = TimePickerDialog(
            this,
            { view, hourOfDay, minute ->
                mHour = hourOfDay
                mMinute = minute
                if (mHour > 12) {
                    mHour = mHour - 12;
                    am_pm = "PM";
                } else {
                    am_pm = "AM";
                }
                editText.setText(hourOfDay.toString() + ":" + minute.toString())
            }, mHour, mMinute, true
        )
        timePickerDialog.show()
    }

    fun callApiForStep3(
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
    ) {
        viewModel.step3Register(
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
            category,
            this
        )
    }

}