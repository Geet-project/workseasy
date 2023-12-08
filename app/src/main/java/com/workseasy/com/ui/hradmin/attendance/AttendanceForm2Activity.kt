package com.workseasy.com.ui.hradmin.attendance

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityAttendanceForm2Binding
import com.squareup.picasso.Picasso
import com.workseasy.com.utils.GenericTextWatcher
import com.workseasy.com.utils.MonthYearPickerDialog
import java.util.*


class AttendanceForm2Activity : AppCompatActivity() {

    lateinit var _binding: ActivityAttendanceForm2Binding
    var emp_id: Int? = null
    var emp_code: String? = null
    var empname: String? = null
    var photo: String? = null
    var designation: String? = null
    var mobile: String? = null
    val viewModel: AttendanceViewModel by viewModels()
    var progressDialog: ProgressDialog? = null
    val binding get() = _binding!!
    var mDay: Int = 0
    var mMonth: Int = 0
    var mYear: Int = 0
    var date = Date()
    var Year: String? = null
    var Month: String? = null
    var department: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_attendance_form2)
        emp_id = intent.getIntExtra("employee_id", 0)
        emp_code = intent.getStringExtra("empCode")
        empname = intent.getStringExtra("empName")
        photo = intent.getStringExtra("photo")
        mobile = intent.getStringExtra("mobile")
        designation = intent.getStringExtra("designation")
        department = intent.getStringExtra("department")
        progressDialog = ProgressDialog(this)
        setUI()
        setListeners()
        responseHandle()
    }

    fun setUI() {
        if (photo == null || photo.equals("")) binding.empProfilePhoto.setImageResource(R.drawable.profilephoto)
        else Picasso.get().load(photo).into(binding.empProfilePhoto)
        binding.empName.text = empname
        binding.empNumber.text = mobile
        binding.empCode.text = emp_code
        binding.empDesignation.text = designation
        binding.empDepartment.text = department
        binding.wdEt.text.clear()
        binding.OtET.text.clear()
        binding.elET.text.clear()
        binding.clET.text.clear()
        binding.slET.text.clear()
        binding.HdET.text.clear()
        binding.woEt.text.clear()
        binding.PdET.text.clear()

    }


    fun setListeners() {
        binding.monthTv.setOnClickListener {
            MonthYearPickerDialog(date).apply {
                setListener { view, year, month, dayOfMonth ->
                    Month = "" + (month + 1)
                    Year = "$year"
                    binding.monthTv.text = "" + (month + 1) + "/$year"
                }
                show(supportFragmentManager, "MonthYearPickerDialog")
            }
        }

        binding.nextBtn.setOnClickListener {
            var wd: String = binding.wdEt.text.toString()
            var ot: String = binding.OtET.text.toString()
            var el: String = binding.elET.text.toString()
            var cl: String = binding.clET.text.toString()
            var sl: String = binding.slET.text.toString()
            var hd: String = binding.HdET.text.toString()
            var wo: String = binding.woEt.text.toString()
            var pd: String = binding.PdET.text.toString()

            if (Year.equals("") || wd.equals("") || ot.equals("") || el.equals("") || cl.equals("") || sl.equals(
                    ""
                ) || hd.equals("") || wo.equals("") || pd.equals("")
            ) {
                callApiForDailyAttendance("next", emp_id, "monthly")
                paginationResponseHandle()
            } else {
                callApi(wd, ot, pd, el, cl, Year, Month, sl, hd, wo, emp_id)
                responseHandle()
            }

//            if(Year.isNullOrEmpty()){
//                Toast.makeText(this, "Please select year and month", Toast.LENGTH_SHORT).show()
//            }else if(wd.equals(""))
//            {
//                binding.wdError.visibility = View.VISIBLE
//                binding.wdError.text = "*Please enter WD"
//                binding.wdEt.requestFocus()
//            }else if(ot.equals(""))
//            {
//                binding.otError.visibility = View.VISIBLE
//                binding.otError.text = "*Please enter OT"
//                binding.OtET.requestFocus()
//            }else if(el.equals(""))
//            {
//                binding.elET.requestFocus()
//                binding.elError.text = "*Please enter EL"
//                binding.elError.visibility = View.VISIBLE
//            }else if(cl.equals(""))
//            {
//                binding.clET.requestFocus();
//                binding.clError.visibility = View.VISIBLE
//                binding.clError.text = "*Please enter CL"
//            }else if(sl.equals(""))
//            {
//                binding.slET.requestFocus();
//                binding.slError.visibility = View.VISIBLE
//                binding.slError.text = "*Please enter SL"
//            }else if(hd.equals(""))
//            {
//                binding.HdET.requestFocus();
//                binding.hdError.visibility = View.VISIBLE
//                binding.hdError.text = "*Please enter HD"
//            }else if(wo.equals(""))
//            {
//                binding.woEt.requestFocus();
//                binding.woError.visibility = View.VISIBLE
//                binding.woError.text = "*Please enter WO"
//            }else{
//            }
        }

//        binding.wdEt.addTextChangedListener(GenericTextWatcher(binding.wdEt, null, binding.wdError))
//        binding.OtET.addTextChangedListener(GenericTextWatcher(binding.OtET, null, binding.otError))
//        binding.elET.addTextChangedListener(GenericTextWatcher(binding.elET, null, binding.elError))
//        binding.clET.addTextChangedListener(GenericTextWatcher(binding.clET, null, binding.clError))
//        binding.slET.addTextChangedListener(GenericTextWatcher(binding.slET, null, binding.slError))
//        binding.HdET.addTextChangedListener(GenericTextWatcher(binding.HdET, null, binding.hdError))
//        binding.woEt.addTextChangedListener(GenericTextWatcher(binding.woEt, null, binding.woError))
//

        binding.prevBtn.setOnClickListener {
            callApiForDailyAttendance("previous", emp_id, "monthly")
            paginationResponseHandle()
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

    }

    fun callApi(
        wd: String?,
        ot: String?,
        pd: String?,
        el: String?,
        cl: String?,
        year: String?,
        month: String?,
        sl: String?,
        hd: String?,
        wo: String?,
        employee_id: Int?
    ) {
        viewModel.add_monthly_attendance(
            wd, ot, pd, el, cl, year, month, sl, hd, wo, employee_id, this
        )
    }

    fun responseHandle() {
        viewModel?.attendance2Response?.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.status == 200) {
                        progressDialog?.dismiss()
                        callApiForDailyAttendance("next", emp_id, "monthly")
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


    fun callApiForDailyAttendance(page: String?, employee_id: Int?, type: String?) {
        viewModel.attendancePaginationMonthly(page, employee_id, type, this)
    }

    fun paginationResponseHandle() {
        viewModel?.paginationResponseMonthly?.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.status == 200) {
                        progressDialog?.dismiss()
                        if (dataSate.data.data != null) {
                            empname = dataSate.data.data.name
                            designation = dataSate.data.data.designation.toString()
                            emp_id = dataSate.data.data.id
                            emp_code = dataSate.data.data.emp_code.toString()
                            if (dataSate.data.data.photo != null && !dataSate.data.data.photo.isEmpty()) photo =
                                dataSate.data.data.photo
                            mobile = dataSate.data.data.mob_no
                            binding.wdEt.setText(dataSate.data.data.wd)
                            binding.OtET.setText(dataSate.data.data.ot)
                            binding.elET.setText(dataSate.data.data.el)
                            binding.clET.setText(dataSate.data.data.cl)
                            binding.slET.setText(dataSate.data.data.sl)
                            binding.HdET.setText(dataSate.data.data.hd.toString())
                            binding.woEt.setText(dataSate.data.data.wo.toString())
                            setUI()
                        } else {
                            Toast.makeText(this, "No Employee's Data Found", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
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
                    Toast.makeText(this, "" + dataSate.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}