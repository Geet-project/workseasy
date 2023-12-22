package com.workseasy.com.ui.purchase.screens

import android.Manifest
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.loader.content.CursorLoader
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityRaisePrBinding
import com.workseasy.com.ui.hradmin.employeeRegistration.response.Asset
import com.workseasy.com.ui.purchase.response.ItemData
import com.workseasy.com.ui.purchase.viewmodel.RaisePrViewModel
import com.workseasy.com.utils.GenericTextWatcher
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*

class RaisePrActivity : AppCompatActivity() {
    lateinit var _binding: ActivityRaisePrBinding
    val binding get() = _binding!!
    private val REQUEST_GALLERY_PERMISSION = 1
    private val REQUEST_CAMERA_PERMISSION= 2
    var requesCode: Int=0
    var PhotoPathUri: Uri? = null
    var bitmap: Bitmap? = null
    var selectedItem: String?= null
    var selectedMake: String?=null
    var selectedGrade: String?=null
    var progressBar: ProgressDialog?=null
    var photobody: MultipartBody.Part? = null
    lateinit var progressDialog: ProgressDialog
    var assetDto: List<Asset>?=null
    var assetArray = mutableListOf<String>()
    var assetId = mutableListOf<Int>()
    var deliverydatestr: String = ""
    var itemDto: List<ItemData>?=null

    var itemArray = mutableListOf<String>()
    var itemId = mutableListOf<Int>()

    var makeArray = mutableListOf<String>()
    var gradeArray = ArrayList<String>()

    var selectedGradePostionArray = ArrayList<Int>()
    var selectedGradeArray = ArrayList<String>()


    val viewModel: RaisePrViewModel by viewModels()




    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_raise_pr)
        progressDialog = ProgressDialog(this)
        progressBar = ProgressDialog(this)
        callApiForItemList()
        itemResponseHandle()
        setListeners()
        _binding.itemSpinner.setTitle("Select Item")
        _binding.itemSpinner.setPositiveButton("OK");

    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun setListeners()
    {
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.UploadPhotoEt.setOnClickListener {
            requesCode = 10
            ChoosePhotoTypeDialog(10)
        }

        binding.CancelImg1.setOnClickListener {
            binding.PhotoLayout.visibility = View.GONE
            PhotoPathUri = null
        }

        binding.itemSpinner.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedItem = itemArray.get(p2);
                var index = itemArray.indexOf(selectedItem)
                var selectedItemId = itemId.get(index)
                callApiForPrAsset(selectedItemId)
                AssetResponseHandle()
                binding.itemError.visibility = View.GONE
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        })

//        binding.itemSpinner.onItemSelectedListener <String> { oldIndex, oldItem, newIndex, newText ->
//            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
//            selectedItem = newText
//
//        }

        binding.makeSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
            selectedMake = newText
            binding.makeError.visibility = View.GONE

        }

