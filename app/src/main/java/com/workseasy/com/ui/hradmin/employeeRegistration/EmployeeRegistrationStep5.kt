package com.workseasy.com.ui.hradmin.employeeRegistration

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityEmployeeRegistrationStep5Binding
import com.skydoves.powerspinner.PowerSpinnerView
import com.workseasy.com.ui.hradmin.employeeRegistration.adapter.step5Adapter
import com.workseasy.com.ui.hradmin.employeeRegistration.response.Asset
import com.workseasy.com.ui.hradmin.employeeRegistration.viewmodel.SignupViewModel
import java.util.*

class EmployeeRegistrationStep5 : AppCompatActivity() , step5Adapter.DataClicked {
    lateinit var binding: ActivityEmployeeRegistrationStep5Binding
    var dialog: AlertDialog?   =  null
    var assetDto: List<Asset>? =  null
    var assetArray = mutableListOf<String>()
    var assetId = mutableListOf<Int>()
    val viewModel: SignupViewModel by viewModels()
    lateinit var progressDialog: ProgressDialog
    var _SalaryTypeSpinner : PowerSpinnerView?=null
    var salaryArray = mutableListOf<String>()
    var salaryId = mutableListOf<Int>()
    var selectedSalaryId: Int =0
    var employee_id: Int?=null
    var selectedIndex: Int=0
    lateinit var step5Adapter: step5Adapter
    var comeFrom: String? = ""







    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_employee_registration_step5)
        progressDialog = ProgressDialog(this)
        employee_id = intent.getIntExtra("employee_id", 0)
        step5Adapter = step5Adapter(this, this)
        comeFrom = intent.getStringExtra("comeFrom")

        apiCallForStep5List(employee_id!!)
        responseHandle()
        setListeners()


    }

    fun responseHandle()
    {
        viewModel.step5ListResponse.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
                    if(dataSate.data.status==200)
                    {
                        if(dataSate.data.data.size>0)
                        {
                            progressDialog?.dismiss()
                            step5Adapter.setData(dataSate.data.data)
                            binding.recyclerView.adapter = step5Adapter
                            binding.recyclerView.visibility =View.VISIBLE

                        }else{
                            progressDialog?.dismiss()
                            binding.recyclerView.visibility =View.GONE
                            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        progressDialog?.dismiss()
                        Toast.makeText(this, dataSate.data.error, Toast.LENGTH_SHORT).show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    progressDialog?.show()
                    progressDialog?.setMessage("Please Wait..")
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressDialog?.dismiss()
                    Toast.makeText(this, ""+dataSate.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun setListeners()
    {
        binding.FabBtn.setOnClickListener {
            val mbuilder = AlertDialog.Builder(this)
            val inflater1: LayoutInflater = LayoutInflater.from(this).context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val v1: View = inflater1.inflate(R.layout.step5_dialog, null)
            val transition_in_view = AnimationUtils.loadAnimation(
                this,
                R.anim.dialog_animation
            ) //customer animation appearance

            _SalaryTypeSpinner = v1.findViewById(R.id.SalaryTypeSpinner)
            val ValueEt: EditText = v1.findViewById(R.id.ValueEt)
            val Step5Submit: Button = v1.findViewById(R.id.Step5Submit)
            val cancelBtnDialog: ImageButton = v1.findViewById(R.id.cancelBtnDialog)
            callApiForSpinnerAsset("salary_master")
            spinnerResponseHandle()
            _SalaryTypeSpinner!!.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
                Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
                selectedIndex = newIndex
            }

            Step5Submit.setOnClickListener {
                val value: String = ValueEt.text.toString()

                if(value.equals(""))
                {
                    Toast.makeText(this, "Please enter Value", Toast.LENGTH_SHORT).show()
                }else if(_SalaryTypeSpinner!!.selectedIndex==-1){
                    Toast.makeText(this, "Please select Salary", Toast.LENGTH_SHORT).show()
                }else{
                    selectedSalaryId = assetId.get(selectedIndex)
                    apiCallForStep5(value, selectedSalaryId!!)
                    insertResponseHandle()
                }
            }

            v1.animation = transition_in_view
            v1.startAnimation(transition_in_view)
            mbuilder.setView(v1)
            dialog = mbuilder.create()
            cancelBtnDialog.setOnClickListener {
                dialog!!.dismiss()
            }
            dialog!!.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog!!.setCanceledOnTouchOutside(false)
            dialog!!.show()
        }
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.nextBtn.setOnClickListener {
           finish()
        }
    }

    fun callApiForSpinnerAsset(type: String)
    {
        viewModel.getAssets(type, this)
    }
    fun apiCallForStep5(value: String, selectedId: Int)
    {
        viewModel.step5Register(employee_id!!, selectedId!!,value, this )
    }

    fun setAssetSpinner(assetList: List<Asset>, type: String)
    {
        assetArray.clear()
        assetId.clear()
        for (i in 0 until assetList!!.size) {
            assetArray?.add(assetList[i].name)
            assetId?.add(assetList[i].id)
        }


        if(type.equals("salary_master"))
        {
            _SalaryTypeSpinner?.setItems(assetArray!!.toList() )
        }

    }

    fun apiCallForStep5List(employee_id: Int)
    {
        viewModel.step5List(employee_id!!,this)
    }

    fun apiCallForStep5Delete(id: Int)
    {
        viewModel.step5delete(id, this)
    }

    override fun itemClicked(id: Int?) {
        apiCallForStep5Delete(id!!)
        deleteResponseHandle()
    }

    override fun onBackPressed() {

    }

    fun insertResponseHandle()
    {
        viewModel.signupResponse.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success -> {
                    if(dataSate.data.status==200)
                    {
                        progressDialog.dismiss()
                        dialog!!.dismiss()
                        Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show()
                        apiCallForStep5List(employee_id!!)
                        responseHandle()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this, dataSate.data.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    progressDialog?.show()
                    progressDialog?.setMessage("Please Wait..")
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressDialog?.dismiss()
                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun spinnerResponseHandle(){
        viewModel.spinnerResponse.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success -> {
                    if(dataSate.data.status==200)
                    {
                        progressDialog.dismiss()
                        assetDto = dataSate.data.data.assets
                        setAssetSpinner(assetDto!!, dataSate.data.data.type)
                    }else{
                        Toast.makeText(this, dataSate.data.error, Toast.LENGTH_SHORT).show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    progressDialog?.show()
                    progressDialog?.setMessage("Please Wait..")
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressDialog?.dismiss()
                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun deleteResponseHandle()
    {
        viewModel.step5delete.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success -> {
                    if(dataSate.data.status==200)
                    {
                        progressDialog.dismiss()
                        Toast.makeText(this, "Item Deleted", Toast.LENGTH_SHORT).show()
                        apiCallForStep5List(employee_id!!)
                        responseHandle()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this, dataSate.data.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    progressDialog?.show()
                    progressDialog?.setMessage("Please Wait..")
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressDialog?.dismiss()
                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}