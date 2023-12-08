package com.workseasy.com.ui.administrations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityChangePasswordBinding
import com.workseasy.com.ui.administrations.viewmodel.AdministrationViewModel
import com.workseasy.com.utils.GenericTextWatcher
import com.workseasy.com.utils.SharedPref

class ChangePasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityChangePasswordBinding
    val viewModel: AdministrationViewModel by viewModels()
    var sharedPref: SharedPref? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password)
        sharedPref = SharedPref(this)
        responseHandle()
        setValidationLisnters()
        setListeners()

    }
    fun setValidationLisnters()
    {
        binding.oldpassEt.addTextChangedListener(GenericTextWatcher(binding.oldpassEt, null, binding.oldpasswordError))
        binding.newpassEt.addTextChangedListener(GenericTextWatcher(binding.newpassEt, null, binding.newpasswordError))
        binding.cpasset.addTextChangedListener(GenericTextWatcher(binding.cpasset, binding.newpassEt, binding.cpasswordError))
    }
    fun setListeners()
    {
        binding.nextBtn.setOnClickListener {
            var oldpass = binding.oldpassEt.text.toString()
            var newpass = binding.newpassEt.text.toString()
            val cpass = binding.cpasset.text.toString()
            if(oldpass.equals(""))
            {
                binding.oldpassEt.requestFocus()
                binding.oldpasswordError.text = "*Please enter old password"
                binding.oldpasswordError.visibility = View.VISIBLE
            }else if(newpass.equals(""))
            {
                binding.newpassEt.requestFocus()
                binding.newpasswordError.text = "*Please enter new password"
                binding.newpasswordError.visibility = View.VISIBLE
            }else if(!newpass.equals(cpass))
            {
                binding.cpasset.requestFocus()
                binding.cpasswordError.text = "*Passwords didn't match"
                binding.cpasswordError.visibility = View.VISIBLE
            } else{
                apiCallForChangePassword(oldpass, newpass, cpass)
            }
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    fun apiCallForChangePassword(oldpass: String, newpass: String, confirmpass: String)
    {
        viewModel.changePassword(oldpass, newpass,confirmpass,this)
    }

    fun responseHandle()
    {
        viewModel.changepasswordResponse.observe(this) { dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success -> {
                    if(dataSate.data.status==200)
                    {
                        binding.progressbar.visibility = View.GONE
                        Toast.makeText(this, "Password Changed Successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        binding.progressbar.visibility = View.GONE
                        Toast.makeText(this, dataSate.data.error, Toast.LENGTH_SHORT).show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }

                is com.workseasy.com.network.DataState.Error -> {
                    binding.progressbar.visibility = View.GONE
                    Toast.makeText(this, dataSate.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}