//        binding.gradeSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
//            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
//            selectedGrade = newText
//            binding.gradeError.visibility = View.GONE
//
//        }





        binding.SubmitBtn.setOnClickListener {
            var qty = binding.QtyEt.text.toString()
            var unit = binding.UnitEt.text.toString()
            var delivery = binding.DeliveryEt.text.toString()
            var reasonLeaving = binding.RemarkEt.text.toString()

            for (i in 0 until selectedGradePostionArray!!.size) {
                selectedGradeArray.add(gradeArray.get(selectedGradePostionArray.get(i)))
            }

            if(selectedItem==null)
            {
                binding.itemError.visibility = View.VISIBLE
                binding.itemError.text = "*Please select Item."
            }else if(selectedMake==null)
            {
                binding.makeError.visibility = View.VISIBLE
                binding.makeError.text = "*Please select Make."
            }else if(selectedGrade==null)
            {
                binding.gradeError.visibility = View.VISIBLE
                binding.gradeError.text = "*Please select Grade."
            }else if(qty.equals(""))
            {
                binding.QtyEt.requestFocus()
                binding.qtyError.visibility = View.VISIBLE
                binding.qtyError.text = "*Please enter quantity"
            }else if(unit.equals(""))
            {
                binding.UnitEt.requestFocus()
                binding.unitError.visibility = View.VISIBLE
                binding.unitError.text = "*Please enter unit"
            }else if(deliverydatestr.equals(""))
            {
                binding.DeliveryEt.requestFocus()
                binding.deliveryError.visibility = View.VISIBLE
                binding.deliveryError.text = "*Please enter delivery"
            }else{
                var selectedItemreq        = RequestBody.create("text/plain".toMediaTypeOrNull(),
                    selectedItem!!)
                var selectedMakereq  = RequestBody.create("text/plain".toMediaTypeOrNull(),selectedMake!!)
                var selectedGradereq         = RequestBody.create("text/plain".toMediaTypeOrNull(), selectedGrade!!)
                var qtyreq = RequestBody.create("text/plain".toMediaTypeOrNull(),qty)
                var unitreq      = RequestBody.create("text/plain".toMediaTypeOrNull(), unit)
                var deliveryreq = RequestBody.create("text/plain".toMediaTypeOrNull(),deliverydatestr)
                var remarkreq   = RequestBody.create("text/plain".toMediaTypeOrNull(), reasonLeaving)
                if(PhotoPathUri!=null)
                {
                    val file: File = File(getRealPathFromURI(PhotoPathUri!!))
                    val requestFile    = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                    photobody          = MultipartBody.Part.createFormData("image", file.name, requestFile)
                }
                callApiForRaisePr(selectedItemreq, selectedMakereq, selectedGradereq, qtyreq,
                unitreq, deliveryreq, remarkreq, photobody)
                responseHandle()
            }
        }

        binding.QtyEt.addTextChangedListener(GenericTextWatcher(binding.QtyEt, null,binding.qtyError))
        binding.UnitEt.addTextChangedListener(GenericTextWatcher(binding.UnitEt, null, binding.unitError))
        binding.RemarkEt.addTextChangedListener(GenericTextWatcher(binding.RemarkEt, null, binding.remarkError))

        binding.DeliveryEt.setOnClickListener {
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

                    if(monthofyear.toInt()<8)
                        monthofyear = "0" + (monthofyear.toInt() + 1);
                    else
                        monthofyear =  (monthofyear.toInt() + 1).toString();

                    if (dayofmonth.toString().length == 1) {
                        dayofmonth = "0" + dayofmonth;
                    }
                    deliverydatestr = dayofmonth.toString() + "-" + monthofyear + "-" + year.toString()
                    binding.DeliveryEt.setText(deliverydatestr)
                },
                year,
                month,
                day
            )
            dpd.show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun  ChoosePhotoTypeDialog(requestPermission: Int){
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

    fun openFilePicker(code: Int) {
        val imagepicker = Intent(Intent.ACTION_PICK)
        imagepicker.type = "image/*"
        startActivityForResult(imagepicker, code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10) {
            var photo:Bitmap?=null

            if (data != null) {
                PhotoPathUri = data.getData()
                if (data != null) {
                    PhotoPathUri = data.getData()
                    if(data.extras?.get("data")!=null)
                    {
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

            }
        }
    }

    fun callApiForRaisePr(
        item: RequestBody,
        make: RequestBody,
        gender: RequestBody,
        delivery: RequestBody,
        quantity: RequestBody,
        unit: RequestBody,
        remark: RequestBody,
        photo: MultipartBody.Part?,
    )
    {
        viewModel.raisePr(item,make, gender, delivery, quantity, unit, remark, photo, this)
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

    fun responseHandle()
    {
        viewModel.raiseprSubmitResponse.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
                    if(dataSate.data.status==200)
                    {
                        Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show()
                        progressBar?.dismiss()
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
                    Toast.makeText(this,""+ dataSate.exception, Toast.LENGTH_SHORT).show()
                }
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
        }else if(requestCode == REQUEST_CAMERA_PERMISSION && grantResults[0]== PackageManager.PERMISSION_GRANTED)
        {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, requesCode)
        }
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

    fun callApiForItemList()
    {
        viewModel.getItemList(this)
    }

    fun itemResponseHandle()
    {
        viewModel.getItemListResponse.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
                    if(dataSate.data.code==200)
                    {
                        progressBar?.dismiss()
                        itemDto = dataSate.data.data
                        setItemSpinner(itemDto!!)
                    }else{
                        progressBar?.dismiss()
                        Toast.makeText(this, dataSate.data.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    progressBar?.show()
                    progressBar?.setMessage("Please Wait..")
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressBar?.dismiss()
                    Toast.makeText(this,""+ dataSate.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun setItemSpinner(itemList: List<ItemData>)
    {
        itemArray.clear()
        itemId.clear()
        for (i in 0 until itemList!!.size) {
            itemArray?.add(itemList[i].name)
            itemId?.add(itemList[i].id)
        }
        binding.itemSpinner.adapter = ArrayAdapter<Any?>(
            this@RaisePrActivity,
            android.R.layout.simple_spinner_dropdown_item, itemArray.toList()
        )

    }

    fun callApiForPrAsset(itemId: Int)
    {
        viewModel.getAssetsItemList(itemId,this)
    }

    fun AssetResponseHandle()
    {
        viewModel.getAssetsListResponse.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
                    if(dataSate.data.code==200)
                    {
                        progressBar?.dismiss()
                        setMakeSpinner(dataSate.data.data.make)
                        setGradeSpinner(dataSate.data.data.grades)
                        binding.UnitEt.setText(dataSate.data.data.unit)
                    }else{
                        progressBar?.dismiss()
                        Toast.makeText(this, dataSate.data.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    progressBar?.show()
                    progressBar?.setMessage("Please Wait..")
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressBar?.dismiss()
                    Toast.makeText(this,""+ dataSate.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun setMakeSpinner(makeList: List<String>)
    {
        makeArray.clear()
        for (i in 0 until makeList!!.size) {
            makeArray?.add(makeList[i])
        }
        binding.makeSpinner.setItems(makeArray!!.toList())

    }

    fun setGradeSpinner(gradeList: List<String>)
    {
        gradeArray.clear()
        for (i in 0 until gradeList!!.size) {
            gradeArray?.add(gradeList[i])
        }

        val testDataList = gradeArray
        with(binding) {
            gradeSpinner.buildCheckedSpinner(testDataList) {
                    selectedPositionList, displayString ->
                selectedGradePostionArray = selectedPositionList
            }
        }

    }

}