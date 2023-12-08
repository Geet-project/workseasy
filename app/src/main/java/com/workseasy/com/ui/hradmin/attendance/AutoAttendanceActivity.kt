package com.workseasy.com.ui.hradmin.attendance

import android.app.DatePickerDialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityAttendanceForm1Binding
import com.hr.hrmanagement.databinding.ActivityAutoAttendanceBinding
import com.squareup.picasso.Picasso
import com.workseasy.com.ui.hradmin.attendance.response.AttendanceWithLocationDto
import com.workseasy.com.ui.hradmin.attendance.response.AutoAttendanceDto
import com.workseasy.com.utils.SharedPref
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class AutoAttendanceActivity : AppCompatActivity () {
    lateinit var _binding: ActivityAutoAttendanceBinding
    var emp_id: Int? = null
    var emp_code: String? = null
    var empname: String? = null
    var photo: String? = null
    var designation: String? = null
    var department: String? = null
    var mobile: String? = null
    val viewModel: AttendanceViewModel by viewModels()
    var currentDate: String? = null
    var progressDialog: ProgressDialog? = null
    val binding get() = _binding!!
    var fromdatestr: String = ""
    var todatestr: String = ""

    var sharedPref: SharedPref? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_auto_attendance)
        emp_id = intent.getIntExtra("employee_id", 0)
        emp_code = intent.getStringExtra("empCode")
        empname = intent.getStringExtra("empName")
        photo = intent.getStringExtra("photo")
        mobile = intent.getStringExtra("mobile")
        designation = intent.getStringExtra("designation")
        department = intent.getStringExtra("department")
        progressDialog = ProgressDialog(this)
        sharedPref = SharedPref(this)
        setUI()

        binding.fromDate.setOnClickListener {
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

                    if(monthofyear.toInt()<8)
                        monthofyear = "0" + (monthofyear.toInt() + 1);
                    else
                        monthofyear =  (monthofyear.toInt() + 1).toString();

                    if (dayofmonth.toString().length == 1) {
                        dayofmonth = "0" + dayofmonth;
                    }
                    fromdatestr = dayofmonth.toString() + "-" + monthofyear + "-" + year.toString()
                    binding.fromDate.setText(fromdatestr)
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        binding.toDate.setOnClickListener {
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

                    if(monthofyear.toInt()<8)
                        monthofyear = "0" + (monthofyear.toInt() + 1);
                    else
                        monthofyear =  (monthofyear.toInt() + 1).toString();

                    if (dayofmonth.toString().length == 1) {
                        dayofmonth = "0" + dayofmonth;
                    }
                    todatestr = dayofmonth.toString() + "-" + monthofyear + "-" + year.toString()
                    binding.toDate.setText(todatestr)
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        binding.nextBtn.setOnClickListener {
            var fromDatetoSend: String = binding.fromDate.text.toString();
            var toDatetoSend: String = binding.toDate.text.toString();
            callApiForAutoAttendancePost(AutoAttendanceDto(emp_id!!, toDatetoSend, fromDatetoSend))
            responseHandle()
//            if(punchinTime.equals("")|| punchinTime == null)
//            {
//                binding.punchinError.visibility = View.VISIBLE
//                binding.punchinError.text= "*Please enter Punch in time."
//            }else if(punchoutTime.equals("")||punchoutTime==null)
//            {
//                binding.punchoutError.visibility  = View.VISIBLE
//                binding.punchoutError.text= "*Please enter Punch Out time."
//            }else{
//
//            }
        }
        binding.prevBtn.setOnClickListener {
            callApiForPagination("previous", emp_id, "daily")
            paginationResponseHandle()
        }
    }

    fun setUI() {
        if (photo == null || photo.equals(""))
            binding.empProfilePhoto.setImageResource(R.drawable.profilephoto)
        else
            Picasso.get().load(photo).into(binding.empProfilePhoto)
        binding.empName.text = empname
        binding.empNumber.text = mobile
        binding.empCode.text = emp_code
        binding.empDesignation.text = designation
        binding.empDepartment.text = department
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        currentDate = sdf.format(Date())
        binding.currentDate.text = currentDate
    }

    fun callApiForAutoAttendancePost(autoAttendanceDto: AutoAttendanceDto) {
        viewModel.employeeAutoAttendance(autoAttendanceDto, this)
    }

    fun responseHandle() {
        viewModel?.postAutoAttendanceResponse?.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.code == 200) {
                        progressDialog?.dismiss()
                        callApiForPagination("next", emp_id, "daily")
                        paginationResponseHandle()

                    } else {
                        progressDialog?.dismiss()
                        Toast.makeText(this, dataSate.data.message, Toast.LENGTH_SHORT).show()
                    }
                }

                is com.workseasy.com.network.DataState.Loading -> {
                    progressDialog?.setMessage("Please Wait..")
                    progressDialog?.show()
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressDialog?.dismiss()
                    Toast.makeText(this, "" + dataSate.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun callApiForPagination(page: String?, employee_id: Int?, type: String?) {
        viewModel.attendancePaginationDaily(page, employee_id, type, this)
    }

    fun paginationResponseHandle() {
        viewModel?.paginationResponseDaily?.observe(this)
        { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.status == 200) {
                        progressDialog?.dismiss()
                        if (dataSate.data.data != null) {
                            empname = dataSate.data.data.name
                            designation = dataSate.data.data.designation
                            emp_id = dataSate.data.data.id
                            emp_code = dataSate.data.data.pay_code?.toString()
                            if (dataSate.data.data.photo != null && !dataSate.data.data.photo.toString()
                                    .isNullOrEmpty()
                            )
                                photo = dataSate.data.data.photo.toString()
                            mobile = dataSate.data.data.mob_no
                            binding.fromDate.setText("")
                            binding.toDate.setText("")
                            setUI()
                        } else {
                            Toast.makeText(this, "No Employee's Data Found", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        progressDialog?.dismiss()
                        Toast.makeText(this, dataSate.data.message, Toast.LENGTH_SHORT).show()
                        viewModel.paginationResponseDaily.removeObservers(this@AutoAttendanceActivity)
                    }
                }

                is com.workseasy.com.network.DataState.Loading -> {
                    progressDialog?.setMessage("Please Wait..")
                    progressDialog?.show()
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressDialog?.dismiss()
                    Toast.makeText(this, "" + dataSate.exception, Toast.LENGTH_SHORT).show()
                    viewModel.paginationResponseDaily.removeObservers(this@AutoAttendanceActivity)

                }
            }
        }
    }
}