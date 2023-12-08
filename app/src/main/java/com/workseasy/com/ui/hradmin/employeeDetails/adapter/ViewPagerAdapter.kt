package com.workseasy.com.ui.hradmin.employeeDetails.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    val fragmentList: MutableList<Fragment> = ArrayList()
    val fragmentTitles: MutableList<String> = ArrayList()


    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitles[position]
    }

    fun addFragment(fragment: Fragment?, name: String?) {
        fragmentList.add(fragment!!)
        fragmentTitles.add(name!!)
    }

    fun addFrag(fragment: Fragment?) {
        fragmentList.add(fragment!!)
    }

}