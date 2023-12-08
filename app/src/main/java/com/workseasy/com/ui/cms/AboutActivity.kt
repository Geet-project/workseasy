package com.workseasy.com.ui.cms

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityAboutBinding
import com.workseasy.com.ui.administrations.viewmodel.AdministrationViewModel

class AboutActivity : AppCompatActivity() {
    lateinit var binding: ActivityAboutBinding
    val viewModel: AdministrationViewModel by viewModels()
    var page: String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about)
        page = intent.getStringExtra("page")
        apiCallForCms()
        responseHandle()
        setListeners()

    }

    fun setListeners()
    {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    fun apiCallForCms()
    {
        viewModel.cmsApi(page, this)
    }

    fun responseHandle()
    {
        viewModel.cmsResponse.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
                    if(dataSate.data.status==200)
                    {
                        binding.txtActiontitle.text = dataSate.data.data.name
                        binding.contentDesc.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Html.fromHtml(dataSate.data.data.value, Html.FROM_HTML_MODE_COMPACT)
                        } else {
                            Html.fromHtml(dataSate.data.data.value)
                        }
                        binding.progressbar.visibility = View.GONE
                        binding.scrollView.visibility = View.VISIBLE

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