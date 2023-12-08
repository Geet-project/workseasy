package com.workseasy.com.ui.hradmin.employeeRegistration

import android.Manifest
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.loader.content.CursorLoader
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityEmployeeStep1Binding
import com.squareup.picasso.Picasso
import com.workseasy.com.ui.hradmin.employeeRegistration.response.Asset
import com.workseasy.com.ui.hradmin.employeeRegistration.response.DataX
import com.workseasy.com.ui.hradmin.employeeRegistration.response.Department
import com.workseasy.com.ui.hradmin.employeeRegistration.response.Designation
import com.workseasy.com.ui.hradmin.employeeRegistration.viewmodel.SignupViewModel
import com.workseasy.com.utils.GenericTextWatcher
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


class EmployeeRegistrationStep1 : AppCompatActivity() {
    lateinit var binding: ActivityEmployeeStep1Binding
    var isHusband: Int = 0
    private val REQUEST_GALLERY_PERMISSION = 1
    private val REQUEST_CAMERA_PERMISSION = 2
    var PhotoPathUri: Uri? = null
    var AdharFrontUri: Uri? = null
    var AdharBackUri: Uri? = null
    var PanUri: Uri? = null
    var bitmap: Bitmap? = null
    var requesCode: Int = 0
    val viewModel: SignupViewModel by viewModels()
    var dobstr: String = ""
    var dojstr: String = ""
    var photobody: MultipartBody.Part? = null
    var adharfrontbody: MultipartBody.Part? = null
    var adharbackbody: MultipartBody.Part? = null
    var panbody: MultipartBody.Part? = null
    var gender: String? = ""
    var progressBar: ProgressDialog? = null
    var pdfUri: Uri? = null
    var encodedPdf: String? = ""
    var age: Int = 0
    var employee_id: Int = 0
    var comeFrom: String? = ""
    var department: ArrayList<Department>? = null
    var designation: ArrayList<Designation>? = null
    var designationArray = mutableListOf<String>()
    var departmentArray = mutableListOf<String>()
    var selectedDesignation: String? = ""
    var selectedDepartment: String? = ""
    var adharnumber: String? = ""
    var isUniqueAdhar: Boolean = true
    var adharData: DataX? = null
    var genderData: String? = ""
    var adharfrontphoto: String? = ""
    var adharbackphoto: String? = ""
    var photo: String? = ""
    var pan_front_photo: String? = ""
    var selectedReligion: String? = ""
    var selectedQualification: String? = ""
    var assetDto: List<Asset>? = null
    lateinit var progressDialog: ProgressDialog
    var religionArray = mutableListOf<String>()
    var qualificationArray = mutableListOf<String>()
    var assetId = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee_step1)
        employee_id = intent.getIntExtra("employee_id", 0)
        comeFrom = intent.getStringExtra("comeFrom")
        adharnumber = intent.getStringExtra("adharnumber")
        progressBar = ProgressDialog(this)
        isUniqueAdhar = intent.getBooleanExtra("isUniqueAdhar", true)
        progressDialog = ProgressDialog(this)
        callApiForMasterData()
        setListners()
        setValidationListners()
        setGenderSpinnerData()
        callApiForSpinnerAsset("Qualification")
        callApiForSpinnerAsset("Religion")

        assetSpinnerResponse()

        if (comeFrom != null && comeFrom.equals("edit")) {
            binding.payCodeLayout.visibility = View.GONE;
            binding.religionLayout.visibility = View.VISIBLE
            getEmployeeData()
            empDataResponseHandle()
        }
        if (isUniqueAdhar == true)
            binding.AdhaarEt.setText(adharnumber)
        else if (isUniqueAdhar == false) {
            adharData = intent.getParcelableExtra("data")
            binding.NameEt.setText(adharData?.name)
            binding.FatherNameET.setText(adharData?.fatherName)
            binding.AdhaarEt.setText(adharData?.aadharNumber)
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                simpleDateFormat.parse(adharData?.dateOfBirth)!!
            )
            val dojdate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                simpleDateFormat.parse(adharData?.dateOfJoining)!!
            )
            binding.Dobet.setText(date)
            binding.Dojet.setText(dojdate)
            dobstr = date
            dojstr = dojdate
            binding.EmailEt.setText(adharData?.email)
            binding.PanEt.setText(adharData?.panNumber)
            binding.PhoneEt.setText(adharData?.mobNo)
            binding.etAge.setText(adharData?.age)
            binding.PayCodeET.setText(adharData?.payCode)
            val genderArray: Array<String> = resources.getStringArray(R.array.genderArray)
            gender = adharData?.gender
            val index = genderArray.indexOf(gender)
            binding.genderSpinner.selectItemByIndex(index)
            adharfrontphoto = adharData?.aadhar_front_photo
            adharbackphoto = adharData?.aadhar_back_photo
            photo = adharData?.photo
            pan_front_photo = adharData?.pan_front_photo

            if (adharData?.photo != null && adharData?.photo!!.isNotEmpty()) {
                Picasso.get().load(adharData?.photo).into(binding.Photo1)
                binding.PhotoLayout.visibility = View.VISIBLE
            }
            if (adharData?.aadhar_front_photo != null && adharData?.aadhar_front_photo!!.isNotEmpty()) {
                Picasso.get().load(adharData?.aadhar_front_photo).into(binding.AdharFronPhoto)
                binding.AdharFrontLayout.visibility = View.VISIBLE

            }

            if (adharData?.aadhar_back_photo != null && adharData?.aadhar_back_photo!!.isNotEmpty()) {
                Picasso.get().load(adharData?.aadhar_back_photo).into(binding.AdharBackImg)
                binding.AdharBackLayout.visibility = View.VISIBLE

            }

            if (adharData?.pan_front_photo != null && adharData?.pan_front_photo!!.isNotEmpty()) {
                Picasso.get().load(adharData?.panNumber).into(binding.PanImg)
                binding.PanCardLayout.visibility = View.VISIBLE
            }
        }

    }


    fun setValidationListners() {
        binding.NameEt.addTextChangedListener(
            GenericTextWatcher(
                binding.NameEt,
                null,
                binding.nameError
            )
        )
        binding.FatherNameET.addTextChangedListener(
            GenericTextWatcher(
                binding.FatherNameET,
                null,
                binding.fthernameError
            )
        )
        binding.etAge.addTextChangedListener(
            GenericTextWatcher(
                binding.etAge,
                null,
                binding.ageError
            )
        )
        binding.PhoneEt.addTextChangedListener(
            GenericTextWatcher(
                binding.PhoneEt,
                null,
                binding.mobileError
            )
        )
        binding.PanEt.addTextChangedListener(
            GenericTextWatcher(
                binding.PanEt,
                null,
                binding.panError
            )
        )

        binding.desigSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", "" + oldIndex + oldItem + newIndex + newText)
            selectedDesignation = newText
        }

        binding.departSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", "" + oldIndex + oldItem + newIndex + newText)
            selectedDepartment = newText

        }

        binding.ifscEt.addTextChangedListener(
            GenericTextWatcher(
                binding.ifscEt,
                null,
                binding.ifscerror
            )
        )

        binding.ifscEt.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (!GenericTextWatcher.isIFSCValid(binding.ifscEt.text.toString())) {
                    binding.ifscerror.visibility = View.VISIBLE
                    binding.ifscerror.setText("*Please enter valid ifsc code.")
                } else {
                    apicallForBankName(binding.ifscEt.text.toString(), this)
                    responseHandleForBankName()
                }
                true
            } else false
        }
        )
    }


    fun setListners() {
        binding.savebtn.setOnClickListener {
            var name = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                binding.NameEt.text.toString()
            )
            var fathername = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                binding.FatherNameET.text.toString()
            )
            var age =
                RequestBody.create("text/plain".toMediaTypeOrNull(), binding.etAge.text.toString())
            var email = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                binding.EmailEt.text.toString()
            )
            var mobile = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                binding.PhoneEt.text.toString()
            )
            var adharnumber = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                binding.AdhaarEt.text.toString()
            )
            var pannumber =
                RequestBody.create("text/plain".toMediaTypeOrNull(), binding.PanEt.text.toString())
            var husband = RequestBody.create("text/plain".toMediaTypeOrNull(), isHusband.toString())
            var company_id = RequestBody.create("text/plain".toMediaTypeOrNull(), "1")
            val dobreq = RequestBody.create("text/plain".toMediaTypeOrNull(), dobstr)
            val dojreq = RequestBody.create("text/plain".toMediaTypeOrNull(), dojstr)
            val banknamereq = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                binding.bankNameEt.text.toString()
            )
            val bankaccountno = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                binding.BankAccountEt.text.toString()
            )
            val ifscreq =
                RequestBody.create("text/plain".toMediaTypeOrNull(), binding.ifscEt.text.toString())


            val genderreq = RequestBody.create("text/plain".toMediaTypeOrNull(), gender!!)
            val pdftosend =
                RequestBody.create("text/plain".toMediaTypeOrNull(), encodedPdf.toString())
            val emp_id =
                RequestBody.create("text/plain".toMediaTypeOrNull(), employee_id.toString())
            val paycode = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                binding.PayCodeET.text.toString()
            )

            val deptreq = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                selectedDepartment!!
            )

            val desigreq = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                selectedDesignation!!
            )
            if (PhotoPathUri != null) {
                val file: File = File(getRealPathFromURI(PhotoPathUri!!))
                val requestFile =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                photobody = MultipartBody.Part.createFormData("photo", file.name, requestFile)
            }
            if (AdharFrontUri != null) {
                val file: File = File(getRealPathFromURI(PhotoPathUri!!))
                val requestFile =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                adharfrontbody =
                    MultipartBody.Part.createFormData("aadhar_front_photo", file.name, requestFile)
            }
            if (AdharBackUri != null) {
                val file: File = File(getRealPathFromURI(PhotoPathUri!!))
                val requestFile =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                adharbackbody =
                    MultipartBody.Part.createFormData("aadhar_back_photo", file.name, requestFile)
            }
            if (PanUri != null) {
                val file: File = File(getRealPathFromURI(PhotoPathUri!!))
                val requestFile =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                panbody =
                    MultipartBody.Part.createFormData("pan_front_photo", file.name, requestFile)
            }

            if (!comeFrom.equals("edit")) {
                if (binding.NameEt.text.toString() == "") {
                    Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show()
                } else if (binding.FatherNameET.text.toString() == "") {
                    Toast.makeText(this, "Please enter father name", Toast.LENGTH_SHORT).show()
                } else if (dobstr == "") {
                    Toast.makeText(this, "Please enter date of birth", Toast.LENGTH_SHORT).show()
                } else if (gender == "") {
                    Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show()
                } else if (selectedDepartment == "") {
                    Toast.makeText(this, "Please select department", Toast.LENGTH_SHORT).show()
                } else if (selectedDesignation == "") {
                    Toast.makeText(this, "Please select designation", Toast.LENGTH_SHORT).show()
                } else if (binding.PhoneEt.text.toString() == "") {
                    Toast.makeText(this, "Please enter mobile number", Toast.LENGTH_SHORT).show()
                } else if (!com.workseasy.com.utils.Patterns.PHONEUMBER.matcher(binding.PhoneEt.text)
                        .matches()
                ) {
                    Toast.makeText(this, "Please enter valid nmobile number", Toast.LENGTH_SHORT)
                        .show()
                } else if (binding.PhoneEt.text.toString() == "") {
                    Toast.makeText(this, "Please enter mobile number", Toast.LENGTH_SHORT).show()
                } else if (!GenericTextWatcher.isIFSCValid(binding.ifscEt.text.toString())) {
                    Toast.makeText(this, "Please enter valid ifsc code", Toast.LENGTH_SHORT).show()
                } else {
                    callApi(
                        company_id,
                        name,
                        fathername,
                        photobody,
                        dobreq,
                        email,
                        mobile,
                        adharnumber,
                        pannumber,
                        genderreq,
                        adharfrontbody,
                        adharbackbody,
                        panbody,
                        husband,
                        age,
                        paycode,
                        deptreq,
                        desigreq,
                        pdftosend,
                        dojreq,
                        banknamereq,
                        bankaccountno,
                        ifscreq
                    )
                    responseHandle()
                }

            } else {
                callApiForEdit(
                    employee_id,
                    binding.NameEt.text.toString(),
                    binding.FatherNameET.text.toString(),
                    dobstr,
                    binding.EmailEt.text.toString(),
                    binding.PhoneEt.text.toString(),
                    binding.AdhaarEt.text.toString(),
                    binding.PanEt.text.toString(),
                    gender!!,
                    isHusband.toString(),
                    binding.etAge.text.toString(),
                    binding.PayCodeET.text.toString(),
                    selectedDepartment!!,
                    selectedDesignation!!,
                    selectedReligion!!,
                    selectedQualification!!,
                    binding.bankNameEt.text.toString(),
                    binding.BankAccountEt.text.toString(),
                    binding.ifscEt.text.toString()

                );
                responseHandleForEdit()
            }
        }

        binding.UploadResumeTv.setOnClickListener {
            selectPdf()
        }

        binding.backBtn.setOnClickListener {
            finish()
        }


        binding.Dobet.setOnClickListener {
            var today = Calendar.getInstance()
            var c = Calendar.getInstance()
            var year = c.get(Calendar.YEAR)
            var month = c.get(Calendar.MONTH)
            var day = c.get(Calendar.DAY_OF_MONTH)
            var dayofmonth = ""
            var monthofyear = ""
            var selectedyear = ""

            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    selectedyear = year.toString()
                    monthofyear = monthOfYear.toString()
                    dayofmonth = dayOfMonth.toString()

                    if (monthofyear.toString().length == 1) {
                        monthofyear = "0" + (monthofyear.toInt() + 1);
                    }

                    if (dayofmonth.toString().length == 1) {
                        dayofmonth = "0" + dayofmonth;
                    }

                    dobstr = dayofmonth.toString() + "-" + monthofyear + "-" + year.toString()

                    age = today[Calendar.YEAR] - year
                    binding.etAge.setText(age.toString())
                    binding.Dobet.setText(dobstr)
                },
                year,
                month,
                day
            )
            dpd.getDatePicker().setMaxDate(System.currentTimeMillis())
            dpd.show()
        }

        binding.Dojet.setOnClickListener {
            var today = Calendar.getInstance()
            var c = Calendar.getInstance()
            var year = c.get(Calendar.YEAR)
            var month = c.get(Calendar.MONTH)
            var day = c.get(Calendar.DAY_OF_MONTH)
            var dayofmonth = ""
            var monthofyear = ""
            var selectedyear = ""

            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    selectedyear = year.toString()
                    monthofyear = monthOfYear.toString()
                    dayofmonth = dayOfMonth.toString()

                    if (monthofyear.toString().length == 1) {
                        monthofyear = "0" + (monthofyear.toInt() + 1);
                    }

                    if (dayofmonth.toString().length == 1) {
                        dayofmonth = "0" + dayofmonth;
                    }

                    dojstr = dayofmonth.toString() + "-" + monthofyear + "-" + year.toString()

                    binding.Dojet.setText(dojstr)
                },
                year,
                month,
                day
            )
            dpd.getDatePicker().setMaxDate(System.currentTimeMillis())
            dpd.show()
        }

        binding.isHusband.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.FatherNameET.setHint("Enter Husband Name")
                binding.labelFatherName.setText("Husband Name")
                isHusband = 1
            } else {
                binding.FatherNameET.setHint("Enter Father Name")
                binding.labelFatherName.setText("Father Name")
                isHusband = 0
            }
        }

        binding.UploadPhotoEt.setOnClickListener {
            requesCode = 10
            ChoosePhotoTypeDialog(10)
        }

        binding.AdharFront.setOnClickListener {
            requesCode = 11
            ChoosePhotoTypeDialog(11)
        }

        binding.UploadAdhaarBack.setOnClickListener {
            requesCode = 12
            ChoosePhotoTypeDialog(12)
        }
        binding.UploadPanCardtv.setOnClickListener {
            requesCode = 13
            ChoosePhotoTypeDialog(13)
        }

        binding.CancelImg1.setOnClickListener {
            binding.PhotoLayout.visibility = View.GONE
            PhotoPathUri = null
        }

        binding.AdhaarCancleImg.setOnClickListener {
            binding.AdharFrontLayout.visibility = View.GONE
            AdharFrontUri = null
        }
        binding.AdhaarBackCancle.setOnClickListener {
            binding.AdharBackLayout.visibility = View.GONE
            AdharBackUri = null
        }

        binding.PanCancleImg.setOnClickListener {
            binding.PanCardLayout.visibility = View.GONE
            PanUri = null
        }

        binding.genderSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            gender = newText
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.qualifySpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", "" + oldIndex + oldItem + newIndex + newText)
            selectedQualification = newText
        }

        binding.religionSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", "" + oldIndex + oldItem + newIndex + newText)
            selectedReligion = newText
        }

    }


    fun openFilePicker(code: Int) {
        val imagepicker = Intent(Intent.ACTION_PICK)
        imagepicker.type = "image/*"
        startActivityForResult(imagepicker, code)
    }

    private fun requestPermission(code: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_GALLERY_PERMISSION
            )
        } else {
            openFilePicker(code)
        }
    }

    fun getEmployeeData() {
        viewModel.empData(employee_id, this)
    }

    fun empDataResponseHandle() {
        viewModel.empData.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    var dojdate = "";
                    progressBar?.dismiss()
                    binding.NameEt.setText(dataSate.data.data.name)
                    binding.FatherNameET.setText(dataSate.data.data.father_name)
                    binding.etAge.setText("" + dataSate.data.data.age.toString())
                    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val dobdate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                        simpleDateFormat.parse(dataSate.data.data.date_of_birth)!!
                    )
                    if (dataSate.data.data.date_of_joining != null) {
                        dojdate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                            simpleDateFormat.parse(dataSate.data.data.date_of_joining)!!
                        )
                    }

                    binding.Dobet.setText(dobdate)
                    binding.Dojet.setText(dojdate)
                    if (dataSate.data.data.email != null)
                        binding.EmailEt.setText(dataSate.data.data.email)
                    if (dataSate.data.data.mob_no != null)
                        binding.PhoneEt.setText(dataSate.data.data.mob_no)
                    if (dataSate.data.data.pan_number != null)
                        binding.PanEt.setText(dataSate.data.data.pan_number.toString())
                    if (dataSate.data.data.aadhar_number != null)
                        binding.AdhaarEt.setText(dataSate.data.data.aadhar_number.toString())
                    if (dataSate.data.data.photo != null) {
                        Picasso.get().load(dataSate.data.data.photo.toString()).into(binding.Photo1)
                        binding.PhotoLayout.visibility = View.VISIBLE
                    }
                    if (dataSate.data.data.aadhar_front_photo != null) {
                        Picasso.get().load(dataSate.data.data.aadhar_front_photo.toString())
                            .into(binding.AdharFronPhoto)
                        binding.AdharFrontLayout.visibility = View.VISIBLE
                    }

                    if (dataSate.data.data.aadhar_back_photo != null) {
                        Picasso.get().load(dataSate.data.data.aadhar_back_photo.toString())
                            .into(binding.AdharBackImg)
                        binding.AdharBackLayout.visibility = View.VISIBLE
                    }

                    if (dataSate.data.data.pan_front_photo != null) {
                        binding.PanCardLayout.visibility = View.VISIBLE
                        Picasso.get().load(dataSate.data.data.pan_front_photo.toString())
                            .into(binding.PanImg)
                    }

                    if (dataSate.data.data.gender.equals("Male"))
                        binding.genderSpinner.selectItemByIndex(0)
                    else if (dataSate.data.data.gender.equals("Female"))
                        binding.genderSpinner.selectItemByIndex(1)

                    var deptindex = departmentArray.indexOf(dataSate.data.data.department)
                    var desigindex = designationArray.indexOf(dataSate.data.data.designation)

                    binding.desigSpinner.selectItemByIndex(desigindex)
                    binding.departSpinner.selectItemByIndex(deptindex)
                    if (dataSate.data.data.ifsc != null)
                        binding.ifscEt.setText(dataSate.data.data.ifsc)
                    var bankname = "";
                    if (dataSate.data.data.bank_name == null || dataSate.data.data.bank_name == "null")
                        bankname = ""
                    else
                        bankname = dataSate.data.data.bank_name;
                    if (dataSate.data.data.bank_name != null)
                        binding.bankNameEt.setText(bankname)
                    var qualificationindex =
                        qualificationArray.indexOf(dataSate.data.data.qualification)
                    binding.qualifySpinner.selectItemByIndex(qualificationindex)
                    binding.BankAccountEt.setText(dataSate.data.data.bank_ac_no)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {

        } else {
            if (requestCode == 10) {
                var photo: Bitmap? = null
                if (data != null) {
                    PhotoPathUri = data.getData()
                    if (data.extras?.get("data") != null) {
                        photo = data.extras?.get("data") as Bitmap
                        PhotoPathUri = getImageUri(this, photo!!)
                    }
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, PhotoPathUri)
                        binding.Photo1.setImageBitmap(bitmap)
                        binding.PhotoLayout.visibility = View.VISIBLE
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } else if (requestCode == 11) {
                var adharphoto: Bitmap? = null
                if (data != null) {
                    AdharFrontUri = data.getData()
                    if (data.extras?.get("data") != null) {
                        adharphoto = data.extras?.get("data") as Bitmap
                        AdharFrontUri = getImageUri(this, adharphoto!!)
                    }
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, AdharFrontUri)
                        binding.AdharFronPhoto.setImageBitmap(bitmap)
                        binding.AdharFrontLayout.visibility = View.VISIBLE
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } else if (requestCode == 12) {
                var adharback: Bitmap? = null

                if (data != null) {
                    AdharBackUri = data.getData()
                    if (data.extras?.get("data") != null) {
                        adharback = data.extras?.get("data") as Bitmap
                        AdharBackUri = getImageUri(this, adharback!!)
                    }
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, AdharBackUri)
                        binding.AdharBackImg.setImageBitmap(bitmap)
                        binding.AdharBackLayout.visibility = View.VISIBLE
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } else if (requestCode == 13) {
                var panphoto: Bitmap? = null
                if (data != null) {
                    PanUri = data.getData()
                    if (data.extras?.get("data") != null) {
                        panphoto = data.extras?.get("data") as Bitmap
                        PanUri = getImageUri(this, panphoto!!)
                    }
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, PanUri)
                        binding.PanImg.setImageBitmap(bitmap)
                        binding.PanCardLayout.visibility = View.VISIBLE
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } else if (requestCode == 14) {
                pdfUri = data?.data!!
                val uri: Uri = data?.data!!
                val uriString: String = uri.toString()
                var pdfName: String? = null
                if (uriString.startsWith("content://")) {
                    var myCursor: Cursor? = null
                    try {
                        // Setting the PDF to the TextView
                        myCursor =
                            applicationContext!!.contentResolver.query(uri, null, null, null, null)
                        if (myCursor != null && myCursor.moveToFirst()) {
                            pdfName =
                                myCursor.getString(myCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                            binding.UploadResumeTv.text = pdfName
                        }
                    } finally {
                        myCursor?.close()
                    }
                }
                var inputStream: InputStream? = this.contentResolver.openInputStream(pdfUri!!)
                val pdfInBytes: ByteArray = ByteArray(inputStream!!.read())
                inputStream.read(pdfInBytes)
                encodedPdf = Base64.encodeToString(pdfInBytes, Base64.DEFAULT)
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_GALLERY_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openFilePicker(requesCode) //do your job
        } else if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, requesCode)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun ChoosePhotoTypeDialog(requestPermission: Int) {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Photo!")
        builder.setItems(
            options
        ) { dialog, item ->
            if (options[item] == "Take Photo") {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        ), REQUEST_CAMERA_PERMISSION
                    )
                } else {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, requestPermission)
                }
            } else if (options[item] == "Choose from Gallery") {
                requestPermission(requestPermission)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    fun callApi(
        company_id: RequestBody,
        name: RequestBody,
        father_name: RequestBody,
        photo: MultipartBody.Part?,
        date_of_birth: RequestBody,
        email: RequestBody,
        mob_no: RequestBody,
        aadhar_number: RequestBody,
        pan_number: RequestBody,
        gender: RequestBody,
        aadhar_front_photo: MultipartBody.Part?,
        aadhar_back_photo: MultipartBody.Part?,
        pan_front_photo: MultipartBody.Part?,
        husband: RequestBody,
        age: RequestBody,
        paycode: RequestBody,
        dept: RequestBody,
        designation: RequestBody,
        resume: RequestBody,
        dateofjoining: RequestBody,
        bankname: RequestBody,
        bankaccountno: RequestBody,
        ifscCode: RequestBody
    ) {
        viewModel.step1Register(
            company_id,
            name,
            father_name,
            photo,
            date_of_birth,
            email,
            mob_no,
            aadhar_number,
            pan_number,
            gender,
            aadhar_front_photo,
            aadhar_back_photo,
            pan_front_photo,
            husband,
            age,
            paycode,
            dept,
            designation,
            resume,
            dateofjoining,
            bankname,
            bankaccountno,
            ifscCode,
            this
        )

    }

    fun callApiForEdit(
        employee_id: Int,
        name: String,
        father_name: String,
        date_of_birth: String,
        email: String,
        mob_no: String,
        aadhar_number: String,
        pan_number: String,
        gender: String,
        husband: String,
        age: String,
        paycode: String,
        dept: String,
        designation: String,
        religion: String,
        qualification: String,
        bank_name: String,
        bank_ac_no: String,
        ifsc: String,
    ) {
        viewModel.step1Edit(
            employee_id,
            name,
            father_name,
            date_of_birth,
            email,
            mob_no,
            aadhar_number,
            pan_number,
            gender, age,
            paycode,
            dept,
            designation,
            religion,
            qualification,
            bank_name,
            bank_ac_no,
            ifsc,
            this
        )

    }

    fun setGenderSpinnerData() {
        val myArray: Array<String> = resources.getStringArray(R.array.genderArray)
        binding.genderSpinner.setItems(myArray.toList())
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

    fun responseHandle() {
        viewModel.signupResponse.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.status == 200) {
                        Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show()
                        progressBar?.dismiss()
//                        val intent = Intent(this, EmployeeRegistrationStep2::class.java)
//                        intent.putExtra("employee_id", dataSate.data.data.id)
//                        startActivity(intent)
                        finish()
                    } else {
                        progressBar?.dismiss()
                        Toast.makeText(this, dataSate.data.error, Toast.LENGTH_SHORT).show()
                    }
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

    fun responseHandleForEdit() {
        viewModel.editResponse.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show()
                    progressBar?.dismiss()
//                    val intent = Intent(this, EmployeeRegistrationStep2::class.java)
//                    intent.putExtra("employee_id", employee_id)
//                    intent.putExtra("comeFrom", comeFrom)
//                    startActivity(intent)
                    finish()
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

    private fun selectPdf() {
        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
        pdfIntent.type = "application/pdf"
        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(pdfIntent, 14)
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

    fun callApiForMasterData() {
        viewModel.getMasterData(this)
        responseHandleForMasterData()
    }


    fun responseHandleForMasterData() {
        viewModel.masterDataResponse.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    progressBar?.dismiss()
                    department = dataSate.data.data.departments
                    designation = dataSate.data.data.designations
                    designationSetSpinner(designation!!)
                    departmentSpinner(department!!)
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

    fun designationSetSpinner(desigList: List<Designation>) {
        for (i in 0 until desigList!!.size) {
            designationArray?.add(desigList[i].name)
        }
        binding.desigSpinner.setItems(designationArray!!.toList())

        if (adharData != null) {
            selectedDesignation = adharData?.designation
            val desigIndex = designationArray.indexOf(selectedDesignation)
            binding.desigSpinner.selectItemByIndex(desigIndex)
        }
    }

    fun departmentSpinner(departList: List<Department>) {
        for (i in 0 until departList!!.size) {
            departmentArray?.add(departList[i].name)
        }
        binding.departSpinner.setItems(departmentArray!!.toList())

        if (adharData != null) {
            selectedDepartment = adharData?.department
            val deptindex = departmentArray.indexOf(selectedDepartment)
            binding.departSpinner.selectItemByIndex(deptindex)
        }
    }

    fun apicallForBankName(ifsc: String, context: Context) {
        viewModel.getBankFromIfsc("https://ifsc.razorpay.com/" + ifsc, context);
    }

    fun responseHandleForBankName() {
        viewModel.bankNameResponse.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    progressBar?.dismiss()
                    binding.bankNameEt.setText(
                        dataSate.data.BANK + " " + dataSate.data.BRANCH + " " +
                                dataSate.data.STATE
                    )

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

    fun setQualificationSpinner(assetList: List<Asset>, type: String) {
        qualificationArray.clear()
        assetId.clear()
        for (i in 0 until assetList!!.size) {
            qualificationArray?.add(assetList[i].name)
            assetId?.add(assetList[i].id)
        }
        binding.qualifySpinner.setItems(qualificationArray!!.toList())


//        if (type.equals("Qualification")) {
//        } else if (type.equals("Religion")) {
//            binding.religionSpinner.setItems(assetArray!!.toList())
//        }

    }

    fun setReligionSpinner(assetList: List<Asset>, type: String) {
        religionArray.clear()
        assetId.clear()
        for (i in 0 until assetList!!.size) {
            religionArray?.add(assetList[i].name)
            assetId?.add(assetList[i].id)
        }
        binding.religionSpinner.setItems(religionArray!!.toList())


//        if (type.equals("Qualification")) {
//        } else if (type.equals("Religion")) {
//            binding.religionSpinner.setItems(assetArray!!.toList())
//        }

    }

    fun assetSpinnerResponse() {
        viewModel.spinnerResponse.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.status == 200) {
                        progressDialog.dismiss()
                        assetDto = dataSate.data.data.assets
                        if (dataSate.data.data.type.equals("Qualification")) {
                            setQualificationSpinner(assetDto!!, dataSate.data.data.type)
                        } else if (dataSate.data.data.type.equals("Religion")) {
                            setReligionSpinner(assetDto!!, dataSate.data.data.type)
                        }
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

    }

    fun callApiForSpinnerAsset(type: String) {
        viewModel.getAssets(type, this)
    }

}

