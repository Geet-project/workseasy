package com.workseasy.com.ui.hradmin.employeeRegistration

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityEmployeeRegistrationStep4Binding
import com.workseasy.com.ui.hradmin.employeeRegistration.response.Asset
import com.workseasy.com.ui.hradmin.employeeRegistration.viewmodel.SignupViewModel

class EmployeeRegistrationStep4 : AppCompatActivity(){
    lateinit var binding: ActivityEmployeeRegistrationStep4Binding
    val viewModel: SignupViewModel by viewModels()
    var employee_id: Int= 0
    var selectedPf: String ?=null
    var selectedEsi: String ?=null
    var selectedLwf: String ?=null
    var selectedVPf: String ?=null
    var selectedPfLimit: String ?=null
    var selectedpt: String ?=null
    var selectedBonus: String ?=null
    var selectedEl: String ?=null
    var selectedCl: String ?=null
    var selectedSl: String ?=null
    var selectedtds: String ?=null
    var selectedGratia: String ?=null

    var selectedWageCalc: String?=null
    var selectedcrader: String?=null
    var selectedOt: String?=null
    var selectedElNegative: String?=null
    var selectedClNegative: String?=null
    var selectedSlNegative: String?=null
    var selectedEpfLimit: String?=null
    var selectedEpfEmpLimit: String?=null
    var selectedOtType: String?=null









