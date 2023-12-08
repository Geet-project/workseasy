package com.workseasy.com.ui.purchase.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityPrListingActiivtyBinding
import com.workseasy.com.ui.purchase.adapter.PrListAdapter
import com.workseasy.com.ui.purchase.response.PrData
import com.workseasy.com.ui.purchase.viewmodel.RaisePrViewModel

class PrListingActiivty : AppCompatActivity() , PrListAdapter.DataClicked {
    lateinit var _binding: ActivityPrListingActiivtyBinding
    val binding get() = _binding!!
    val viewModel: RaisePrViewModel by viewModels()
    var prListAdapter: PrListAdapter? = null
    var comeFrom: String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= DataBindingUtil.setContentView(this,R.layout.activity_pr_listing_actiivty)
        prListAdapter = PrListAdapter(this, this)
        comeFrom = intent.getStringExtra("comeFrom")

        if(comeFrom.equals("PR")){
            apiCallForPrList()
            responseHandle()

        }else if(comeFrom.equals("Quotation")) {
            apiCallForApprovedPrList("approved")
            responseHandleprapproved()
        }
        setListeners()
        allBasicWorkLayouts()
    }


    fun apiCallForPrList()
    {
        viewModel?.getPrList(this)
    }

    fun apiCallForApprovedPrList(status:String?)
    {
        viewModel?.getApprovedPrList(this, status!!)
    }

    fun setListeners()
    {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    override fun itemClicked(data: ArrayList<PrData>?, index: Int?, prid:Int?) {
        if(comeFrom.equals("PR")){
            val intent = Intent(this, PrApprovalActivity::class.java)
            intent.putExtra("pr_id", prid)
            intent.putExtra("projectname", data?.get(index!!)?.name)
            intent.putExtra("item", data?.get(index!!)?.item)
            intent.putExtra("make", data?.get(index!!)?.make)
            intent.putExtra("grade", data?.get(index!!)?.grade)
            intent.putExtra("quantity", data?.get(index!!)?.quantity)
            intent.putExtra("unit", data?.get(index!!)?.unit)
            intent.putExtra("deliveryrequired", data?.get(index!!)?.delivery)
            intent.putExtra("remark", data?.get(index!!)?.remark)

            intent.putExtra("index", index)
            startActivity(intent)
        }else if(comeFrom.equals("Quotation")) {
            val intent = Intent(this, PrPendingQuotationActivity::class.java)
            intent.putExtra("pr_id", prid)
            intent.putExtra("quantity", data?.get(index!!)?.quantity)

            startActivity(intent)
        }

    }

    fun allBasicWorkLayouts()
    {
        val layoutManager = LinearLayoutManager(this)
        binding.leaveReycler.adapter = prListAdapter
        binding.leaveReycler.layoutManager = layoutManager
        binding.leaveReycler.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
        )
    }

    fun responseHandle() {
        viewModel?.prListResponse?.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.status == 200) {
                        binding.progressbar.visibility = View.GONE
                        if (dataSate.data.data!!.size > 0) {
                            binding.scrollview.visibility = View.VISIBLE
                            prListAdapter!!.setData(dataSate.data.data)
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

    fun responseHandleprapproved() {
        viewModel?.approvedprListResponse?.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.status == 200) {
                        binding.progressbar.visibility = View.GONE
                        if (dataSate.data.data!!.size > 0) {
                            binding.scrollview.visibility = View.VISIBLE
                            prListAdapter!!.setData(dataSate.data.data)
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

}