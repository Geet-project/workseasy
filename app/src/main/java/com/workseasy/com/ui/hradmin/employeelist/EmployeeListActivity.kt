package com.workseasy.com.ui.hradmin.employeelist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityEmployeeListBinding
import com.workseasy.com.ui.hradmin.attendance.AttendanceForm1Activity
import com.workseasy.com.ui.hradmin.attendance.AttendanceForm2Activity
import com.workseasy.com.ui.hradmin.attendance.AttendanceForm3Activity
import com.workseasy.com.ui.hradmin.attendance.AttendanceWithLocationActivity
import com.workseasy.com.ui.hradmin.attendance.AutoAttendanceActivity
import com.workseasy.com.ui.hradmin.employeeDetails.EmployeeDetailsActivity
import com.workseasy.com.ui.hradmin.employeelist.adapter.EmployeeAdapter
import com.workseasy.com.ui.hradmin.employeelist.viewmodel.EmployeeViewModel
import com.workseasy.com.utils.SharedPref

class EmployeeListActivity : AppCompatActivity(), EmployeeAdapter.DataClicked {
    lateinit var binding: ActivityEmployeeListBinding
    val viewModel: EmployeeViewModel by viewModels()
    var sharedPref: SharedPref? = null
    var token: String = ""
    var employeeAdapter: EmployeeAdapter? = null
    var comeFrom: String? = null
    var formType: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee_list)
        sharedPref = SharedPref(this)
        comeFrom = intent.getStringExtra("comeFrom")
        formType = intent.getIntExtra("formtype", 0)
        callApi(comeFrom!!);
        responseHandle()
        allBasicWorkLayouts()
        setListners()
    }

    fun setListners() {
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                var searchEt = binding.searchEt.text.toString()
                apiCallForEmpSearch(searchEt)
                SearchresponseHandle()
            }
        })
    }

    fun allBasicWorkLayouts() {
        employeeAdapter = EmployeeAdapter(this, this)
        val layoutManager = LinearLayoutManager(this)
        binding.empRecyler.adapter = employeeAdapter
        binding.empRecyler.layoutManager = layoutManager
        binding.empRecyler.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
        )
    }

    fun callApi(type: String) {
        viewModel.employeeList(
            sharedPref!!.getData(SharedPref.COMPANYID, "0").toString(),
            sharedPref!!.getData(SharedPref.BRANCHID, "0").toString(), this
        )
    }

    fun responseHandle() {
        viewModel.emplistResponse.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.status == 200) {
                        binding.progressbar.visibility = View.GONE
                        binding.scrollview.visibility = View.VISIBLE
                        if (dataSate.data.data!!.employees.size > 0) {
                            employeeAdapter!!.setData(dataSate.data.data!!.employees)
                            binding.scrollview.visibility = View.VISIBLE
                            binding.noDataLayout.visibility = View.GONE
                        } else {
                            binding.scrollview.visibility = View.GONE
                            binding.noDataLayout.visibility = View.VISIBLE
                        }

                    } else {
                        binding.progressbar.visibility = View.GONE
                        Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                    }
                }

                is com.workseasy.com.network.DataState.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }

                is com.workseasy.com.network.DataState.Error -> {
                    binding.progressbar.visibility = View.GONE
                    Toast.makeText(this, dataSate.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun itemClicked(
        empName: String?,
        empCode: String?,
        empId: Int?,
        form_type: Int?,
        photo: String?,
        mobile: String?,
        designation: String?,
    department: String?
    ) {
        if (comeFrom.equals("Attendance")) {
            if (formType == 1) {
                val intent = Intent(this, AttendanceForm1Activity::class.java)
                intent.putExtra("employee_id", empId)
                intent.putExtra("empCode", empCode)
                intent.putExtra("empName", empName)
                intent.putExtra("photo", photo)
                intent.putExtra("mobile", mobile)
                intent.putExtra("comeFrom", comeFrom)
                intent.putExtra("designation", designation)
                intent.putExtra("department",department )
                startActivity(intent)
            } else if (formType == 2) {
                val intent = Intent(this, AttendanceForm2Activity::class.java)
                intent.putExtra("employee_id", empId)
                intent.putExtra("empCode", empCode)
                intent.putExtra("empName", empName)
                intent.putExtra("photo", photo)
                intent.putExtra("mobile", mobile)
                intent.putExtra("comeFrom", comeFrom)
                intent.putExtra("designation", designation)
                intent.putExtra("department",department )
                startActivity(intent)
            } else if(formType==3) {
                val intent = Intent(this, AttendanceForm3Activity::class.java)
                intent.putExtra("employee_id", empId)
                intent.putExtra("empCode", empCode)
                intent.putExtra("empName", empName)
                intent.putExtra("photo", photo)
                intent.putExtra("mobile", mobile)
                intent.putExtra("comeFrom", comeFrom)
                intent.putExtra("designation", designation)
                intent.putExtra("department",department )
                startActivity(intent)
            } else if(formType==4) {
                val intent = Intent(this, AttendanceWithLocationActivity::class.java)
                intent.putExtra("employee_id", empId)
                intent.putExtra("empCode", empCode)
                intent.putExtra("empName", empName)
                intent.putExtra("photo", photo)
                intent.putExtra("mobile", mobile)
                intent.putExtra("comeFrom", comeFrom)
                intent.putExtra("designation", designation)
                intent.putExtra("department",department )
                startActivity(intent)
            }else if(formType==5) {
                val intent = Intent(this, AutoAttendanceActivity::class.java)
                intent.putExtra("employee_id", empId)
                intent.putExtra("empCode", empCode)
                intent.putExtra("empName", empName)
                intent.putExtra("photo", photo)
                intent.putExtra("mobile", mobile)
                intent.putExtra("comeFrom", comeFrom)
                intent.putExtra("designation", designation)
                intent.putExtra("department",department )
                startActivity(intent)
            }
        } else {
            val intent = Intent(this, ViewCardsActivity::class.java)
            intent.putExtra("employee_id", empId)
            intent.putExtra("empCode", empCode)
            intent.putExtra("empName", empName)
            intent.putExtra("form_type", form_type)
            intent.putExtra("photo", photo)
            intent.putExtra("mobile", mobile)
            intent.putExtra("comeFrom", comeFrom)
            intent.putExtra("designation", designation)
            intent.putExtra("department",department )
            startActivity(intent)
        }
    }

    fun apiCallForEmpSearch(search: String) {
        viewModel.empSearch(search, this)
    }

    fun SearchresponseHandle() {
        viewModel.empSearchResponse.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.status == 200) {

                        binding.progressbar.visibility = View.GONE
                        binding.scrollview.visibility = View.VISIBLE
                        if (dataSate.data.data!!.employees.size > 0) {
                            employeeAdapter!!.setData(dataSate.data.data!!.employees)
                            binding.scrollview.visibility = View.VISIBLE
                            binding.noDataLayout.visibility = View.GONE
                        } else {
                            binding.scrollview.visibility = View.GONE
                            binding.noDataLayout.visibility = View.VISIBLE
                        }

                    } else {
                        binding.progressbar.visibility = View.GONE
                        Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                    }
                }

                is com.workseasy.com.network.DataState.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }

                is com.workseasy.com.network.DataState.Error -> {
                    binding.progressbar.visibility = View.GONE
                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}