package com.workseasy.com.ui.hradmin.employeeRegistration

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityFamiliDetailBinding
import com.hr.hrmanagement.databinding.ActivityWorkExperienceBinding
import com.skydoves.powerspinner.PowerSpinnerView
import com.workseasy.com.ui.hradmin.employeeRegistration.adapter.familyadapter
import com.workseasy.com.ui.hradmin.employeeRegistration.adapter.workexpadapter
import com.workseasy.com.ui.hradmin.employeeRegistration.response.AddFamilyDto
import com.workseasy.com.ui.hradmin.employeeRegistration.response.Asset
import com.workseasy.com.ui.hradmin.employeeRegistration.response.FamilyRelationMasterData
import com.workseasy.com.ui.hradmin.employeeRegistration.response.WorkExpDto
import com.workseasy.com.ui.hradmin.employeeRegistration.viewmodel.SignupViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class FamiliDetailActivity : AppCompatActivity(), familyadapter.DataClicked {
    lateinit var binding: ActivityFamiliDetailBinding
    var employee_id: Int = 0
    val viewModel: SignupViewModel by viewModels()
    var dialog: AlertDialog? = null
    var dobstr: String = ""
    lateinit var progressDialog: ProgressDialog
    lateinit var familyadapter: familyadapter
    var relationArray = mutableListOf<String>()
    var selectedRelation: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_famili_detail)
        employee_id = intent.getIntExtra("employee_id", 0)
        progressDialog = ProgressDialog(this)

        familyadapter = familyadapter(this, this)

        setListeners()

        apiCallForFamilyDetailList(employee_id)
        responseHandle()


    }

    @SuppressLint("MissingInflatedId")
    fun setListeners() {
        binding.FabBtn.setOnClickListener {
            val mbuilder = AlertDialog.Builder(this)
            val inflater1: LayoutInflater = LayoutInflater.from(this).context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
            ) as LayoutInflater
            val v1: View = inflater1.inflate(R.layout.add_familydetail_diaog, null)
            val transition_in_view = AnimationUtils.loadAnimation(
                this,
                R.anim.dialog_animation
            ) //customer animation appearance

            val nameEt: EditText = v1.findViewById(R.id.NameEt)
            val relationSpinner: PowerSpinnerView = v1.findViewById(R.id.relationSpinner)
//            val relationEt: EditText = v1.findViewById(R.id.RelationEt)
            val tvdob: TextView = v1.findViewById(R.id.tvdob)
//            val workexpet: EditText = v1.findViewById(R.id.workexpet)
            val AddFamilySubmit: Button = v1.findViewById(R.id.AddFamilySubmit)
            relationSpinner.setItems(relationArray)

            val cancelBtnDialog: ImageButton = v1.findViewById(R.id.cancelBtnDialog)

            relationSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
                Log.e("stateSpin", "" + oldIndex + oldItem + newIndex + newText)
                selectedRelation = newText
            }
            AddFamilySubmit.setOnClickListener {
                apiCallForFamilyDetail(
                    AddFamilyDto(
                        selectedRelation!!,
                        employee_id, nameEt.text.toString(), dobstr
                    )
                )
                insertResponseHandle()

            }


            tvdob.setOnClickListener {
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

                        tvdob.setText(dobstr)

                    },
                    year,
                    month,
                    day
                )
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis())
                dpd.show()
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


    }


    fun apiCallForFamilyDetail(familyDto: AddFamilyDto) {
        viewModel.addFamilyDetail(familyDto, this)
    }

    fun insertResponseHandle() {
        viewModel.familyDetailResponse.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.code == 200) {
                        progressDialog.dismiss()
                        dialog!!.dismiss()
                        Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show()
                        apiCallForFamilyDetailList(employee_id)
                        responseHandle()
                    } else {
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

    fun apiCallForFamilyDetailList(employee_id: Int) {
        viewModel.getFamilyDetail(employee_id!!, this)
    }

    fun responseHandle() {
        viewModel.getFamilyDetailResponse.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.code == 200) {
                        if (dataSate.data.data.size > 0) {
                            progressDialog?.dismiss()
                            familyadapter.setData(dataSate.data.data)
                            binding.recyclerView.adapter = familyadapter
                            binding.recyclerView.visibility = View.VISIBLE
                        } else {
                            progressDialog?.dismiss()
                            binding.recyclerView.visibility = View.GONE
                            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show()
                        }
                        setRelationSpinner(dataSate.data.relationMasterData)
                    } else {
                        progressDialog?.dismiss()
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

    override fun itemClicked(id: Int?) {
        viewModel.deleteFamilyId(id, this);
        deleteResponseHandle()
    }

    fun deleteResponseHandle() {
        viewModel.deleteFamilyDetailResponse.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.code == 200) {
                        Toast.makeText(this, dataSate.data.message, Toast.LENGTH_SHORT).show()
                        apiCallForFamilyDetailList(employee_id)

                    } else {
                        progressDialog?.dismiss()
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

    fun setRelationSpinner(assetList: List<FamilyRelationMasterData>) {
        relationArray.clear()
        for (i in 0 until assetList!!.size) {
            relationArray?.add(assetList[i].name)
        }

    }


}