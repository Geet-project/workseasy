package com.workseasy.com.ui.hradmin.employeeDetails.fragments

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.FragmentAdvanceRequestApprovalBinding
import com.workseasy.com.ui.hradmin.employeeDetails.adapter.ApprovalSalaryAdapter
import com.workseasy.com.ui.hradmin.employeeDetails.viewmodel.AdvanceSalaryViewModel
import com.workseasy.com.utils.GenericTextWatcher


class AdvanceRequestApprovalFragment : Fragment(), ApprovalSalaryAdapter.DataClicked {
    var employee_id: Int?=null
    lateinit var _binding: FragmentAdvanceRequestApprovalBinding
    val binding get() = _binding!!
    var viewModel: AdvanceSalaryViewModel?=null
    var approvalSalaryAdapter: ApprovalSalaryAdapter? = null
    var progressDialog: ProgressDialog?=null
    var dialog: AlertDialog? = null







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
        _binding = DataBindingUtil.inflate(inflater,R.layout.fragment_advance_request_approval, container, false)
        viewModel = ViewModelProviders.of(requireActivity()).get(AdvanceSalaryViewModel::class.java)
        approvalSalaryAdapter = ApprovalSalaryAdapter(requireContext(), this)
        progressDialog = ProgressDialog(requireContext())
        apiCallForApprovalList()
        responseHandle()
        allBasicWorkLayouts()
        return binding.root
    }

    fun apiCallForApprovalList()
    {
        viewModel?.advApprovalSalaryList(employee_id, requireContext())
    }

    companion object {
        @JvmStatic
        fun newInstance(employee_id: Int) =
            AdvanceRequestApprovalFragment().apply {
                arguments = Bundle().apply {
                    putInt("empid", employee_id)
                }
            }
    }

    fun allBasicWorkLayouts()
    {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.leaveReycler.adapter = approvalSalaryAdapter
        binding.leaveReycler.layoutManager = layoutManager
        binding.leaveReycler.setLayoutManager(
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
        )
    }

    fun responseHandle() {
        viewModel?.approvallistRespone?.observe(requireActivity()) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.status == 200) {

                        binding.progressbar.visibility = View.GONE

                        if (dataSate.data.data.advance_salaries!!.size > 0) {
                            binding.scrollview.visibility = View.VISIBLE
                            approvalSalaryAdapter!!.setData(dataSate.data.data.advance_salaries)
                            binding.noDataLayout.visibility = View.GONE
                        } else {
                            binding.scrollview.visibility = View.GONE
                            binding.noDataLayout.visibility = View.VISIBLE
                        }

                    } else {
                        binding.progressbar.visibility = View.GONE
//                        Toast.makeText(
//                            activity,
//                            "" + dataSate.data.error,
//                            Toast.LENGTH_SHORT
//                        ).show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }

                is com.workseasy.com.network.DataState.Error -> {
                    binding.progressbar.visibility = View.GONE
//                    Toast.makeText(activity, "" + dataSate.exception, Toast.LENGTH_SHORT)
//                        .show()
                }
            }
        }


    }

    override fun itemClicked(buttonstatus: String?, advance_salary_id: Int?) {
        val mbuilder = AlertDialog.Builder(requireContext())
        val inflater1: LayoutInflater = LayoutInflater.from(activity).context.getSystemService(
            Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v1: View = inflater1.inflate(R.layout.dialog_remarks, null)
        val transition_in_view = AnimationUtils.loadAnimation(
            context,
            R.anim.dialog_animation
        ) //customer animation appearance
        val RemarkEt: EditText = v1.findViewById(R.id.remarksEt)
        val remarkError: TextView = v1.findViewById(R.id.remarkError)
        val btnSent: Button = v1.findViewById(R.id.remarkBtnSent)
        val cancelBtnDialog : ImageButton = v1.findViewById(R.id.cancelBtnDialog)
        RemarkEt.addTextChangedListener(GenericTextWatcher(RemarkEt, null, remarkError))

        btnSent.setOnClickListener {
            if(RemarkEt.text.toString().equals(""))
            {
                RemarkEt.requestFocus()
                remarkError.visibility = View.VISIBLE
                remarkError.text = "*Please enter Remarks"
            }else{
                viewModel?.updateSalaryRequest(advance_salary_id, buttonstatus , RemarkEt.text.toString(), requireContext())
                salaryApprovalSubmitResponse()
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

    fun salaryApprovalSubmitResponse()
    {
        viewModel?.updatesalaryResponse?.observe(requireActivity()){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
                    if(dataSate.data.status==200)
                    {
                        progressDialog?.dismiss()
                        dialog!!.dismiss()
                        apiCallForApprovalList()
                    }else{
                        progressDialog?.dismiss()
                        dialog!!.dismiss()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    progressDialog?.show()
                    progressDialog?.setMessage("Please Wait...")
                }
                is com.workseasy.com.network.DataState.Error -> {
                    progressDialog?.dismiss()
                    Toast.makeText(activity, ""+dataSate.exception, Toast.LENGTH_SHORT).show()
                    dialog!!.dismiss()

                }
            }
        }
    }
}