package com.workseasy.com.ui.hradmin.employeeRegistration

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.loader.content.CursorLoader
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityEmployeeRegistrationStep2Binding
import com.workseasy.com.ui.hradmin.employeeRegistration.response.Asset
import com.workseasy.com.ui.hradmin.employeeRegistration.response.DistrictData
import com.workseasy.com.ui.hradmin.employeeRegistration.response.State
import com.workseasy.com.ui.hradmin.employeeRegistration.viewmodel.SignupViewModel
import com.workseasy.com.utils.SharedPref
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File

class EmployeeRegistrationStep2 : AppCompatActivity() {
    lateinit var binding: ActivityEmployeeRegistrationStep2Binding
    var employee_id: Int = 0
    val viewModel: SignupViewModel by viewModels()
    lateinit var progressDialog: ProgressDialog
    var sharedPref: SharedPref? = null
    var stateArray = mutableListOf<String>()
    var stateId = mutableListOf<Int>()
    var stateDto: List<State>? = null
    var districtData: List<DistrictData>? = null
    var districtArray = mutableListOf<String>()
    var districtId = mutableListOf<Int>()
    private val REQUEST_GALLERY_PERMISSION = 1
    private val REQUEST_CAMERA_PERMISSION = 2
    var requesCode: Int = 0
    var PassbookUri: Uri? = null
    var bitmap: Bitmap? = null
    var photobody: MultipartBody.Part? = null
    var selectedState: String? = ""
    var selectedState1: String? = ""

    var selectedStateid: String? = ""
    var selectedState1id: String? = ""

    var selectedDistrictid: String? = ""
    var selectedDistrict1id: String? = ""

    var selectedDistrict: String? = ""
    var selectedDistrict1: String? = ""

    var selectedBank: String? = null
    var selectedQualification: String? = null
    var selectedMaritalStatus: String? = null
    var assetDto: List<Asset>? = null
    var assetArray = mutableListOf<String>()
    var assetId = mutableListOf<Int>()
    var comeFrom: String? = ""
    var progressBar: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee_registration_step2)
        employee_id = intent.getIntExtra("employee_id", 0)
        comeFrom = intent.getStringExtra("comeFrom")
        progressBar = ProgressDialog(this)
        progressDialog = ProgressDialog(this)
        sharedPref = SharedPref(this)
        callApiForState(sharedPref!!.getData(SharedPref.TOKEN, "").toString())
        responseHandle()
        setListeners()
        if (comeFrom.equals("edit")) {
            getEmployeeData()
            empDataResponseHandle()
        }

        binding.checkBox2.setOnCheckedChangeListener { compoundButton, b ->
            if(b) {
                var i = binding.PermanentEt.text.toString()
                var pincode = binding.PincodeEt.text.toString()
                Log.e("per", binding.PermanentEt.text.toString())
                if(i=="") {
                    binding.checkBox2.isChecked = false
                    Toast.makeText(this, "Please enter Permanent Address First", Toast.LENGTH_SHORT).show()
                } else if(selectedState==null|| selectedState.equals("")){
                    binding.checkBox2.isChecked = false
                    Toast.makeText(this, "Please select Permanent State First", Toast.LENGTH_SHORT).show()
                } else if(selectedDistrict==null|| selectedDistrict.equals("")){
                    binding.checkBox2.isChecked = false
                    Toast.makeText(this, "Please select Permanent District First", Toast.LENGTH_SHORT).show()
                } else if(pincode==""){
                    binding.checkBox2.isChecked = false
                    Toast.makeText(this, "Please enter Permanent pincode first", Toast.LENGTH_SHORT).show()
                }else {
                    binding.PresentET.text = binding.PermanentEt.text
                    binding.PincodeEt1.text = binding.PincodeEt.text
                    var stateindex = stateArray.indexOf(selectedState)
                    binding.state1Spinner.selectItemByIndex(stateindex)
                    var distindex = districtArray.indexOf(selectedDistrict)
                    binding.districtSpinner1.selectItemByIndex(distindex)
                }
            }else if(!b) {
                binding.PresentET.setText("")
                binding.PincodeEt1.setText("")
                binding.state1Spinner.clearSelectedItem()
                binding.districtSpinner1.clearSelectedItem()
            }
        }

        binding.savebtn.setOnClickListener {
            callApiForStep2(
                employee_id.toString(),
                binding.PresentET.text.toString(),
                binding.PermanentEt.text.toString(),
                binding.PincodeEt.text.toString(),
                binding.PincodeEt1.text.toString(),
                selectedState!!,
                selectedDistrict!!,
                selectedState1!!,
                selectedDistrict1!!,
                this
            )
            responseHandle()
        }
    }

    fun setListeners() {
        binding.savebtn.setOnClickListener {
            var permanentAddresses = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                binding.PermanentEt.text.toString()
            )
            var presentAddress = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                binding.PresentET.text.toString()
            )
            var pincode = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                binding.PincodeEt.text.toString()
            )
            var emp_id =
                RequestBody.create("text/plain".toMediaTypeOrNull(), employee_id.toString())
            val statereq =
                RequestBody.create("text/plain".toMediaTypeOrNull(), selectedState.toString())
            val districtreq =
                RequestBody.create("text/plain".toMediaTypeOrNull(), selectedDistrict.toString())
            val bankreq =
                RequestBody.create("text/plain".toMediaTypeOrNull(), selectedBank.toString())
            val qualifyreq = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                selectedQualification.toString()
            )
            val maritalreq = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                selectedMaritalStatus.toString()
            )


            if (PassbookUri != null) {
                val file: File = File(getRealPathFromURI(PassbookUri!!))
                val requestFile =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                photobody = MultipartBody.Part.createFormData("passbook", file.name, requestFile)
            }

