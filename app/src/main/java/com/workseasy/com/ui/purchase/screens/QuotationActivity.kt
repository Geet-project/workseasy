package com.workseasy.com.ui.purchase.screens

import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityQuotationBinding
import com.workseasy.com.ui.purchase.adapter.QuotationAdapter
import com.workseasy.com.ui.purchase.adapter.QuotationNestedAdapter
import com.workseasy.com.ui.purchase.adapter.QuotationVendorNameAdapter
import com.workseasy.com.ui.purchase.response.QuotationApprovedListData
import com.workseasy.com.ui.purchase.viewmodel.RaisePrViewModel
import com.workseasy.com.utils.GenericTextWatcher

class QuotationActivity : AppCompatActivity() {
    lateinit var binding: ActivityQuotationBinding
    var adapter: QuotationAdapter? = null
    var viewModel: RaisePrViewModel?=null
    var dialog: AlertDialog? = null
    var id : Int?=null
    var progressDialog: ProgressDialog?=null
    var nestedAdapter: QuotationNestedAdapter? = null
    var vendorNameAdapter:QuotationVendorNameAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quotation)
        viewModel = ViewModelProviders.of(this).get(RaisePrViewModel::class.java)
        adapter = QuotationAdapter(this)
        vendorNameAdapter = QuotationVendorNameAdapter(this)
        progressDialog = ProgressDialog(this)
        allBasicWorkLayouts()
        apiCallForQuotationList()
        responseHandle()

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    fun allBasicWorkLayouts()
    {

        binding.approvebtn.setOnClickListener {
            showDialog("approve")
        }

        binding.rejectbtn.setOnClickListener {
            showDialog("reject")
        }
    }

    fun apiCallForQuotationList()
    {
        viewModel?.quotationList(this)
    }

    fun apiCallForUpdateQuotation(quotationId: Int, status: String)
    {
        viewModel?.quotationUpdate(quotationId, status, this)
    }


    fun responseHandle()
    {
        viewModel?.quotationResponse?.observe(this) { dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success -> {
                    if(dataSate.data.code==200)
                    {
                        id = dataSate.data.data.get(0)?.id
                        binding.progressbar.visibility = View.GONE

                        if(dataSate.data.data.size>0)
                        {
                            binding.mainLayout.visibility = View.VISIBLE
                            adapter!!.setData(dataSate.data.data)
                            nestedAdapter!!.setData(dataSate.data.data)
                            vendorNameAdapter!!.setData(dataSate.data.data)
                            binding.quotatinRecycler.adapter = adapter
                            binding.vendorNameRecyclerView.adapter = vendorNameAdapter
                            binding.noDataLayout.visibility = View.GONE
                        }else{
                            binding.mainLayout.visibility = View.GONE
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

    fun QuotationUpdateresposeHandle()
    {
        viewModel?.quotationUpdateResponse?.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success -> {
                    if(dataSate.data.status==200)
                    {
                        progressDialog?.dismiss()
                        dialog!!.dismiss()
                        Toast.makeText(this, "Update Succesfully", Toast.LENGTH_SHORT).show()

                    } else {
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
                    Toast.makeText(this, ""+dataSate.exception, Toast.LENGTH_SHORT).show()
                    dialog!!.dismiss()
                }
            }
        }
    }

    fun showDialog(status: String)
    {
        val mbuilder = AlertDialog.Builder(this)
        val inflater1: LayoutInflater = LayoutInflater.from(this).context.getSystemService(
            Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v1: View = inflater1.inflate(R.layout.dialog_remarks, null)
        val transition_in_view = AnimationUtils.loadAnimation(
            this,
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
                viewModel?.quotationUpdate(id, status , this)
                QuotationUpdateresposeHandle()
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

}