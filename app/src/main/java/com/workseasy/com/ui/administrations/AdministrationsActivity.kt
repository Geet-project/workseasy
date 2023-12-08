package com.workseasy.com.ui.administrations

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityAdministrationsBinding

class AdministrationsActivity : AppCompatActivity() {
    lateinit var binding: ActivityAdministrationsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_administrations)
        setListeners()
    }
    fun setListeners()
    {
        binding.ChangePasswordCard.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        binding.MPinChangeCard.setOnClickListener {
            val intent = Intent(this, MPinChangeActivity::class.java)
            startActivity(intent)
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}