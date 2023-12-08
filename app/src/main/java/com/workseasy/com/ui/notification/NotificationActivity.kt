package com.workseasy.com.ui.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityNotificationBinding
import com.workseasy.com.ui.notification.adapter.NotificationAdapter

class NotificationActivity : AppCompatActivity() {
    lateinit var binding: ActivityNotificationBinding
    val viewModel: NotificationViewModel by viewModels()
    var notiAdapter: NotificationAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification)
        notiAdapter = NotificationAdapter(this)
        apiCallForNotiList()
        allBasicWorkLayouts()
        responseHandle()
        setListeners()
    }

    fun apiCallForNotiList()
    {
        viewModel.notiList(this)
    }

    fun setListeners()
    {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    fun allBasicWorkLayouts()
    {
        val layoutManager = LinearLayoutManager(this)
        binding.notiRecycler.adapter = notiAdapter
        binding.notiRecycler.layoutManager = layoutManager
        binding.notiRecycler.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
        )
    }

    fun responseHandle()
    {
        viewModel.notificationResponse.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
                    if(dataSate.data.status==200)
                    {

                        binding.progressbar.visibility = View.GONE
                        binding.notiRecycler.visibility = View.VISIBLE
                        if(dataSate.data.data!!.notifications.size>0)
                        {
                            notiAdapter!!.setData(dataSate.data.data!!.notifications)
                            binding.notiRecycler.visibility = View.VISIBLE
                            binding.noDataLayout.visibility = View.GONE
                        }else{
                            binding.notiRecycler.visibility = View.GONE
                            binding.noDataLayout.visibility = View.VISIBLE
                        }

                    }else{
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
}