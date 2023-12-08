package com.workseasy.com.ui

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.green
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityHomeBinding
import com.squareup.picasso.Picasso
import com.workseasy.com.ui.accounts.AccountActivity
import com.workseasy.com.ui.administrations.AdministrationsActivity
import com.workseasy.com.ui.cms.AboutActivity
import com.workseasy.com.ui.hradmin.HrAdminActivity
import com.workseasy.com.ui.hradmin.employeeDetails.adapter.ApprovalSalaryAdapter
import com.workseasy.com.ui.hradmin.employeeDetails.adapter.HomeAdapter
import com.workseasy.com.ui.hradmin.employeeRegistration.EmployeeRegistrationStep1
import com.workseasy.com.ui.hradmin.employeeRegistration.response.AdharUniqueDto
import com.workseasy.com.ui.login.LoginActivity
import com.workseasy.com.ui.login.LoginViewModel
import com.workseasy.com.ui.notification.NotificationActivity
import com.workseasy.com.ui.purchase.screens.PurchaseProcurementActivity
import com.workseasy.com.utils.SharedPref
import de.hdodenhof.circleimageview.CircleImageView

class HomeActivity : AppCompatActivity(), HomeAdapter.OnClickListener{
    lateinit var homeBinding: ActivityHomeBinding
    var sharedPref: SharedPref? = null
    var doubleTapBack = false
    val viewModel: com.workseasy.com.HomeViewModel by viewModels()
    val loginViewModel: LoginViewModel by viewModels()

    var orgDto: ArrayList<com.workseasy.com.ui.Company>? = null
    var orgArray = mutableListOf<String>()
    var orgId = mutableListOf<Int>()
    var branchIdlist = mutableListOf<Int>()

    var companyid: Int? = null
    var branchId: Int? = null

    lateinit var progressDialog: ProgressDialog
    var homeAdapter: HomeAdapter? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        sharedPref = SharedPref(this)
        progressDialog = ProgressDialog(this)
        sharedPref!!.saveData(SharedPref.COMPANYID,  0);
        homeAdapter = HomeAdapter(this, loginViewModel, this)
        setDrawer()
        drawerListner()
        setListners()
        if(homeBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            homeBinding.orgSpinner.dismiss()
        }
        apiCallForOrgList()
        orgResponseHandle()
        apiCallForDashboard()
        permissionResponseHandle()


