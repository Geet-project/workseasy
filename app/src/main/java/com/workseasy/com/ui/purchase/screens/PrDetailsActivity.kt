package com.workseasy.com.ui.purchase.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityPrDetailsBinding
import com.workseasy.com.ui.purchase.viewmodel.RaisePrViewModel

class PrDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityPrDetailsBinding
    val viewModel: RaisePrViewModel by viewModels()
    var pr_id: Int?=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_pr_details)
        pr_id = intent.getIntExtra("pr_id",0)

        binding.backBtn.setOnClickListener {
            finish()
        }

        apiCallForQuotationPr(pr_id)
        responseHandle()
    }

    fun apiCallForQuotationPr(prid:Int?)
    {
        viewModel?.getPrListForQuotation(this, 8)
    }

    fun responseHandle() {
        viewModel?.quotationPrResponse?.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.status == 200) {
                        binding.progressbar.visibility = View.GONE
                        if (dataSate.data.data!!.pr_details != null) {
                            var prdetails = dataSate.data.data!!.pr_details
                            binding.scrollview.visibility = View.VISIBLE
                            binding.itemEt.setText(prdetails!!.item)
                            binding.makeEt.setText(prdetails!!.make)
                            binding.GradeEt.setText(prdetails!!.grade)
                            binding.QtyEt.setText(prdetails!!.quantity)
                            binding.UnitEt.setText(prdetails!!.unit)
                            binding.DeliveryEt.setText(prdetails!!.delivery)
                            binding.RemarkEt.setText(prdetails!!.remark)

                        } else {
                            binding.scrollview.visibility = View.GONE
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