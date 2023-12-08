package com.workseasy.com.ui.purchase.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityCreateQuotationBinding
import com.hr.hrmanagement.databinding.ActivityPrpendingQuotationBinding

class PrPendingQuotationActivity : AppCompatActivity() {
    lateinit var binding: ActivityPrpendingQuotationBinding
    var pr_id: Int?=0
    var quantity: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_prpending_quotation)
        pr_id = intent.getIntExtra("pr_id",0)
        quantity = intent.getStringExtra("quantity")
        binding.createquotation.setOnClickListener {
            val intent = Intent(this, CreateQuotationActivity::class.java)
            intent.putExtra("pr_id", pr_id)
            intent.putExtra("quantity", quantity)
            startActivity(intent)
        }

        binding.prdetails.setOnClickListener {
            val intent = Intent(this, PrDetailsActivity::class.java)
            intent.putExtra("pr_id", pr_id)
            startActivity(intent)
        }
        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}