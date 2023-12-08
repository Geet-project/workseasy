package com.workseasy.com.ui.hradmin.employeeDetails

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityEmployeeDetailsBinding
import com.workseasy.com.ui.hradmin.employeeDetails.adapter.ViewPagerAdapter
import com.workseasy.com.ui.hradmin.employeeDetails.fragments.*
import com.workseasy.com.ui.hradmin.employeeDetails.viewmodel.LeaveViewModel
import com.workseasy.com.ui.hradmin.employeeRegistration.EmployeeRegistrationStep1
import com.workseasy.com.utils.SharedPref

class   EmployeeDetailsActivity : AppCompatActivity() {
    lateinit var binding:ActivityEmployeeDetailsBinding
    var employee_id: Int? = 0
    var empName : String?=null
    var empCode: String?=null
    var comeFrom: String?=null
    var viewModel: LeaveViewModel?=null
    var progressDialog: ProgressDialog?=null
    var sharedPref: SharedPref?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee_details)
        viewModel = ViewModelProviders.of(this).get(LeaveViewModel::class.java)
        progressDialog= ProgressDialog(this)
        sharedPref = SharedPref(this)

        employee_id = intent.getIntExtra("employee_id",0 )
        empName = intent.getStringExtra("empName")
        empCode= intent.getStringExtra("empCode")
        comeFrom = intent.getStringExtra("comeFrom")

        binding.employeeName.text= empName
        binding.empCode.text = "Emp Code:"+empCode
        setListners()
        binding.tablayout.setupWithViewPager(binding.viewPager)
        setupViewPager(binding.viewPager, employee_id!!)

        if(comeFrom.equals("list")){
            val intent = Intent(this@EmployeeDetailsActivity, EmployeeRegistrationStep1::class.java)
            intent.putExtra("employee_id", employee_id)
            intent.putExtra("comeFrom", "edit")
            startActivity(intent)
            finish()
        }
        else if(comeFrom.equals("leaveRequestEntry"))
            binding.viewPager.setCurrentItem(0)
        else if(comeFrom.equals("leaveReque" +
                    "stApproval"))
            binding.viewPager.setCurrentItem(3)
        else if(comeFrom.equals("advanceSalaryRequest"))
            binding.viewPager.setCurrentItem(1)
        else if(comeFrom.equals("advanceSalaryApproval"))
            binding.viewPager.setCurrentItem(4)
        else if(comeFrom.equals("SalaryDetail"))
            binding.viewPager.setCurrentItem(2)

        binding.nextBtn.setOnClickListener {
            var pos = binding.viewPager!!.currentItem
            if(pos==0)
            {
                callApiForLeavePagination("next", "leave_request")
                paginationResponseHandle()
            }else if(pos==1)
            {
                callApiForLeavePagination("next", "advance_salary")
                paginationResponseHandle()
            }
        }
    }


    fun setListners()
    {
        binding.backBtn.setOnClickListener {
            finish()
        }


        binding.prevBtn.setOnClickListener {
            var pos = binding.viewPager!!.currentItem
            if(pos==0)
            {
                callApiForLeavePagination("previous", "leave_request")
                paginationResponseHandle()
            }else if(pos==1)
            {
                callApiForLeavePagination("previous", "advance_salary")
                paginationResponseHandle()
            }
        }

        binding.tablayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPager!!.currentItem = tab!!.position
                when(tab!!.position)
                {
                    0->{
                        binding.bottmLayout.visibility = View.VISIBLE
                    }
                    1->{
                        binding.bottmLayout.visibility = View.VISIBLE
                    }
                    5->{
                        val intent = Intent(this@EmployeeDetailsActivity, EmployeeRegistrationStep1::class.java)
                        intent.putExtra("employee_id", employee_id)
                        intent.putExtra("comeFrom", "edit")
                        startActivity(intent)
                        finish()
                    }
                    else ->{
                        binding.bottmLayout.visibility = View.GONE

                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }


    fun setupViewPager(viewPager: ViewPager, emp_id: Int) {
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        var leavetype = sharedPref?.getData(SharedPref.LEAVE_TYPE, "0").toString()
        var salarytype = sharedPref?.getData(SharedPref.SALARY_TYPE, "0").toString()

        if(leavetype.equals("0") && salarytype.equals("1"))
        {
            viewPagerAdapter.addFragment(LeaveRequestFragment.newInstance(emp_id), " Leave Request ")
            viewPagerAdapter.addFragment(AdvanceSalaryFragment.newInstance(emp_id), " Advance Salary ")
            viewPagerAdapter.addFragment(ViewSalaryFragment.newInstance(emp_id), " View Salary ")
            viewPagerAdapter.addFragment(AdvanceRequestApprovalFragment.newInstance(emp_id), " Advance Request Approval")
            viewPagerAdapter.addFragment(EditEmpDetailsFragment(), "Edit Details")
            viewPager.adapter = viewPagerAdapter
        }else if(leavetype.equals("0") && salarytype.equals("0"))
        {
            viewPagerAdapter.addFragment(LeaveRequestFragment.newInstance(emp_id), " Leave Request ")
            viewPagerAdapter.addFragment(AdvanceSalaryFragment.newInstance(emp_id), " Advance Salary ")
            viewPagerAdapter.addFragment(ViewSalaryFragment.newInstance(emp_id), " View Salary ")
            viewPagerAdapter.addFragment(EditEmpDetailsFragment(), "Edit Details")

            viewPager.adapter = viewPagerAdapter
        }else if(leavetype.equals("1") && salarytype.equals("1"))
        {
            viewPagerAdapter.addFragment(LeaveRequestFragment.newInstance(emp_id), " Leave Request ")
            viewPagerAdapter.addFragment(AdvanceSalaryFragment.newInstance(emp_id), " Advance Salary ")
            viewPagerAdapter.addFragment(ViewSalaryFragment.newInstance(emp_id), " View Salary ")
            viewPagerAdapter.addFragment(LeaveRequestApprovalFragment.newInstance(emp_id), " Leave Request Approval ")
            viewPagerAdapter.addFragment(AdvanceRequestApprovalFragment.newInstance(emp_id), " Advance Request Approval")
            viewPagerAdapter.addFragment(EditEmpDetailsFragment(), "Edit Details")
            viewPager.adapter = viewPagerAdapter
        }else if(leavetype.equals("1") && salarytype.equals("0"))
        {
            viewPagerAdapter.addFragment(LeaveRequestFragment.newInstance(emp_id), " Leave Request ")
            viewPagerAdapter.addFragment(AdvanceSalaryFragment.newInstance(emp_id), " Advance Salary ")
            viewPagerAdapter.addFragment(ViewSalaryFragment.newInstance(emp_id), " View Salary ")
            viewPagerAdapter.addFragment(LeaveRequestApprovalFragment.newInstance(emp_id), " Leave Request Approval ")
            viewPagerAdapter.addFragment(EditEmpDetailsFragment(), "Edit Details")
            viewPager.adapter = viewPagerAdapter
        }

    }

    private fun callApiForLeavePagination(
        page: String?,
        type: String?
    )
    {
        viewModel!!.leavePaginationList(employee_id, page, type, this)
    }

    fun paginationResponseHandle()
    {
        viewModel?.leavepaginationResponse?.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success -> {
                    if(dataSate.data.status==200)
                    {
                        if(dataSate.data.data!=null)
                        {
                            progressDialog!!.dismiss()
                            employee_id = dataSate.data.data.employee?.id
                            binding.employeeName.setText(dataSate.data.data.employee?.name.toString())
                            binding.empCode.setText(dataSate.data.data.employee?.employee_code.toString())
                            setupViewPager(binding.viewPager, employee_id!!)
                        }else{
                            progressDialog!!.dismiss()
                            Toast.makeText(this, "No Employee Found", Toast.LENGTH_SHORT).show()
                        }

                    }else{
                        progressDialog!!.dismiss()
                        Toast.makeText(this, ""+dataSate.data.error, Toast.LENGTH_SHORT).show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    progressDialog!!.setMessage("Please Wait..")
                    progressDialog!!.show()
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressDialog!!.dismiss()
                    Toast.makeText(this, ""+dataSate.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}