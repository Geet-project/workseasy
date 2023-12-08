package com.workseasy.com.ui.hradmin.employeeRegistration

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityEmployeeRegistrationBinding
import com.workseasy.com.ui.hradmin.employeeDetails.adapter.ViewPagerAdapter
import com.workseasy.com.ui.hradmin.employeeDetails.fragments.EditEmpDetailsFragment
import com.workseasy.com.ui.hradmin.employeeRegistration.response.DataX
import java.text.SimpleDateFormat
import java.util.*

class EmployeeRegistrationActivity : AppCompatActivity() {
    lateinit var binding: ActivityEmployeeRegistrationBinding
    var isUniqueAdhar: Boolean = true
    var adharData : DataX? = null
    var adharnumber:String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_employee_registration)
        isUniqueAdhar = intent.getBooleanExtra("isUniqueAdhar", true)
        adharnumber = intent.getStringExtra("adharnumber")
        if(isUniqueAdhar==false) {
            adharData = intent.getParcelableExtra("data")
        }

        binding.viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(binding.tabLayout))
        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        binding.viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            //To move the indicator as the user scroll, we will need the scroll offset values
            //positionOffset is a value from [0..1] which represents how far the page has been scrolled
            //see https://developer.android.com/reference/android/support/v4/view/ViewPager.OnPageChangeListener
            override fun onPageScrolled(i: Int, positionOffset: Float, positionOffsetPx: Int) {
//                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mIndicator.getLayoutParams();
//
//                //Multiply positionOffset with indicatorWidth to get translation
//                float translationOffset =  (positionOffset+i) * indicatorWidth ;
//                params.leftMargin = (int) translationOffset;
//                mIndicator.setLayoutParams(params);
            }

            override fun onPageSelected(i: Int) {}
            override fun onPageScrollStateChanged(i: Int) {}
        })

//        setupViewPager()
    }

//    fun setupViewPager() {
//        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
//        viewPagerAdapter.addFragment(EmployeeStep1Fragment.newInstance(adharnumber!!), "Basic Details")
//        binding.viewPager.setAdapter(viewPagerAdapter);
//    }

}