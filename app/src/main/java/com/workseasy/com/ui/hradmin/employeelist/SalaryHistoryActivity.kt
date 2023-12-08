package com.workseasy.com.ui.hradmin.employeelist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivitySalaryHistoryBinding
import com.workseasy.com.ui.hradmin.employeeDetails.adapter.ViewSalaryAdapter
import com.workseasy.com.ui.hradmin.employeeDetails.fragments.ViewSalaryFragment
import com.workseasy.com.ui.hradmin.employeeDetails.viewmodel.ViewSalaryViewModel

class SalaryHistoryActivity : AppCompatActivity() {
    lateinit var binding: ActivitySalaryHistoryBinding
    var viewModel: ViewSalaryViewModel?=null
    var viewSalaryAdapter: ViewSalaryAdapter? = null
    var employee_id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_salary_history)
        employee_id = intent.getIntExtra("employee_id", 0)
        viewModel = ViewModelProviders.of(this).get(ViewSalaryViewModel::class.java)
        viewSalaryAdapter = ViewSalaryAdapter(this)

        binding.backBtn.setOnClickListener {
            finish()
        }

        callApiForViewSalary()
        responsehandle()
        allBasicWorkLayouts()
    }

    private fun callApiForViewSalary()
    {
        viewModel!!.viewSalaryList(employee_id,this)
    }

    fun allBasicWorkLayouts()
    {
        val layoutManager = LinearLayoutManager(this)
        binding.salaryRecyler .adapter = viewSalaryAdapter
        binding.salaryRecyler.layoutManager = layoutManager
        binding.salaryRecyler.setLayoutManager(
            LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false
            )
        )
    }



    companion object {
        @JvmStatic
        fun newInstance(employee_id: Int) =
            ViewSalaryFragment().apply {
                arguments = Bundle().apply {
                    putInt("empid", employee_id)

                }
            }
    }

    fun responsehandle()
    {
        viewModel?.salarylist?.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
                    if(dataSate.data.status==200)
                    {

                        binding.progressbar.visibility = View.GONE
                        if(dataSate.data.data.salary.size>0)
                        {
                            viewSalaryAdapter!!.setData(dataSate.data.data.salary)
                            binding.scrollView.visibility = View.VISIBLE
                            binding.noDataLayout.visibility = View.GONE
                        }else{
                            binding.scrollView.visibility = View.GONE
                            binding.noDataLayout.visibility = View.VISIBLE
                        }

                    }else{
                        binding.progressbar.visibility = View.GONE
                        Toast.makeText(this, ""+dataSate.data.error, Toast.LENGTH_SHORT).show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }

                is com.workseasy.com.network.DataState.Error -> {
                    binding.progressbar.visibility = View.GONE
                    Toast.makeText(this, ""+dataSate.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}