package com.workseasy.com.ui.hradmin.employeeDetails.fragments

import android.app.DatePickerDialog
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
import com.hr.hrmanagement.databinding.FragmentLeaveRequestBinding
import com.workseasy.com.ui.hradmin.employeeDetails.adapter.LeaveRequestAdapter
import com.workseasy.com.ui.hradmin.employeeDetails.viewmodel.LeaveViewModel
import com.workseasy.com.utils.GenericTextWatcher
import java.util.*


class LeaveRequestFragment : Fragment() {

    lateinit var _binding: FragmentLeaveRequestBinding
    val binding get() = _binding!!
    var dialog: AlertDialog? = null
    var viewModel: LeaveViewModel?=null
    var employee_id: Int?=null
    var progressDialog: ProgressDialog?=null
    var requestAdapter: LeaveRequestAdapter? = null



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
        _binding = DataBindingUtil.inflate(inflater,R.layout.fragment_leave_request, container, false)
        viewModel = ViewModelProviders.of(requireActivity()).get(LeaveViewModel::class.java)
        progressDialog= ProgressDialog(context)
        requestAdapter = LeaveRequestAdapter(requireContext())
        setListners()
        responseHandle()
        apiCallForLeaveList()
        allBasicWorkLayouts()
        return binding.root
    }

    fun setListners()
    {
        binding.LeaveRequestBtn.setOnClickListener {
            showDialog()
        }
    }

    fun allBasicWorkLayouts()
    {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.leaveReycler.adapter = requestAdapter
        binding.leaveReycler.layoutManager = layoutManager
        binding.leaveReycler.setLayoutManager(
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
         * @return A new instance of fragment LeaveRequestFragment.
         */
        @JvmStatic
        fun newInstance(employee_id: Int) =
            LeaveRequestFragment().apply {
                arguments = Bundle().apply {
                    putInt("empid", employee_id)
                }
            }
    }

    fun showDialog()
    {
        val mbuilder = AlertDialog.Builder(requireContext())
        val inflater1: LayoutInflater = LayoutInflater.from(activity).context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v1: View = inflater1.inflate(R.layout.leave_request_dialog, null)
        val transition_in_view = AnimationUtils.loadAnimation(
            context,
            R.anim.dialog_animation
        ) //customer animation appearance

        val leaveDateFrom: TextView = v1.findViewById(R.id.LeaveDateFrom)
        val leaveDateTo: TextView= v1.findViewById(R.id.LeaveDateTo)
        val leaveError : TextView = v1.findViewById(R.id.leaveDateError)
        val leaveReasonEt: EditText = v1.findViewById(R.id.LeaveReasonEt)
        val leaveReasonError: TextView = v1.findViewById(R.id.leaveReasonError)
        val submitBtn: Button = v1.findViewById(R.id.leaveRequestSent)
        val cancelBtnDialog : ImageButton = v1.findViewById(R.id.cancelBtnDialog)
        var leaveDateStrFrom: String?=null
        var leaveDateStrTo: String?=null
        leaveReasonEt.addTextChangedListener(GenericTextWatcher(leaveReasonEt, null, leaveReasonError))

        leaveDateFrom.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // this never ends while debugging
                    // Display Selected date in textbox
                    leaveDateStrFrom = dayOfMonth.toString() + "-" + (monthOfYear+1).toString() + "-" + year.toString()
                    leaveDateFrom.text = leaveDateStrFrom
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        leaveDateTo.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // this never ends while debugging
                    // Display Selected date in textbox
                    leaveDateStrTo = dayOfMonth.toString() + "-" + (monthOfYear+1).toString() + "-" + year.toString()
                    leaveDateTo.text = leaveDateStrTo
                },
                year,
                month,
                day
            )
            dpd.show()

            submitBtn.setOnClickListener {
                if(leaveDateStrFrom==null || leaveDateStrTo==null)
                {
                    leaveError.visibility = View.VISIBLE
                    leaveError.text = "Please enter Leave Date"
                }else if(leaveReasonEt.text.toString().equals(""))
                {
                    leaveReasonError.visibility = View.VISIBLE
                    leaveReasonError.text = "*Please Enter Leave Reason"
                }else{
                    callApiForLeaveRequestSubmit(leaveDateStrFrom, leaveDateStrTo, leaveReasonEt.text.toString(), employee_id)
                    responseLeaveSubmitHandle()
                }
            }

//            binding.prevBtn.setOnClickListener {
//                callApiForLeavePagination("previous")
//                paginationResponseHandle()
//            }
//            binding.nextBtn.setOnClickListener {
//                callApiForLeavePagination("next")
//                paginationResponseHandle()
//            }
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

    private fun callApiForLeaveRequestSubmit(
        leaveDateFrom:String?=null,
        leaveDateTo:String?=null,
        leaveReason:String?=null,
        employee_id:Int?=null
    )
    {
        viewModel!!.leaveSubmit(employee_id, leaveDateFrom, leaveDateTo, leaveReason,requireContext())
    }

    fun apiCallForLeaveList()
    {
        viewModel?.getLeaveRequest(employee_id, requireContext())
    }

    fun responseHandle()
    {

        viewModel?.leavelist?.observe(requireActivity()){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success -> {
                    if(dataSate.data.status==200)
                    {
                        binding.progressbar.visibility = View.GONE
                        if(dataSate.data.data.leaves!!.size>0)
                        {
                            requestAdapter!!.setData(dataSate.data.data.leaves)
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
//                        Toast.makeText(context, ""+dataSate.data.error, Toast.LENGTH_SHORT).show()
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

    fun responseLeaveSubmitHandle()
    {
        viewModel?.leavesubmitResponse?.observe(requireActivity()){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
                    if(dataSate.data.status==200)
                    {
                        progressDialog?.dismiss()
                        dialog?.dismiss()
                        Toast.makeText(context, "Leave Request Submitted", Toast.LENGTH_SHORT).show()
                        apiCallForLeaveList()
                        responseHandle()
                    }else{
                        progressDialog?.dismiss()
                        Toast.makeText(activity, dataSate.data.error, Toast.LENGTH_SHORT).show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    progressDialog?.setMessage("Please Wait..")
                    progressDialog?.show()
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressDialog?.dismiss()
                    Toast.makeText(activity, ""+dataSate.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



}