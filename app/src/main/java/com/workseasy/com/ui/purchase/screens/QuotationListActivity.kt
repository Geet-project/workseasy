package com.workseasy.com.ui.purchase.screens

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityQuotationListBinding
import com.workseasy.com.ui.hradmin.employeelist.EmployeeListActivity
import com.workseasy.com.ui.purchase.adapter.ApprovePoAdapter
import com.workseasy.com.ui.purchase.adapter.QuotationApprovalListAdapter
import com.workseasy.com.ui.purchase.adapter.QuotationNestedAdapter
import com.workseasy.com.ui.purchase.viewmodel.RaisePrViewModel

class QuotationListActivity : AppCompatActivity(),QuotationApprovalListAdapter.OnClickListener {

    lateinit var binding: ActivityQuotationListBinding
    var viewModel: RaisePrViewModel?=null
    var progressDialog: ProgressDialog?=null
    var quotationListAdapter: QuotationApprovalListAdapter ? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_quotation_list)
        viewModel = ViewModelProviders.of(this).get(RaisePrViewModel::class.java)
        quotationListAdapter = QuotationApprovalListAdapter(this, this)

        apiCallForQuotationApproval()
        responseHandle()
    }

    fun apiCallForQuotationApproval() {
        viewModel!!.getQuotationForApproval(this)
    }

    fun responseHandle() {
        viewModel!!.quotationListResponse.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.code == 200) {
                        binding.progressbar.visibility = View.GONE
                        binding.approvePoRecycler.visibility = View.VISIBLE
                        if (dataSate.data.data!!.size > 0) {
                            quotationListAdapter!!.setData(dataSate.data.data)
                            binding.approvePoRecycler.adapter = quotationListAdapter
                            binding.approvePoRecycler.visibility = View.VISIBLE
                            binding.noDataLayout.visibility = View.GONE
                        } else {
                            binding.approvePoRecycler.visibility = View.GONE
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

    override fun onClick() {
        val intent = Intent(this, QuotationActivity::class.java)
        startActivity(intent)
    }
}