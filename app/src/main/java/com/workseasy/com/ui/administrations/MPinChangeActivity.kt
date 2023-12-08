package com.workseasy.com.ui.administrations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityMpinChangeBinding
import com.workseasy.com.ui.administrations.viewmodel.AdministrationViewModel
import com.workseasy.com.utils.GenericTextWatcher

class MPinChangeActivity : AppCompatActivity() {
    lateinit var binding: ActivityMpinChangeBinding
    val viewModel: AdministrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mpin_change)
        setValidationLisnters()
        setListeners()
        responseHandle()
    }

    fun setValidationLisnters()
    {
        binding.oldMpinEt.addTextChangedListener(GenericTextWatcher(binding.oldMpinEt, null, binding.oldpasswordError))
        binding.newpinEt.addTextChangedListener(GenericTextWatcher(binding.newpinEt, null, binding.newpasswordError))
        binding.cMPin.addTextChangedListener(GenericTextWatcher(binding.cMPin, binding.newpinEt, binding.cpasswordError))
    }
    fun setListeners()
    {
        binding.nextBtn.setOnClickListener {
            var oldpass = binding.oldMpinEt.text.toString()
            var newpass = binding.newpinEt.text.toString()
            val cpass = binding.cMPin.text.toString()
            if(oldpass.equals(""))
            {
                binding.oldMpinEt.requestFocus()
                binding.oldpasswordError.text = "*Please enter old pin"
                binding.oldpasswordError.visibility = View.VISIBLE
            }else if(newpass.equals(""))
            {
                binding.newpinEt.requestFocus()
                binding.newpasswordError.text = "*Please enter new pin"
                binding.newpasswordError.visibility = View.VISIBLE
            }else if(!newpass.equals(cpass))
            {
                binding.cMPin.requestFocus()
                binding.cpasswordError.text = "*Pins didn't match"
                binding.cpasswordError.visibility = View.VISIBLE
            } else{
                apiCallForChangeMPin(oldpass, newpass, cpass)
            }
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    fun apiCallForChangeMPin(oldpin: String, newpin: String, confirmpin: String)
    {
        viewModel.changePin(oldpin, newpin,confirmpin,this)
    }


    fun responseHandle()
    {
        viewModel.changepinResponse.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
                    if(dataSate.data.status==200)
                    {

                        binding.progressbar.visibility = View.GONE
                        Toast.makeText(this, "Pin Changed Successfully", Toast.LENGTH_SHORT).show()
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