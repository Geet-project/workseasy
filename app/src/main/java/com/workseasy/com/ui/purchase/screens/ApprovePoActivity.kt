package com.workseasy.com.ui.purchase.screens

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityApprovePoBinding
import com.workseasy.com.ui.notification.adapter.NotificationAdapter
import com.workseasy.com.ui.purchase.adapter.ApprovePoAdapter
import com.workseasy.com.ui.purchase.response.ChangePoStatusDto
import com.workseasy.com.ui.purchase.viewmodel.CreatePoViewModel

class ApprovePoActivity : AppCompatActivity(), ApprovePoAdapter.DataClicked {
    var viewModel: CreatePoViewModel? = null
    lateinit var binding: ActivityApprovePoBinding
    var poListAdapter: ApprovePoAdapter? = null
    var progressBar:ProgressDialog?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        poListAdapter = ApprovePoAdapter(this, this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_approve_po)
        viewModel = ViewModelProviders.of(this).get(CreatePoViewModel::class.java)
        progressBar = ProgressDialog(this)
        apiCallForPoList()
        responseHandle()

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    fun apiCallForPoList() {
        viewModel!!.getPoList(this)
    }

    fun responseHandle() {
        viewModel!!.poListResponse.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.code == 200) {
                        binding.progressbar.visibility = View.GONE
                        binding.approvePoRecycler.visibility = View.VISIBLE
                        if (dataSate.data.data!!.size > 0) {
                            poListAdapter!!.setData(dataSate.data.data!!)
                            binding.approvePoRecycler.adapter = poListAdapter
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

    fun apiCallForChangeStatus(po_id: String) {
        viewModel!!.changePoStatus(ChangePoStatusDto(po_id), this)
    }

    override fun itemClicked(index: Int?, prid: Int?) {
        apiCallForChangeStatus(prid.toString())
        statusResponseHandle()
    }

    fun statusResponseHandle() {
        viewModel!!.changePoStatusResponse.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    progressBar!!.dismiss()
                    if (dataSate.data.code == 200) {
                        Toast.makeText(this, dataSate.data.message, Toast.LENGTH_SHORT).show()
                        apiCallForPoList()
                        responseHandle()
                    } else {
                        Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    progressBar!!.show()
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressBar!!.dismiss()
                    binding.progressbar.visibility = View.GONE
                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}