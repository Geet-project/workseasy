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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.loader.content.CursorLoader
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityGrnEntryBinding
import com.workseasy.com.ui.hradmin.employeeRegistration.response.Asset
import com.workseasy.com.ui.purchase.viewmodel.RaisePrViewModel
import com.workseasy.com.utils.GenericTextWatcher
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*

class GrnEntryActivity : AppCompatActivity() {
    lateinit var _binding: ActivityGrnEntryBinding
    val binding get() = _binding!!
    val viewModel: RaisePrViewModel by viewModels()
    var receiptDate: String ?=null
    var progressDialog: ProgressDialog?=null
    var assetDto: List<Asset>?=null
    var assetArray = mutableListOf<String>()
    var selectedPr :String?=null
    var selectedItem: String?=null
    var PhotoPathUri: Uri? = null
    var requesCode: Int=0
    private val REQUEST_GALLERY_PERMISSION = 1
    private val REQUEST_CAMERA_PERMISSION= 2
    var bitmap: Bitmap? = null



    var photobody: MultipartBody.Part? = null






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this,R.layout.activity_grn_entry)
        progressDialog = ProgressDialog(this)
        setListeners()
        setValidationListeners()
        callApiForSpinnerAsset("Item")
        callApiForSpinnerAsset("Pr")
        spinnerAssetresponseHandle()
    }

    fun setValidationListeners()
    {
        binding.QtyEt.addTextChangedListener(GenericTextWatcher(binding.QtyEt, null, binding.qtyError))
        binding.BillEt.addTextChangedListener(GenericTextWatcher(binding.BillEt, null,binding.billError))
        binding.RemarkEt.addTextChangedListener(GenericTextWatcher(binding.RemarkEt, null, binding.remarkError))
    }

    fun setListeners()
    {
        binding.backBtn.setOnClickListener {
            finish()
        }


        binding.reciptTv.setOnClickListener { val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // this never ends while debugging
                    // Display Selected date in textbox
                    receiptDate = dayOfMonth.toString() + "-" + (monthOfYear+1).toString() + "-" + year.toString()
                    binding.reciptTv.setText(receiptDate)
                    binding.receiptError.visibility= View.GONE
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        binding.itemSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
            selectedItem = newText
            binding.itemError.visibility = View.GONE
        }

        binding.prSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("stateSpin", ""+oldIndex+oldItem+newIndex+newText)
            selectedPr= newText
            binding.prError.visibility = View.GONE
        }

        binding.UploadPhotoEt.setOnClickListener {
            requesCode = 10
            ChoosePhotoTypeDialog(10)
        }

        binding.SubmitBtn.setOnClickListener {
            var quantity = binding.QtyEt.text.toString()
            var bill_no = binding.BillEt.text.toString()
            var RemarkValue = binding.RemarkEt.text.toString()

            if(selectedPr==null || selectedPr.equals(""))
            {
                binding.prError.setText("*Please Select Pr")
                binding.prError.visibility = View.VISIBLE
            } else if(selectedItem==null || selectedItem.equals(""))
            {
                binding.itemError.setText("*Please Select Item")
                binding.itemError.visibility = View.VISIBLE
            }else if(quantity.equals(""))
            {
                binding.QtyEt.requestFocus();
                binding.qtyError.setText("*Please enter Quantity")
                binding.qtyError.visibility = View.VISIBLE
            }else if(bill_no.equals(""))
            {
                binding.BillEt.requestFocus();
                binding.billError.setText("*Please enter Bill No.")
                binding.billError.visibility = View.VISIBLE
            }else if(receiptDate==null){
                binding.receiptError.setText("*Please enter Receipt Date.")
                binding.receiptError.visibility = View.VISIBLE
            }else if(RemarkValue.equals(""))
            {
                binding.RemarkEt.requestFocus();
                binding.remarkError.setText("*Please enter Remark")
                binding.remarkError.visibility = View.VISIBLE
            }else if(PhotoPathUri==null)
            {
                Toast.makeText(this, "Please choose Photo.", Toast.LENGTH_SHORT).show()
            }else{
                var qty        = RequestBody.create("text/plain".toMediaTypeOrNull(),
                    quantity)
                var billno        = RequestBody.create("text/plain".toMediaTypeOrNull(),
                    bill_no)
                var remark =RequestBody.create("text/plain".toMediaTypeOrNull(), RemarkValue)
                var receiptDateReq =RequestBody.create("text/plain".toMediaTypeOrNull(), receiptDate!!)
                var selectedPrRrq = RequestBody.create("text/plain".toMediaTypeOrNull(), selectedPr!!)
                var selectedItemRrq = RequestBody.create("text/plain".toMediaTypeOrNull(), selectedItem!!)
                if(PhotoPathUri!=null)
                {
                    val file: File = File(getRealPathFromURI(PhotoPathUri!!))
                    val requestFile    = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                    photobody          = MultipartBody.Part.createFormData("bill_photo", file.name, requestFile)
                }
                storeData(selectedPrRrq, selectedItemRrq, remark, billno, receiptDateReq, qty, photobody)
                responseHandle()
            }
        }
    }

    fun storeData(po : RequestBody,
                  item: RequestBody,
                  remark: RequestBody,
                  bill_no: RequestBody,
                  receipt_date: RequestBody,
                  quantity: RequestBody,
                  photo: MultipartBody.Part?)
    {
        viewModel.grnStore(po, item, remark, bill_no, receipt_date, quantity, photo, this)
    }

    fun callApiForSpinnerAsset(type: String)
    {
        viewModel.getAssets(type, this)
    }

        fun spinnerAssetresponseHandle()
        {
            viewModel.spinnerResponse.observe(this){ dataSate->
                when(dataSate)
                {
                    is com.workseasy.com.network.DataState.Success ->{
                        if(dataSate.data.status==200)
                        {
                            progressDialog!!.dismiss()
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

    fun setAssetSpinner(assetList: List<Asset>, type: String)
    {
        assetArray.clear()
        for (i in 0 until assetList!!.size) {
            assetArray?.add(assetList[i].name)
        }
        if(type.equals("Pr"))
        {
            binding.prSpinner.setItems(assetArray!!.toList() )
        }else if(type.equals("Item"))
        {
            binding.itemSpinner.setItems(assetArray!!.toList())
        }

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


    fun responseHandle()
    {
        viewModel.grnstoreResponse.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
                    if(dataSate.data.status==200)
                    {
                        Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show()
                        progressDialog?.dismiss()
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
                    Toast.makeText(this,""+ dataSate.exception, Toast.LENGTH_SHORT).show()
                }
            }
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
}