    lateinit var progressDialog: ProgressDialog
    var assetDto: List<Asset>?=null
    var assetArray = mutableListOf<String>()
    var assetId = mutableListOf<Int>()
    var comeFrom: String? = ""














    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_employee_registration_step4)
        employee_id= intent.getIntExtra("employee_id", 0)
        progressDialog = ProgressDialog(this)
        comeFrom = intent.getStringExtra("comeFrom")



        setListeners()
        setSpinnerData()
        callApiForSpinnerAsset("Wage Calculation")
        callApiForSpinnerAsset("Crader")
        responseHandle()

        if(comeFrom.equals("edit"))
        {
            getEmployeeData()
            empDataResponseHandle()
        }
    }
    fun setListeners()
    {
        binding.nextBtn.setOnClickListener {
            val pfreq      = selectedPf!!.toString()
            val esireq      = selectedEsi!!.toString()
            val vpfreq      = selectedVPf!!.toString()
            val ptreq       =  selectedpt!!.toString()
            val bonusreq    = selectedBonus!!.toString()
            val elreq      = selectedEl!!.toString()
            val clreq      =  selectedCl!!.toString()
            val slreq      =  selectedSl!!.toString()
            val lwfreq      = selectedLwf!!.toString()
            val tdsreq = selectedtds!!.toString()
            val gratiareq = selectedBonus!!.toString()
            var otreq = selectedOt!!.toString()
            var wage = selectedWageCalc.toString()

            apiCallForStep4(employee_id, pfreq,vpfreq,esireq, lwfreq, tdsreq,ptreq, elreq, clreq, slreq, bonusreq,gratiareq,otreq, wage, selectedcrader!!,
                selectedOtType!!,selectedElNegative!!,selectedClNegative!!,
                selectedSlNegative!!,
                selectedEpfLimit!!, selectedEpfEmpLimit!!

            )

        }

        binding.epfSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
            if(newText.equals("Yes"))
                selectedPf = "1"
            else
                selectedPf = "0"
        }
        binding.esicSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
            if(newText.equals("Yes"))
                selectedEsi = "1"
            else
                selectedEsi = "0"
        }
        binding.lwfSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
            if(newText.equals("Yes"))
                selectedLwf = "1"
            else
                selectedLwf = "0"

        }

        binding.vpfSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
            if(newText.equals("Yes"))
                selectedVPf = "1"
            else
                selectedVPf = "0"
        }
        binding.tdsSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
            if(newText.equals("Yes"))
                selectedtds = "1"
            else
                selectedtds = "0"
        }
        binding.ptSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
            if(newText.equals("Yes"))
                selectedpt = "1"
            else
                selectedpt = "0"
        }
        binding.elSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
            if(newText.equals("Yes"))
                selectedEl = "1"
            else
                selectedEl = "0"
        }
        binding.clSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
            if(newText.equals("Yes"))
                selectedCl = "1"
            else
                selectedCl = "0"
        }
        binding.slSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
            if(newText.equals("Yes"))
                selectedSl = "1"
            else
                selectedSl = "0"
        }

        binding.bonusSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
            if(newText.equals("Yes"))
                selectedBonus = "1"
            else
                selectedBonus = "0"
        }

        binding.wageSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
            if(newText.equals("Yes"))
                selectedWageCalc = "1"
            else
                selectedWageCalc = "0"
        }

        binding.cradeSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
            if(newText.equals("Yes"))
                selectedcrader = "1"
            else
                selectedcrader = "0"
        }

        binding.exgratiaSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
            if(newText.equals("Yes"))
                selectedGratia = "1"
            else
                selectedGratia = "0"
        }

        binding.otSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
            if(newText.equals("Yes"))
                selectedOt = "1"
            else
                selectedOt = "0"
        }


        binding.otTypeSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
            if(newText.equals("Yes"))
                selectedOtType = "1"
            else
                selectedOtType = "0"
        }

        binding.elnegSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
            if(newText.equals("Yes"))
                selectedElNegative = "1"
            else
                selectedElNegative = "0"
        }

        binding.clnegativeSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
            if(newText.equals("Yes"))
                selectedClNegative = "1"
            else
                selectedClNegative = "0"
        }

        binding.slnegative.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
            if(newText.equals("Yes"))
                selectedSlNegative = "1"
            else
                selectedSlNegative = "0"
        }

        binding.epflimitSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
            if(newText.equals("Yes"))
                selectedEpfLimit = "1"
            else
                selectedEpfLimit = "0"
        }

        binding.epfempSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
            if(newText.equals("Yes"))
                selectedEpfEmpLimit = "1"
            else
                selectedEpfEmpLimit = "0"
        }




        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    fun setSpinnerData()
    {
        val myArray: Array<String> = resources.getStringArray(R.array.yesnoarray)
        val otTypeArray: Array<String> = resources.getStringArray(R.array.ottype)

        binding.vpfSpinner.setItems(myArray.toList())
        binding.otTypeSpinner.setItems(otTypeArray.toList())
        binding.vpfSpinner.selectItemByIndex(1);
        binding.esicSpinner.setItems(myArray.toList())
        binding.esicSpinner.selectItemByIndex(1);

        binding.lwfSpinner.setItems(myArray.toList())
        binding.lwfSpinner.selectItemByIndex(1);

        binding.vpfSpinner.setItems(myArray.toList())
        binding.vpfSpinner.selectItemByIndex(1);

        binding.ptSpinner.setItems(myArray.toList())
        binding.ptSpinner.selectItemByIndex(1);

        binding.bonusSpinner.setItems(myArray.toList())
        binding.bonusSpinner.selectItemByIndex(1);

        binding.clSpinner.setItems(myArray.toList())
        binding.clSpinner.selectItemByIndex(1);

        binding.elSpinner.setItems(myArray.toList())
        binding.elSpinner.selectItemByIndex(1);

        binding.slSpinner.setItems(myArray.toList())
        binding.slSpinner.selectItemByIndex(1);


        binding.epfSpinner.setItems(myArray.toList())
        binding.epfSpinner.selectItemByIndex(1);

        binding.tdsSpinner.setItems(myArray.toList())
        binding.tdsSpinner.selectItemByIndex(1);

        binding.exgratiaSpinner.setItems(myArray.toList())
        binding.exgratiaSpinner.selectItemByIndex(1);

        binding.otSpinner.setItems(myArray.toList())
        binding.otSpinner.selectItemByIndex(1);

        binding.elnegSpinner.setItems(myArray.toList())
        binding.elnegSpinner.selectItemByIndex(1);

        binding.clnegativeSpinner.setItems(myArray.toList())
        binding.clnegativeSpinner.selectItemByIndex(1);

        binding.slnegative.setItems(myArray.toList())
        binding.slnegative.selectItemByIndex(1);

        binding.epflimitSpinner.setItems(myArray.toList())
        binding.epflimitSpinner.selectItemByIndex(1);

        binding.epfempSpinner.setItems(myArray.toList())
        binding.epfempSpinner.selectItemByIndex(1);



    }

    fun apiCallForStep4(
        employee_id: Int,
        epf: String,
        vpf: String,
        esic: String,
        lwf: String,
        tds: String,
        pt: String,
        el: String,
        cl:  String,
        sl: String,
        bonus : String,
        ex_gratia: String,
        ot: String,
        wage_calc: String,
        crader: String,
        otType: String,
        elNegative:String,
        clNegative: String,
        sl_negative: String,
        epf_limit: String,
        epf_emp_limit: String,
    )
    {
        viewModel.step4Register( employee_id,
            epf,
            vpf,
            esic,
            lwf,
            tds,
            pt,
            el,
            cl,
            sl,
            bonus,
            ex_gratia,
            ot,
            wage_calc,
            crader,
            otType,
            elNegative,
            clNegative,
            sl_negative,
            epf_limit,
            epf_emp_limit,
            this)
    }

    fun responseHandle()
    {

        viewModel.spinnerResponse.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
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

        viewModel.signupResponse.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
                    if(dataSate.data.status==200)
                    {
                        progressDialog?.dismiss()
                        Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show()
//                        val intent = Intent(this, EmployeeRegistrationStep5::class.java)
//                        intent.putExtra("employee_id", dataSate.data.data.id)
//                        intent.putExtra("comeFrom", "edit")
//                        startActivity(intent)
                        finish()
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

    fun setAssetSpinner(assetList: List<Asset>, type: String)
    {
        assetArray.clear()
        assetId.clear()
        for (i in 0 until assetList!!.size) {
            assetArray?.add(assetList[i].name)
            assetId?.add(assetList[i].id)
        }

        if(type.equals("Wage Calculation"))
        {
            binding.wageSpinner.setItems(assetArray!!.toList() )
        }else if(type.equals("Crader"))
        {
            binding.cradeSpinner.setItems(assetArray!!.toList())
        }
    }

    fun callApiForSpinnerAsset(type: String)
    {
        viewModel.getAssets(type, this)
    }

    override fun onBackPressed() {
    }

    fun getEmployeeData()
    {
        viewModel.empData(employee_id, this)
    }

    fun empDataResponseHandle()
    {
        viewModel.empData.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
                    if(dataSate.data.status==200)
                    {


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
                    Toast.makeText(this,""+ dataSate.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }







}