        if (homeBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            homeBinding.orgSpinner.dismiss()
        }
    }

    fun setup() {

    }

    fun setDrawer() {
        setSupportActionBar(homeBinding.toolbar)


        val mtoggle = object: ActionBarDrawerToggle(this, homeBinding.drawerLayout,
            homeBinding.toolbar, R.string.Open,
            R.string.Close)
        {
            override fun onDrawerClosed(view: View) {
                super.onDrawerClosed(view)
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                homeBinding.orgSpinner.dismiss()
            }

        };

        homeBinding.drawerLayout.addDrawerListener(mtoggle)
        mtoggle.isDrawerIndicatorEnabled= true
        mtoggle.syncState()
        supportActionBar?.setHomeButtonEnabled(true);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setHomeAsUpIndicator(R.drawable.hamburgericon);
        homeBinding.navView.menu.getItem(1).setActionView(R.layout.arrow_layout)
        homeBinding.navView.menu.getItem(2).setActionView(R.layout.arrow_layout)
        homeBinding.navView.menu.getItem(3).setActionView(R.layout.arrow_layout)
        homeBinding.navView.menu.getItem(4).setActionView(R.layout.arrow_layout)
        homeBinding.navView.menu.getItem(5).setActionView(R.layout.arrow_layout)
        val headerLayout = homeBinding.navView.getHeaderView(0)
        val username = headerLayout.findViewById<TextView>(R.id.userNameDrawer)
        val useremail = headerLayout.findViewById<TextView>(R.id.userEmailDrawer)
        val profilephotoimg = headerLayout.findViewById<CircleImageView>(R.id.profilephotoimg)
        if (!SharedPref.IMAGE.equals("")) {
            Picasso.get().load(sharedPref!!.getData(SharedPref.IMAGE, "").toString())
                .into(profilephotoimg)
        }
        username.setText(sharedPref!!.getData(SharedPref.USER_NAME, "").toString())
        useremail.setText(sharedPref!!.getData(SharedPref.EMAIL_iD, "").toString())
    }

    fun setListners() {
        homeBinding.orgSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", "" + oldIndex + oldItem + newIndex + newText)
            companyid = orgId.get(newIndex)
            branchId = branchIdlist.get(newIndex)
            sharedPref!!.saveData(SharedPref.COMPANYID, companyid.toString()).toString()
            sharedPref!!.saveData(SharedPref.BRANCHID, branchId.toString()).toString()
            viewModel.orgUpdate(branchId.toString(), this)
            orgUpdateResponseHandle()
        }
        homeBinding.LogOutLayout.setOnClickListener {
            sharedPref!!.clearPreference()
            Toast.makeText(this, "Log Out Successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        homeBinding.notiIcon.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

    }

    fun apiCallForOrgList() {
        viewModel.orgList(this)
    }

    fun drawerListner() {
        homeBinding.navView.setNavigationItemSelectedListener { it: MenuItem ->
            when (it.itemId) {
                R.id.hrnav -> {
                    val intent = Intent(this, HrAdminActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.homenav -> {
                    homeBinding.orgSpinner.dismiss()
                    if (homeBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        homeBinding.drawerLayout.closeDrawer(GravityCompat.START)
                    }
                    true
                }
                R.id.adminnav -> {
                    if(homeBinding.orgSpinner.isShowing){
                        homeBinding.drawerLayout.closeDrawers()
                    }
                    val intent = Intent(this, AdministrationsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.aboutnav -> {
                    if(homeBinding.orgSpinner.isShowing){
                        homeBinding.drawerLayout.closeDrawers()
                    }
                    val intent = Intent(this, AboutActivity::class.java)
                    intent.putExtra("page", "about_company")
                    startActivity(intent)
                    true
                }

                R.id.termsnav -> {
                    if(homeBinding.orgSpinner.isShowing){
                        homeBinding.drawerLayout.closeDrawers()
                    }
                    val intent = Intent(this, AboutActivity::class.java)
                    intent.putExtra("page", "terms_and_conditions")
                    startActivity(intent)
                    true
                }
                R.id.privacynav -> {
                    if(homeBinding.orgSpinner.isShowing){
                        homeBinding.drawerLayout.closeDrawers()
                    }
                    val intent = Intent(this, AboutActivity::class.java)
                    intent.putExtra("page", "privacy_policy")
                    startActivity(intent)
                    true
                }
                R.id.purchasenav -> {
                    if(homeBinding.orgSpinner.isShowing){
                        homeBinding.drawerLayout.closeDrawers()
                    }
                    val intent = Intent(this, PurchaseProcurementActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.accountsnav -> {
                    if(homeBinding.orgSpinner.isShowing){
                        homeBinding.drawerLayout.closeDrawers()
                    }
                    val intent = Intent(this, AccountActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.reportsnav -> {
                    if(homeBinding.orgSpinner.isShowing){
                        homeBinding.drawerLayout.closeDrawers()
                    }
                    val browserIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("http://erp.workseasy.in/"))
                    startActivity(browserIntent)
                    true
                }
                else -> {
                    true
                }
            }
        }
    }

    override fun onBackPressed() {
        if (doubleTapBack) {
            super.onBackPressed()
            finish()
        } else {
            Toast.makeText(this@HomeActivity, "Press again to exit", Toast.LENGTH_SHORT).show()
            this.doubleTapBack = true
            Handler().postDelayed({ doubleTapBack = false }, 2000)
        }
    }

    fun orgResponseHandle() {
        viewModel?.homeResponse?.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    progressDialog!!.dismiss()
                    if (dataSate.data.status == 200) {
                        if (dataSate.data.companies.size > 0) {
                            orgDto = dataSate.data.companies
                            orgSetSpinner(orgDto!!, "grade")
                        }
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    progressDialog?.show()
                    progressDialog?.setMessage("Please Wait..")
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressDialog?.dismiss()
                    Toast.makeText(this, "" + dataSate.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun orgUpdateResponseHandle() {
        viewModel?.orgResponse?.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    progressDialog!!.dismiss()
                    if (dataSate.data.status == 200) {
                    } else {
                        Toast.makeText(this, dataSate.data.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    progressDialog?.show()
                    progressDialog?.setMessage("Please Wait..")
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressDialog?.dismiss()
                    Toast.makeText(this, "" + dataSate.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun orgSetSpinner(orgList: List<com.workseasy.com.ui.Company>, type: String) {
        orgArray.clear()
        orgId.clear()
        for (i in 0 until orgList!!.size) {
            orgArray?.add(orgList[i].company_name)
            orgId?.add(orgList[i].company_id)
            branchIdlist?.add(orgList[i].branch_id)
        }
        homeBinding.orgSpinner?.setItems(orgArray!!.toList())
        homeBinding.orgSpinner.selectItemByIndex(0)
        companyid = orgId.get(0)
        sharedPref!!.saveData(SharedPref.COMPANYID, companyid.toString())
    }

    fun apiCallForDashboard()  {
        viewModel.getPermissions(sharedPref!!.getData(SharedPref.USER_ID, 0).toString(), this)
    }

    fun permissionResponseHandle() {
        viewModel?.permissionResponse?.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    progressDialog!!.dismiss()
                    if (dataSate.data.data!!.size > 0) {
                        homeAdapter!!.setData(dataSate.data.data)
                        allBasicWorkLayouts()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    progressDialog?.show()
                    progressDialog?.setMessage("Please Wait..")
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressDialog?.dismiss()
                    Toast.makeText(this, "" + dataSate.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun allBasicWorkLayouts()
    {
        val layoutManager = LinearLayoutManager(this)
        homeBinding.homeRecycler.adapter = homeAdapter
        homeBinding.homeRecycler.layoutManager = layoutManager
        homeBinding.homeRecycler.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
        )
    }

    fun responseHandle(adharnumber: String,dialog: AlertDialog)
    {
        loginViewModel.adharcheckResponse.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success -> {
                    progressDialog.dismiss()
                    dialog?.dismiss()
                    if(dataSate.data.isUniqueAadhar==true)
                    {
                        val intent = Intent(this, EmployeeRegistrationStep1::class.java)
                        intent.putExtra("isUniqueAdhar", true)
                        intent.putExtra("adharnumber",adharnumber)
                        startActivity(intent)
                    }else if(dataSate.data.isUniqueAadhar==false) {
                        loginViewModel.getAdharDetail(adharnumber, this)
                        adhardetailresponseHandle(dialog)
                    }
                    else{
                        progressDialog.dismiss()
                        Toast.makeText(this, dataSate.data.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    progressDialog.show()
                    progressDialog.setMessage("Please Wait..")
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun adhardetailresponseHandle(dialog: AlertDialog)
    {
        loginViewModel.adhardetailResponse.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success -> {
                    progressDialog.dismiss()
                    dialog?.dismiss()
                    if(dataSate.data.code=="200")
                    {
                        val intent = Intent(this, EmployeeRegistrationStep1::class.java)
                        intent.putExtra("isUniqueAdhar", false)
                        intent.putExtra("data", dataSate.data.data)
                        startActivity(intent)
                    }
                    else{
                        progressDialog.dismiss()
                        Toast.makeText(this, dataSate.data.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    progressDialog.show()
                    progressDialog.setMessage("Please Wait..")
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onClick(adharnumber: String, dialog: AlertDialog) {
        responseHandle(adharnumber, dialog)
    }


}



