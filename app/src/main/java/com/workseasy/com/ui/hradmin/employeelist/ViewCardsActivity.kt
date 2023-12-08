package com.workseasy.com.ui.hradmin.employeelist

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityViewCardsBinding
import com.workseasy.com.ui.hradmin.attendance.AttendanceForm1Activity
import com.workseasy.com.ui.hradmin.attendance.AttendanceForm2Activity
import com.workseasy.com.ui.hradmin.attendance.AttendanceForm3Activity
import com.workseasy.com.ui.hradmin.employeeDetails.EmployeeDetailsActivity
import com.workseasy.com.ui.hradmin.employeeRegistration.*
import com.workseasy.com.ui.hradmin.employeeRegistration.response.Department
import com.workseasy.com.ui.hradmin.employeeRegistration.response.Designation

class ViewCardsActivity : AppCompatActivity() {
    var employee_id: Int = 0
    var comeFrom: String? = ""
    var department: ArrayList<Department>? = null
    var designation: ArrayList<Designation>? = null
    var designationArray = mutableListOf<String>()
    var departmentArray = mutableListOf<String>()
    var adharnumber: String? = ""
    var progressBar: ProgressDialog? = null
    var isUniqueAdhar: Boolean = true
    var form_type: Int? = 0
    var empCode: String? = ""
    var empName: String? = ""
    var photo: String? = ""
    var mobile: String? = ""


    lateinit var binding: ActivityViewCardsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_cards)
        employee_id = intent.getIntExtra("employee_id", 0)
        comeFrom = intent.getStringExtra("comeFrom")
        adharnumber = intent.getStringExtra("adharnumber")
        form_type = intent.getIntExtra("form_type", 0)
        empCode = intent.getStringExtra("empCode")
        empName = intent.getStringExtra("empName")
        photo = intent.getStringExtra("photo")
        mobile = intent.getStringExtra("mobile")
        progressBar = ProgressDialog(this)
        isUniqueAdhar = intent.getBooleanExtra("isUniqueAdhar", true)

        binding.backBtn.setOnClickListener {
            finish()
        }


        binding.BasicDetailCard.setOnClickListener {
            if (comeFrom.equals("Attendance")) {
                if (form_type == 1) {
                    val intent = Intent(this, AttendanceForm1Activity::class.java)
                    intent.putExtra("employee_id", employee_id)
                    intent.putExtra("empCode", empCode)
                    intent.putExtra("empName", empName)
                    intent.putExtra("photo", photo)
                    intent.putExtra("mobile", mobile)
                    intent.putExtra("comeFrom", comeFrom)
                    intent.putExtra("designation", designation)
                    startActivity(intent)
                } else if (form_type == 2) {
                    val intent = Intent(this, AttendanceForm2Activity::class.java)
                    intent.putExtra("employee_id", employee_id)
                    intent.putExtra("empCode", empCode)
                    intent.putExtra("empName", empName)
                    intent.putExtra("photo", photo)
                    intent.putExtra("mobile", mobile)
                    intent.putExtra("comeFrom", comeFrom)
                    intent.putExtra("designation", designation)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, AttendanceForm3Activity::class.java)
                    intent.putExtra("employee_id", employee_id)
                    intent.putExtra("empCode", empCode)
                    intent.putExtra("empName", empName)
                    intent.putExtra("photo", photo)
                    intent.putExtra("mobile", mobile)
                    intent.putExtra("comeFrom", comeFrom)
                    intent.putExtra("designation", designation)
                    startActivity(intent)
                }
            } else {
                val intent = Intent(this, EmployeeDetailsActivity::class.java)
                intent.putExtra("employee_id", employee_id)
                intent.putExtra("empCode", empCode)
                intent.putExtra("empName", empName)
                intent.putExtra("comeFrom", comeFrom)
                startActivity(intent)
            }
        }

        binding.AddressDetailCard.setOnClickListener {
            val intent = Intent(this, EmployeeRegistrationStep2::class.java)
            intent.putExtra("employee_id", employee_id)
            startActivity(intent)
        }

        binding.SituatoryCard.setOnClickListener {
            val intent = Intent(this, EmployeeRegistrationStep4::class.java)
            intent.putExtra("employee_id", employee_id)
            intent.putExtra("comeFrom", comeFrom)
            startActivity(intent)
        }

        binding.HrDetailCard.setOnClickListener {
            val intent = Intent(this, HrDetailsActivity::class.java)
            intent.putExtra("employee_id", employee_id)
            intent.putExtra("comeFrom", comeFrom)
            startActivity(intent)
        }

        binding.WorkExpCard.setOnClickListener {
            val intent = Intent(this, WorkExperienceActivity::class.java)
            intent.putExtra("employee_id", employee_id)
            intent.putExtra("comeFrom", comeFrom)
            startActivity(intent)
        }

        binding.FamilyDetailCard.setOnClickListener {
            val intent = Intent(this, FamiliDetailActivity::class.java)
            intent.putExtra("employee_id", employee_id)
            intent.putExtra("comeFrom", comeFrom)
            startActivity(intent)
        }

        binding.SalaryHistoryCard.setOnClickListener {
            val intent = Intent(this, SalaryHistoryActivity::class.java)
            intent.putExtra("employee_id", employee_id)
            intent.putExtra("comeFrom", comeFrom)
            startActivity(intent)
        }

        binding.SalaryStructureCard.setOnClickListener {
            val intent = Intent(this, SalaryStructureActivity::class.java)
            intent.putExtra("employee_id", employee_id)
            intent.putExtra("comeFrom", comeFrom)
            startActivity(intent)
        }

//        binding.RegistrationCard.setOnClickListener {
//            val intent = Intent(this, EmployeeRegistrationStep5::class.java)
//            intent.putExtra("employee_id", employee_id)
//            intent.putExtra("comeFrom", "edit")
//            startActivity(intent)
//        }


    }
}