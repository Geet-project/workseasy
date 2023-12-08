package com.workseasy.com.ui.hradmin.attendance

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityAttendanceForm1Binding
import com.squareup.picasso.Picasso
import com.workseasy.com.utils.SharedPref
import java.text.SimpleDateFormat
import java.util.*

class AttendanceForm1Activity : AppCompatActivity() {
    lateinit var _binding: ActivityAttendanceForm1Binding
    var emp_id: Int?=null
    var emp_code: String?=null
    var empname: String?=null
    var photo: String?=null
    var designation: String?=null
    var department :String? =null
    var mobile: String?=null
    val viewModel: AttendanceViewModel by viewModels()
    var currentDate: String?=null
    var progressDialog: ProgressDialog?=null
    val binding get() = _binding!!
    var dobstr: String = ""
    var sharedPref: SharedPref? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this,R.layout.activity_attendance_form1)
        emp_id = intent.getIntExtra("employee_id",0)
        emp_code = intent.getStringExtra("empCode")
        empname = intent.getStringExtra("empName")
        photo = intent.getStringExtra("photo")
        mobile = intent.getStringExtra("mobile")
        designation = intent.getStringExtra("designation")
        department = intent.getStringExtra("department")
        progressDialog = ProgressDialog(this)
        sharedPref = SharedPref(this)
        dobstr = sharedPref!!.getData("dailydate", "").toString();
        if(dobstr!=null)
            binding.tvdate.text = dobstr;

        setListeners()
        setUI()
        responseHandle()
    }

    fun setUI()
    {
        if(photo==null || photo.equals(""))
            binding.empProfilePhoto.setImageResource(R.drawable.profilephoto)
        else
            Picasso.get().load(photo).into(binding.empProfilePhoto)

        binding.empName.text = empname
        binding.empNumber.text = mobile
        binding.empCode.text = emp_code
        binding.empDesignation.text = designation
        binding.empDepartment.text = department
        val sdf = SimpleDateFormat("dd-M-yyyy")
        currentDate = sdf.format(Date())
        binding.currentDate.text = currentDate
    }


    fun setListeners()
    {
        binding.nextBtn.setOnClickListener {
            var punchinTime: String = binding.punchinTv.text.toString().replace(" ", "")
            var punchoutTime: String = binding.punchoutTv.text.toString().replace(" ", "")
            var dateToSend: String = binding.tvdate.text.toString();
            if(dateToSend.equals("")) {
                dateToSend = currentDate!!;
            }

            if(dateToSend.equals("") || punchoutTime.equals("") || punchinTime.equals("")){
                callApiForDailyAttendance("next", emp_id, "daily")
                paginationResponseHandle()
            }else{
                callApi(dateToSend, punchinTime, punchoutTime, emp_id)
                responseHandle()
            }
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

        binding.punchinTv.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)

            mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    binding.punchinTv.setText(String.format("%d : %d", hourOfDay, minute))
                    binding.punchinError.visibility = View.GONE
                }
            }, hour, minute, true)
            mTimePicker.show()
        }

        binding.punchoutTv.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)

            mTimePicker = TimePickerDialog(this ,object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    binding.punchoutTv.setText(String.format("%d : %d", hourOfDay, minute))
                    binding.punchoutError.visibility = View.GONE
                }
            }, hour, minute, true)
            mTimePicker.show()
        }

        binding.prevBtn.setOnClickListener {
            callApiForDailyAttendance("previous", emp_id, "daily")
            paginationResponseHandle()
        }

        binding.backBtn.setOnClickListener {
            finish()
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

            val dpd = DatePickerDialog (
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    selectedyear = year.toString()
                    monthofyear = monthOfYear.toString()
                    dayofmonth = dayOfMonth.toString()

                    if (monthofyear.toString().length == 1) {
                        if(monthofyear.toInt()<8)
                            monthofyear = "0" + (monthofyear.toInt() + 1);
                        else
                            monthofyear =  (monthofyear.toInt() + 1).toString();
                    }else{
                        monthofyear =  (monthofyear.toInt() + 1).toString();
                    }

                    if (dayofmonth.toString().length == 1) {
                        dayofmonth = "0" + dayofmonth;
                    }

                    dobstr = dayofmonth.toString() + "-" + monthofyear + "-" + year.toString()
                    binding.tvdate.setText(dobstr)
                    sharedPref!!.saveData("dailydate", dobstr)
                },
                year,
                month,
                day
            )
            dpd.show()
        }
    }

    fun callApi(date: String?, punchin: String?, punchout: String?, employeeid: Int? )
    {
        viewModel.add_daily_attendance(date, punchin, punchout, employeeid, this)
    }

    fun callApiForDailyAttendance(page: String?, employee_id: Int?,type: String?)
    {
        viewModel.attendancePaginationDaily(page, employee_id, type, this)
    }

    fun responseHandle()
    {
        viewModel?.attendance1Response?.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success -> {
                    if(dataSate.data.status==200)
                    {
                        progressDialog?.dismiss()
                        callApiForDailyAttendance("next", emp_id, "daily")
                        paginationResponseHandle()
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

    fun paginationResponseHandle()
    {
        viewModel?.paginationResponseDaily?.observe(this)
        { dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success -> {
                    if(dataSate.data.status==200)
                    {
                        progressDialog?.dismiss()
                        if(dataSate.data.data!=null)
                        {
                            empname = dataSate.data.data.name
                            designation = dataSate.data.data.designation
                            emp_id = dataSate.data.data.id
                            emp_code = dataSate.data.data.pay_code?.toString()
                            if(dataSate.data.data.photo!=null && !dataSate.data.data.photo.toString().isNullOrEmpty())
                                photo = dataSate.data.data.photo.toString()
                            mobile= dataSate.data.data.mob_no
                            binding.punchinTv.text = ""
                            binding.punchoutTv.text = ""
                            setUI()
                        }else{
                            Toast.makeText(this, "No Employee's Data Found", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        progressDialog?.dismiss()
                        Toast.makeText(this, dataSate.data.message, Toast.LENGTH_SHORT).show()
                        viewModel.paginationResponseDaily.removeObservers(this@AttendanceForm1Activity)
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    progressDialog?.setMessage("Please Wait..")
                    progressDialog?.show()

                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressDialog?.dismiss()
                    Toast.makeText(this, ""+dataSate.exception, Toast.LENGTH_SHORT).show()
                    viewModel.paginationResponseDaily.removeObservers(this@AttendanceForm1Activity)

                }
            }
        }
    }
}