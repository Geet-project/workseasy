package com.workseasy.com.ui.hradmin

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityHrAdminBinding
import com.workseasy.com.ui.hradmin.employeeRegistration.EmployeeRegistrationStep1
import com.workseasy.com.ui.hradmin.employeeRegistration.response.AdharUniqueDto
import com.workseasy.com.ui.hradmin.employeelist.EmployeeListActivity
import com.workseasy.com.ui.login.LoginViewModel
import com.workseasy.com.utils.GenericTextWatcher
import com.workseasy.com.utils.SharedPref

class HrAdminActivity : AppCompatActivity() {
    lateinit var binding: ActivityHrAdminBinding
    var sharedPref: SharedPref?=null
    var leavetype: String?=null
    var salarytype: String?=null
    var attendance_type: String?=null
    var dialog: AlertDialog? = null
    val viewModel: LoginViewModel by viewModels()
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hr_admin)
        progressDialog = ProgressDialog(this)
        sharedPref = SharedPref(this);
        leavetype = sharedPref?.getData(SharedPref.LEAVE_TYPE, "0").toString()
        salarytype = sharedPref?.getData(SharedPref.SALARY_TYPE, "0").toString()
        attendance_type = sharedPref?.getData(SharedPref.ATTENDANCE_TYPE, "0").toString()
        setListners()
        hideUnhiideView()
    }

    fun hideUnhiideView()
    {
        if(leavetype.equals("1"))
        {
            binding.LeaveRequestApprovalCard.visibility = View.VISIBLE
        }else if(leavetype.equals("0"))
        {
            binding.LeaveRequestApprovalCard.visibility = View.GONE
        }

        if(attendance_type.equals("1"))
        {
            binding.attendanceCard.visibility = View.VISIBLE
        }else if(attendance_type.equals("0")) {
            binding.attendanceCard.visibility = View.GONE
        }

        if(salarytype.equals("1"))
        {
            binding.advanceSalaryApproval.visibility = View.VISIBLE
        }else if(salarytype.equals("0"))
        {
            binding.advanceSalaryApproval.visibility = View.GONE

        }
    }

    fun setListners()
    {
        binding.backBtn.setOnClickListener {
            onBackPressed()
            finish()
        }

        binding.EmployeeRegistationCard.setOnClickListener {
            val mbuilder = AlertDialog.Builder(this)
            val inflater1: LayoutInflater = LayoutInflater.from(this).context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val v1: View = inflater1.inflate(R.layout.adhar_dialog, null)
            val transition_in_view = AnimationUtils.loadAnimation(
                this,
                R.anim.dialog_animation
            ) //customer animation appearance
            val RemarkEt: EditText = v1.findViewById(R.id.adharEt)
            val remarkError: TextView = v1.findViewById(R.id.adharError)
            val btnSent: Button = v1.findViewById(R.id.remarkBtnSent)
            val cancelBtnDialog : ImageButton = v1.findViewById(R.id.cancelBtnDialog)
            RemarkEt.addTextChangedListener(GenericTextWatcher(RemarkEt, null, remarkError))
            btnSent.setOnClickListener {
                if(RemarkEt.text.toString().length<12)
                {
                    RemarkEt.requestFocus()
                    remarkError.visibility = View.VISIBLE
                    remarkError.text = "*Please enter valid aadhaar number"
                } else {
                    var adharnumber = RemarkEt.text.toString()
                    var companyid =  sharedPref!!.getData(SharedPref.COMPANYID, "0")
                    viewModel?.checkAdhar(AdharUniqueDto(adharnumber,companyid.toString().toInt()),this)
                    responseHandle(adharnumber)
                }
            }
            v1.animation = transition_in_view
            v1.startAnimation(transition_in_view)
            mbuilder.setView(v1)
            dialog = mbuilder.create()
            cancelBtnDialog.setOnClickListener {
                dialog?.dismiss()
            }
            dialog!!.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog!!.setCanceledOnTouchOutside(false)
            dialog!!.show()
        }

        binding.EmpListCard.setOnClickListener {
            val intent = Intent(this, EmployeeListActivity::class.java)
                .putExtra("comeFrom", "list")
            startActivity(intent)
        }

        binding.LeaveRequestCard.setOnClickListener {
            val intent = Intent(this, EmployeeListActivity::class.java)
                .putExtra("comeFrom", "leaveRequestEntry")
            startActivity(intent)
        }

        binding.LeaveRequestApprovalCard.setOnClickListener {
            val intent = Intent(this, EmployeeListActivity::class.java)
                .putExtra("comeFrom", "leaveRequestApproval")
            startActivity(intent)
        }

        binding.advanceSalaryRequest.setOnClickListener {
            val intent = Intent(this, EmployeeListActivity::class.java)
                .putExtra("comeFrom", "advanceSalaryRequest")
            startActivity(intent)
        }

        binding.advanceSalaryApproval.setOnClickListener {
            val intent = Intent(this, EmployeeListActivity::class.java)
                .putExtra("comeFrom", "advanceSalaryApproval")
            startActivity(intent)
        }

        binding.SalaryDetail.setOnClickListener {
            val intent = Intent(this, EmployeeListActivity::class.java)
                .putExtra("comeFrom", "SalaryDetail")
            startActivity(intent)
        }

        binding.attendanceCard.setOnClickListener {
            val intent = Intent(this, EmployeeListActivity::class.java)
                .putExtra("comeFrom", "Attendance")
            startActivity(intent)
        }
    }

    fun responseHandle(adharnumber: String)
    {
        viewModel.adharcheckResponse.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success -> {
                    progressDialog.dismiss()
                    dialog?.dismiss()
                    if(dataSate.data.isUniqueAadhar==true)
                    {
                        val intent = Intent(this, EmployeeRegistrationStep1::class.java)
                        intent.putExtra("isUniqueAdhar", true)
                        intent.putExtra("adharnumber",adharnumber)
                        startActivity(intent)
                    }else if(dataSate.data.isUniqueAadhar==false) {
                        viewModel.getAdharDetail(adharnumber, this)
                        adhardetailresponseHandle()
                    }
                    else{
                        progressDialog.dismiss()
                        Toast.makeText(this, dataSate.data.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    progressDialog.show()
                    progressDialog.setMessage("Please Wait..")
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun adhardetailresponseHandle()
    {
        viewModel.adhardetailResponse.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success -> {
                    progressDialog.dismiss()
                    dialog?.dismiss()
                    if(dataSate.data.code=="200")
                    {
                        val intent = Intent(this, EmployeeRegistrationStep1::class.java)
                        intent.putExtra("isUniqueAdhar", false)
                        intent.putExtra("data", dataSate.data.data)
                        startActivity(intent)
                    }
                    else{
                        progressDialog.dismiss()
                        Toast.makeText(this, dataSate.data.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    progressDialog.show()
                    progressDialog.setMessage("Please Wait..")
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}