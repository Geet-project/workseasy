package com.workseasy.com.ui.hradmin.employeeDetails.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.FragmentViewSalaryBinding
import com.workseasy.com.ui.hradmin.employeeDetails.adapter.ViewSalaryAdapter
import com.workseasy.com.ui.hradmin.employeeDetails.viewmodel.ViewSalaryViewModel


class ViewSalaryFragment : Fragment() {
    var employee_id: Int?=null
    lateinit var _binding: FragmentViewSalaryBinding
    val binding get() = _binding!!
    var viewModel: ViewSalaryViewModel?=null
    var viewSalaryAdapter: ViewSalaryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            employee_id = it.getInt("empid")

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater,R.layout.fragment_view_salary, container, false)
        viewModel = ViewModelProviders.of(requireActivity()).get(ViewSalaryViewModel::class.java)
        viewSalaryAdapter = ViewSalaryAdapter(requireContext())

        callApiForViewSalary()
        responsehandle()
        allBasicWorkLayouts()
        return binding.root
    }

    private fun callApiForViewSalary()
    {
        viewModel!!.viewSalaryList(employee_id,requireContext())
    }

    fun allBasicWorkLayouts()
    {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.salaryRecyler.adapter = viewSalaryAdapter
        binding.salaryRecyler.layoutManager = layoutManager
        binding.salaryRecyler.setLayoutManager(
            LinearLayoutManager(
                requireContext(),
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
        viewModel?.salarylist?.observe(requireActivity()){ dataSate->
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
//                        Toast.makeText(activity, ""+dataSate.data.error, Toast.LENGTH_SHORT).show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }

                is com.workseasy.com.network.DataState.Error -> {
                    binding.progressbar.visibility = View.GONE
//                    Toast.makeText(activity, ""+dataSate.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}