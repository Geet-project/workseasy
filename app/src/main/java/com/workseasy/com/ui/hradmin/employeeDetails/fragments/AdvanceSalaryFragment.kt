package com.workseasy.com.ui.hradmin.employeeDetails.fragments

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.FragmentAdvanceSalaryBinding
import com.workseasy.com.ui.hradmin.employeeDetails.EmployeeDetailsActivity
import com.workseasy.com.ui.hradmin.employeeDetails.adapter.AdvaceSalaryAdapter
import com.workseasy.com.ui.hradmin.employeeDetails.viewmodel.AdvanceSalaryViewModel
import com.workseasy.com.utils.GenericTextWatcher
import java.util.*


class AdvanceSalaryFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    var employee_id: Int?=null
    lateinit var _binding: FragmentAdvanceSalaryBinding
    val binding get() = _binding!!
    var viewModel: AdvanceSalaryViewModel?=null
    var advAdapter: AdvaceSalaryAdapter? = null
    var dialog: AlertDialog? = null
    var progressDialog: ProgressDialog?=null
    var activity: EmployeeDetailsActivity? = null


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
        _binding = DataBindingUtil.inflate(inflater,R.layout.fragment_advance_salary, container, false)
        viewModel = ViewModelProviders.of(requireActivity()).get(AdvanceSalaryViewModel::class.java)
        progressDialog= ProgressDialog(context)
        setListners()
        apiCallForAdvList()
        allBasicWorkLayouts()
        responseHandle()

        return _binding.root

    }

    fun setListners()
    {
        binding.advRequestSent.setOnClickListener {
            showDialog()
        }
    }


    fun apiCallForAdvList()
    {
        viewModel?.getLeaveRequest(employee_id, requireContext())
    }

    fun allBasicWorkLayouts()
    {
        advAdapter = AdvaceSalaryAdapter(requireContext())
        val layoutManager = LinearLayoutManager(requireContext())
        _binding.advRecycler.adapter = advAdapter
        binding.advRecycler.layoutManager = layoutManager
        binding.advRecycler.setLayoutManager(
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AdvanceSalaryFragment.
         */
        @JvmStatic
        fun newInstance(employee_id: Int) =
            AdvanceSalaryFragment().apply {
                arguments = Bundle().apply {
                    putInt("empid", employee_id)
                }
            }
    }

    fun responseHandle()
    {
        viewModel?.advlist?.observe(requireActivity()){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
                    if(dataSate.data.status==200)
                    {

                        binding.progressbar.visibility = View.GONE
                        binding.scrollview.visibility = View.VISIBLE
                        if(dataSate.data.data.advance_salaries!!.size>0)
                        {
                            advAdapter!!.setData(dataSate.data.data.advance_salaries)
                            binding.scrollview.visibility = View.VISIBLE
                            binding.noDataLayout.visibility= View.GONE
                        }
                        else
                        {
                            binding.scrollview.visibility = View.GONE
                            binding.noDataLayout.visibility= View.VISIBLE
                        }
                    }else{
                        binding.progressbar.visibility = View.GONE
//                        Toast.makeText(context, ""+dataSate.data!!.error, Toast.LENGTH_SHORT).show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }

                is com.workseasy.com.network.DataState.Error -> {
                    binding.progressbar.visibility = View.GONE
//                    Toast.makeText(context, ""+dataSate.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun showDialog()
    {
        val mbuilder = AlertDialog.Builder(requireContext())
        val inflater1: LayoutInflater = LayoutInflater.from(requireContext()).context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v1: View = inflater1.inflate(R.layout.adv_request_dialog, null)
        val transition_in_view = AnimationUtils.loadAnimation(
            context,
            R.anim.dialog_animation
        ) //customer animation appearance

        val amtRequired: EditText = v1.findViewById(R.id.amtRequiredEt)
        val advReason: EditText = v1.findViewById(R.id.advReasonEt)
        val maxDeductionEt : EditText = v1.findViewById(R.id.MaxDeductionEt)
        val amountreqError: TextView = v1.findViewById(R.id.amountreqError)
        val advReasonError: TextView = v1.findViewById(R.id.advReasonError)
        val maxdeductionError: TextView = v1.findViewById(R.id.maxdeductionError)
        val submitBtn: Button = v1.findViewById(R.id.advRequestSent)
        val cancelBtnDialog : ImageButton = v1.findViewById(R.id.cancelBtnDialog)

        amtRequired.addTextChangedListener(GenericTextWatcher(amtRequired, null, amountreqError))
        advReason.addTextChangedListener(GenericTextWatcher(advReason, null, advReasonError))
        maxDeductionEt.addTextChangedListener(GenericTextWatcher(maxDeductionEt, null, maxdeductionError))


        submitBtn.setOnClickListener {
            if(amtRequired.text.toString().equals(""))
            {
                amountreqError.visibility = View.VISIBLE
                amountreqError.text = "*Please enter Required Amount"
            }else if(advReason.text.toString().equals(""))
            {
                advReasonError.visibility = View.VISIBLE
                advReasonError.text = "*Please enter Reason of Advance"
            }else if(maxDeductionEt.text.toString().equals(""))
            {
                maxdeductionError.visibility = View.VISIBLE
                maxdeductionError.text = "*Please enter Maximum Deduction"
            }
            else{
                callApiForAdvRequestSubmit(employee_id,amtRequired.text.toString(), advReason.text.toString(), maxDeductionEt.text.toString())
                salarySubmitResponseHandle()
            }
        }



        v1.animation = transition_in_view
        v1.startAnimation(transition_in_view)
        mbuilder.setView(v1)
        dialog = mbuilder.create()
        cancelBtnDialog.setOnClickListener {
            dialog!!.dismiss()
        }
        dialog!!.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.show()
    }

    private fun callApiForAdvRequestSubmit(
        employee_id:Int?=null,
        amount:String?=null,
        reason:String?=null,
        maximumdeduction:String?=null,
    )
    {
        viewModel!!.advSubmit(employee_id, amount, maximumdeduction, reason,requireContext())
    }

    fun salarySubmitResponseHandle()
    {
        viewModel?.advsubmitResponse?.observe(requireActivity()){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
                    if(dataSate.data.status==200)
                    {
                        progressDialog?.dismiss()
                        dialog?.dismiss()
                        Toast.makeText(context, "Advance Salary Request Submitted", Toast.LENGTH_SHORT).show()
                        apiCallForAdvList()
                        responseHandle()
                    }else{
                        progressDialog?.dismiss()
                        Toast.makeText(context, dataSate.data.error, Toast.LENGTH_SHORT).show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    progressDialog?.setMessage("Please Wait..")
                    progressDialog?.show()
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressDialog?.dismiss()
                    Toast.makeText(context, ""+dataSate.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



}