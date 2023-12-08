package com.workseasy.com.ui.hradmin.attendance

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityAttendanceWithLocationBinding
import com.squareup.picasso.Picasso
import com.workseasy.com.ui.hradmin.attendance.response.AttendanceWithLocationDto
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class AttendanceWithLocationActivity : AppCompatActivity() {

    lateinit var binding: ActivityAttendanceWithLocationBinding
    var emp_id: Int? = null
    var emp_code: String? = null
    var empname: String? = null
    var photo: String? = null
    var designation: String? = null
    var mobile: String? = null
    val viewModel: AttendanceViewModel by viewModels()
    var progressDialog: ProgressDialog? = null
    var context: Context? = null
    var locationManager: LocationManager? = null
    var currentDate: String?=null
    var cityName: String? =null
    var isPunched: Boolean? = false
    var fusedLocationProviderClient: FusedLocationProviderClient? =null
    var dialog: AlertDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_attendance_with_location)
        emp_id = intent.getIntExtra("employee_id", 0)
        emp_code = intent.getStringExtra("empCode")
        empname = intent.getStringExtra("empName")
        photo = intent.getStringExtra("photo")
        mobile = intent.getStringExtra("mobile")
        designation = intent.getStringExtra("designation")
        designation = intent.getStringExtra("designation")
        progressDialog = ProgressDialog(this)
        context = this@AttendanceWithLocationActivity
        progressDialog!!.show()
        progressDialog!!.setMessage("Fetching Location")
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        val sdf = SimpleDateFormat("dd-M-yyyy")
        currentDate = sdf.format(Date())
        getLastLocation()



        binding.punchInBtn.setOnClickListener {
            val currentTime: Date = Calendar.getInstance().getTime()
            val df: DateFormat = SimpleDateFormat("HH:mm", Locale.US)
            val time_chat_s: String = df.format(currentTime)
            callApiForAttendanceWithLocation(
                AttendanceWithLocationDto(
                    currentDate!!,
                    emp_id!!,
                    cityName!!,
                    time_chat_s,
                    "",
                    "punch_in",
                    "attendance4"
                )
            )
            punchresponseHandle("punch_in", currentDate.toString())
        }

        binding.punchOutBtn.setOnClickListener {
            val currentTime: Date = Calendar.getInstance().getTime()
            val df: DateFormat = SimpleDateFormat("HH:mm", Locale.US)
            val time_chat_s: String = df.format(currentTime)
            callApiForAttendanceWithLocationPunchOut(
                AttendanceWithLocationDto(
                    currentDate!!,
                    emp_id!!,
                    cityName!!,
                    "",
                    time_chat_s,
                    "punch_out",
                    "attendance4"
                )
            )
            punchoutresponseHandle("punch_out", currentDate.toString())
        }
        binding.backBtn.setOnClickListener {
            finish()
        }

        setUI()
    }

    fun setUI() {
        if (photo == null || photo.equals(""))
            binding.empProfilePhoto.setImageResource(R.drawable.profilephoto)
        else
            Picasso.get().load(photo).into(binding.empProfilePhoto)
        binding.empName.text = empname
        binding.empNumber.text = mobile
        binding.empCode.text = emp_code
        binding.empDesignation.text = designation
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@AttendanceWithLocationActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION) ===
                                PackageManager.PERMISSION_GRANTED)
                        && (ContextCompat.checkSelfPermission(this@AttendanceWithLocationActivity,
                            Manifest.permission.ACCESS_COARSE_LOCATION) ===
                                PackageManager.PERMISSION_GRANTED)) {
                        getLastLocation();
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    fun callApiForAttendanceWithLocation(attendanceWithLocationDto: AttendanceWithLocationDto)
    {
        viewModel.attendancewithLocation(attendanceWithLocationDto, this)
    }

    fun callApiForAttendanceWithLocationPunchOut(attendanceWithLocationDto: AttendanceWithLocationDto)
    {
        viewModel.attendancewithLocation(attendanceWithLocationDto, this)
    }

    fun callApiForGetAttendanceWithLocation(empid:Int?, date: String?)
    {
        viewModel.getattendancewithLocation(empid!!, date!!, this)
    }

    fun responseHandle()
    {
        viewModel?.getAttendanceWithLocResponse?.observe(this) { dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success -> {
                    if(dataSate.data.code==200)
                    {
                        progressDialog!!.dismiss()
                        if(dataSate.data.data.punch_in==null) {
                            isPunched = false;
                            binding.punchOutBtn.visibility = View.GONE
                            binding.punchInBtn.visibility = View.VISIBLE
                        }else if(dataSate.data.data.punch_in!=null) {
                            isPunched = true;
                            binding.punchOutBtn.visibility = View.VISIBLE
                            binding.punchInBtn.visibility = View.GONE
                            binding.punchintime.visibility = View.VISIBLE
                            binding.punchintime.setText("Punch In Time : "+dataSate.data.data.punch_in)
                        }

                        if(dataSate.data.data.punch_out!=null) {
                            binding.punchouttime.visibility = View.VISIBLE
                            binding.punchouttime.setText("Punch Out Time : "+dataSate.data.data.punch_out)
                        }
                    }else{
                        progressDialog?.dismiss()
                        Toast.makeText(this, dataSate.data.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    progressDialog?.setMessage("Please Wait..")
                    progressDialog?.show()
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressDialog?.dismiss()
                    Toast.makeText(this, ""+dataSate.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun punchresponseHandle(type: String, date: String)
    {
        viewModel?.attendanceWithLocResponse?.observe(this) { dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success -> {
                    if(dataSate.data.code==200)
                    {
                        progressDialog!!.dismiss()
                        showDialogDone(dataSate.data.message, date)
                    } else{
                        progressDialog?.dismiss()
                        Snackbar.make(binding.mainLayout, dataSate.data.message, Snackbar.LENGTH_LONG)
                            .setActionTextColor(resources.getColor(android.R.color.holo_red_light))
                            .show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    progressDialog?.setMessage("Please Wait..")
                    progressDialog?.show()
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressDialog?.dismiss()
                    Toast.makeText(this, ""+dataSate.exception, Toast.LENGTH_SHORT).show()
                    viewModel.attendanceWithLocResponse.removeObservers(this@AttendanceWithLocationActivity);

                }
            }
        }
    }

    fun punchoutresponseHandle(type: String, date: String)
    {
        viewModel?.attendancePunchOutResponse?.observe(this) { dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success -> {
                    if(dataSate.data.code==200)
                    {
                        progressDialog!!.dismiss()
                        showDialogDone(dataSate.data.message, date)
                    } else{
                        progressDialog?.dismiss()
                        Snackbar.make(binding.mainLayout, dataSate.data.message, Snackbar.LENGTH_LONG)
                            .setActionTextColor(resources.getColor(android.R.color.holo_red_light))
                            .show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    progressDialog?.setMessage("Please Wait..")
                    progressDialog?.show()
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressDialog?.dismiss()
                    Toast.makeText(this, ""+dataSate.exception, Toast.LENGTH_SHORT).show()
                    viewModel.attendanceWithLocResponse.removeObservers(this@AttendanceWithLocationActivity);

                }
            }
        }
    }

    fun getLastLocation(){
        if(ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient!!.getLastLocation()
                .addOnSuccessListener {
                    if(it!=null){
                        val gcd = Geocoder(this, Locale.getDefault())
                        val addresses: List<Address>
                        try {
                            addresses = gcd.getFromLocation(
                                it.latitude,
                                it.longitude, 1
                            )
                            if (addresses.size > 0) {
                                System.out.println(addresses[0].getLocality())
                                cityName = addresses[0].getLocality()
                                callApiForGetAttendanceWithLocation(emp_id!!, currentDate!!)
                                responseHandle()
                                binding.location.setText("Location: "+cityName)
                                binding.location.visibility = View.VISIBLE
                                print(cityName)
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
        }else {
            ActivityCompat.requestPermissions(
                this@AttendanceWithLocationActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                100
            );
        }
    }

    fun showDialogDone(status: String?, date: String?)
    {
        val mbuilder = AlertDialog.Builder(this)
        val inflater1: LayoutInflater = LayoutInflater.from(this).context.getSystemService(
            Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v1: View = inflater1.inflate(R.layout.dialog_attendancedone, null)
        val transition_in_view = AnimationUtils.loadAnimation(
            this,
            R.anim.dialog_animation
        ) //customer animation appearance
        val dialogdonemssg: TextView = v1.findViewById(R.id.dialogdonemssg)
        val okbtn: Button = v1.findViewById(R.id.okbtn)
        dialogdonemssg.setText(status)
        v1.animation = transition_in_view
        v1.startAnimation(transition_in_view)
        mbuilder.setView(v1)
        dialog = mbuilder.create()
        okbtn.setOnClickListener {
            dialog?.dismiss()
            callApiForGetAttendanceWithLocation(emp_id!!, date)
            responseHandle()
        }
        dialog!!.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.show()
    }


}