//            callApiForStep2(emp_id, presentAddress, permanentAddresses, bankreq, ifsc, accountNumber,
//            maritalreq, qualifyreq,workexp,statereq, cityreq, district,pincode, religion,photobody)
        }

        binding.stateSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            var stateid: Int = stateId.get(newIndex)
            selectedState = newText
            selectedStateid = stateid.toString()
            callApiForDistrict(stateid)
            responseHandle()
        }

        binding.state1Spinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            var stateid: Int = stateId.get(newIndex)
            selectedState1 = newText
            selectedState1id = stateid.toString()
            callApiForDistrict(stateid)
            responseHandle()

        }

        binding.districtSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            var distid: Int = districtId.get(newIndex)
            selectedDistrict=newText
            selectedDistrictid  = distid.toString()
        }

        binding.districtSpinner1.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            var distid: Int = districtId.get(newIndex)
            selectedDistrict1 = newText
            selectedDistrict1id = distid.toString()
        }


        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    fun callApiForState(token: String) {
        viewModel.getStateList(token, this)
    }

    fun callApiForDistrict(stateid: Int) {
        viewModel.getDistrictList(stateid, this)
    }

    fun callApiForStep2(
        employee_id: String,
        present_address: String,
        permanent_address: String,
        permant_pincode: String,
        present_address_pincode: String,
        permant_state: String,
        permant_district: String,
        state_id: String,
        city_id: String,
        context: Context
    ) {
        viewModel.step2Register(
            employee_id,
            present_address,
            permanent_address,
            permant_pincode,
            present_address_pincode,
            permant_state,
            permant_district,
            state_id,
            city_id,
            this
        )
    }

    fun responseHandle() {
        viewModel.stateResponse.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.status == 200) {
                        progressDialog.dismiss()
                        stateDto = dataSate.data.data.states
                        setStateSpinnerData(stateDto!!)
                    } else {
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

        viewModel.districtResponse.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.code == 200) {
                        progressDialog.dismiss()
                        districtData = dataSate.data.data
                        setDistrictSpinnerData(districtData!!)

                    } else {
                        Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
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


        viewModel.signupResponse.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.status == 200) {
                        Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show()
                        progressDialog?.dismiss()
//                        val intent = Intent(this, EmployeeRegistrationStep4::class.java)
//                        intent.putExtra("employee_id", dataSate.data.data.id)
//                        intent.putExtra("comeFrom", comeFrom)
//                        startActivity(intent)
                        finish()
                    } else {
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
                    Toast.makeText(this, "" + dataSate.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun setStateSpinnerData(statelist: List<State>) {
        stateArray.clear()
        stateId.clear()
        for (i in 0 until statelist!!.size) {
            stateArray?.add(statelist[i].name)
            stateId?.add(statelist[i].id)
        }
        binding.stateSpinner.setItems(stateArray!!.toList())
        binding.state1Spinner.setItems(stateArray!!.toList())
    }

    fun setDistrictSpinnerData(districtList: List<DistrictData>) {
        districtArray.clear()
        districtId.clear()
        for (i in 0 until districtList!!.size) {
            districtArray?.add(districtList[i].name)
            districtId?.add(districtList[i].districtid)
        }
        binding.districtSpinner.setItems(districtArray!!.toList())
        binding.districtSpinner1.setItems(districtArray!!.toList())
    }


    fun openFilePicker(code: Int) {
        val imagepicker = Intent(Intent.ACTION_PICK)
        imagepicker.type = "image/*"
        startActivityForResult(imagepicker, code)
    }

    fun getRealPathFromURI(contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(this, contentUri, proj, null, null, null)
        val cursor = loader.loadInBackground()
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result = cursor.getString(column_index)
        cursor.close()
        return result
    }


    override fun onBackPressed() {

    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val sdcard: File = Environment.getExternalStorageDirectory()
        if (sdcard != null) {
            val mediaDir = File(sdcard, "DCIM/Camera")
            if (!mediaDir.exists()) {
                mediaDir.mkdirs()
            }
        }
        val path = MediaStore.Images.Media.insertImage(
            inContext.getContentResolver(),
            inImage,
            "filename",
            null
        )
        return Uri.parse(path)
    }

    fun getEmployeeData() {
        viewModel.empData(employee_id, this)
    }

    fun empDataResponseHandle() {
        viewModel.empData.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    progressBar?.dismiss()
                    binding.PresentET.setText(dataSate.data.data.present_address.toString())
                    binding.PermanentEt.setText(dataSate.data.data.permanent_address.toString())
                    binding.PincodeEt.setText(dataSate.data.data.pincode.toString())
                }

                is com.workseasy.com.network.DataState.Loading -> {
                    progressBar?.show()
                    progressBar?.setMessage("Please Wait..")
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressBar?.dismiss()
                    Toast.makeText(this, "" + dataSate.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}

