package com.workseasy.com.ui.hradmin.attendance

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityAttendanceForm3Binding
import com.squareup.picasso.Picasso
import com.workseasy.com.utils.GenericTextWatcher
import com.workseasy.com.utils.MonthYearPickerDialog
import java.util.*

class AttendanceForm3Activity : AppCompatActivity() {
    lateinit var _binding: ActivityAttendanceForm3Binding
    var emp_id: Int?=null
    var emp_code: String?=null
    var empname: String?=null
    var photo: String?=null
    var designation: String?=null
    var mobile: String?=null
    val viewModel: AttendanceViewModel by viewModels()
    var progressDialog: ProgressDialog?=null
    val binding get() = _binding!!
    var mDay: Int=0
    var mMonth: Int=0
    var mYear: Int=0
    var date = Date()
    var Year : String?=""
    var Month : String?=null
    var department :String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this,R.layout.activity_attendance_form3)
        emp_id = intent.getIntExtra("employee_id",0)
        progressDialog = ProgressDialog(this)
        emp_code = intent.getStringExtra("empCode")
        empname = intent.getStringExtra("empName")
        photo = intent.getStringExtra("photo")
        mobile = intent.getStringExtra("mobile")
        designation = intent.getStringExtra("designation")
        department = intent.getStringExtra("department")
        setListeners()
        responseHandle()
        setUI()
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

    }

    fun setListeners()
    {
        binding.monthTv.setOnClickListener {
            MonthYearPickerDialog(date).apply {
                setListener { view, year, month, dayOfMonth ->
                    Month = ""+(month+1)
                    Year = "$year"
                    binding.monthTv.text=""+(month+1)+"/$year"
                }
                show(supportFragmentManager, "MonthYearPickerDialog")
            }
        }

        binding.nextBtn.setOnClickListener {
            var ot: String =binding.OtET.text.toString()
            var pd: String = binding.PdET.text.toString()

            if(Year.equals("") || pd.equals("") || ot.equals("")){
                callApiForDailyAttendance("next", emp_id, "monthly")
                paginationResponseHandle()
            }else{
                callApi(ot, pd, Year, Month, emp_id)
                responseHandle()
            }

//            if(Year.isNullOrEmpty()) {
//                Toast.makeText(this, "Please select year and month", Toast.LENGTH_SHORT).show()
//            }
//            else if(pd.equals(""))
//            {
//                binding.PdET.requestFocus();
//                binding.pdError.visibility = View.VISIBLE
//                binding.pdError.text = "*Please enter PD"
//            }else if(ot.equals(""))
//            {
//                binding.otError.visibility = View.VISIBLE
//                binding.otError.text = "*Please enter OT"
//                binding.OtET.requestFocus()
//            } else {
//
//            }
        }

//        binding.OtET.addTextChangedListener(GenericTextWatcher(binding.OtET, null, binding.otError))
//        binding.PdET.addTextChangedListener(GenericTextWatcher(binding.PdET, null, binding.pdError))

        binding.prevBtn.setOnClickListener {
            callApiForDailyAttendance("previous", emp_id, "monthly")
            paginationResponseHandle()
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

    }

    fun callApi(ot: String?,
                pd: String?,
                year: String?,
                month: String?,
                employee_id: Int?){
        viewModel.add_monthly_attendance2(ot, pd,year,month,employee_id, this)
    }



    fun responseHandle()
    {
        viewModel?.attendance3Response?.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
                    if(dataSate.data.status==200)
                    {
                        progressDialog?.dismiss()
                        callApiForDailyAttendance("next", emp_id, "monthly")
                        paginationResponseHandle()
                    }else{
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
                    Toast.makeText(this, ""+dataSate.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun callApiForDailyAttendance(page: String?, employee_id: Int?,type: String?)
    {
        viewModel.attendancePaginationAttendance3(page, employee_id, type, this)
    }

    fun paginationResponseHandle()
    {
        viewModel?.paginationResponseAttendance3?.observe(this) { dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
                    if(dataSate.data.status==200)
                    {
                        progressDialog?.dismiss()
                        if(dataSate.data.data!=null)
                        {
                            empname = dataSate.data.data.name
                            designation = dataSate.data.data.designation.toString()
                            emp_id = dataSate.data.data.id
                            emp_code = dataSate.data.data.emp_code.toString()
                            if(dataSate.data.data.photo !=null)
                                photo= dataSate.data.data.photo
                            mobile = dataSate.data.data.mob_no
                            binding.OtET.setText("")
                            binding.PdET.setText("")
                            setUI()
                        }else{
                            Toast.makeText(this, "No Employee's Data Found", Toast.LENGTH_SHORT).show()
                        }
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
}