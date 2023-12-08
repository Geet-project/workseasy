package com.workseasy.com.ui.purchase.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityPurchaseProcurementBinding

class PurchaseProcurementActivity : AppCompatActivity() {
    lateinit var _binding: ActivityPurchaseProcurementBinding
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_purchase_procurement)
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.RaisePrCard.setOnClickListener {
            val intent = Intent(this, RaisePrActivity::class.java)
            startActivity(intent)
        }

        binding.GrnCard.setOnClickListener {
            val intent = Intent(this, GrnEntryActivity::class.java)
            startActivity(intent)
        }

        binding.ConsumptionCard.setOnClickListener {
            val intent = Intent(this, ConsumptionEntryActivity::class.java)
            startActivity(intent)
        }

        binding.PrApprovalCard.setOnClickListener {
            val intent = Intent(this, PrListingActiivty::class.java)
            startActivity(intent)
        }

        binding.QuotationApprovalCard.setOnClickListener {
            val intent = Intent(this, QuotationActivity::class.java)
            startActivity(intent)
        }
    }

}