package com.workseasy.com.ui.hradmin.employeeDetails.fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
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
import com.hr.hrmanagement.databinding.FragmentLeaveRequestApprovalBinding
import com.workseasy.com.ui.hradmin.employeeDetails.adapter.LeaveApprovalAdapter
import com.workseasy.com.ui.hradmin.employeeDetails.viewmodel.LeaveViewModel
import com.workseasy.com.utils.GenericTextWatcher

@SuppressLint("ParcelCreator")
class LeaveRequestApprovalFragment() : Fragment() , LeaveApprovalAdapter.DataClicked, Parcelable {
    private var param1: String? = null
    private var param2: String? = null
    var employee_id: Int?=null
    lateinit var _binding: FragmentLeaveRequestApprovalBinding
    val binding get() = _binding!!
    var viewModel: LeaveViewModel?=null
    var requestAdapter: LeaveApprovalAdapter? = null
    var progressDialog: ProgressDialog?=null
    var dialog: AlertDialog? = null

    constructor(parcel: Parcel) : this() {
        param1 = parcel.readString()
        param2 = parcel.readString()
        employee_id = parcel.readValue(Int::class.java.classLoader) as? Int
    }


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
        _binding = DataBindingUtil.inflate(inflater,R.layout.fragment_leave_request_approval, container, false)
        viewModel = ViewModelProviders.of(requireActivity()).get(LeaveViewModel::class.java)
        requestAdapter = LeaveApprovalAdapter(requireContext(), this)
        progressDialog = ProgressDialog(requireContext())
        apiCallForLeaveList()
        responseHandle()
        allBasicWorkLayouts()
        return binding.root
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

    fun apiCallForLeaveList()
    {
        viewModel?.getApprovalRequestList(employee_id, requireContext())
    }

    companion object {
        @JvmStatic
        fun newInstance(employee_id: Int) =
            LeaveRequestApprovalFragment().apply {
                arguments = Bundle().apply {
                    putInt("empid", employee_id)
                }
            }
    }


    fun responseHandle()
    {
        viewModel?.approveleavelist?.observe(requireActivity()){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
                    if(dataSate.data.status==200)
                    {
                        binding.progressbar.visibility = View.GONE
                        if(dataSate.data.data.leaves.size>0)
                        {
                            binding.scrollview.visibility = View.VISIBLE
                            binding.leaveCountLayout.visibility = View.VISIBLE
                            requestAdapter!!.setData(dataSate.data.data.leaves)
                            binding.clCount.text = ""+dataSate.data.data.leaves_count.cl
                            binding.elCount.text = ""+dataSate.data.data.leaves_count.el
                            binding.slCount.text = ""+dataSate.data.data.leaves_count.sl
                            binding.noDataLayout.visibility = View.GONE
                        }else{
                            binding.scrollview.visibility = View.GONE
                            binding.leaveCountLayout.visibility = View.GONE
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

    override fun itemClicked(buttonstatus: String?, leave_id: Int?) {
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
                viewModel?.updateLeaveRequest(leave_id, buttonstatus , RemarkEt.text.toString(), requireContext())
                leaveApprovalResponseHandle()
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

    fun leaveApprovalResponseHandle()
    {
        viewModel?.updateLeaveResponse?.observe(requireActivity()){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
                    if(dataSate.data.status==200)
                    {
                        progressDialog?.dismiss()
                        dialog!!.dismiss()
                        apiCallForLeaveList()

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
                    Toast.makeText(requireContext(), ""+dataSate.exception, Toast.LENGTH_SHORT).show()
                    dialog!!.dismiss()

                }
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(param1)
        parcel.writeString(param2)
        parcel.writeValue(employee_id)
    }

    override fun describeContents(): Int {
        return 0
    }

     object CREATOR : Parcelable.Creator<LeaveRequestApprovalFragment> {
        override fun createFromParcel(parcel: Parcel): LeaveRequestApprovalFragment {
            return LeaveRequestApprovalFragment(parcel)
        }

        override fun newArray(size: Int): Array<LeaveRequestApprovalFragment?> {
            return arrayOfNulls(size)
        }
    }
}