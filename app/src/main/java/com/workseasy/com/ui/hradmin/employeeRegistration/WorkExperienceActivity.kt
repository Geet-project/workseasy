package com.workseasy.com.ui.hradmin.employeeRegistration

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityWorkExperienceBinding
import com.workseasy.com.ui.hradmin.employeeRegistration.adapter.step5Adapter
import com.workseasy.com.ui.hradmin.employeeRegistration.adapter.workexpadapter
import com.workseasy.com.ui.hradmin.employeeRegistration.response.WorkExpDto
import com.workseasy.com.ui.hradmin.employeeRegistration.viewmodel.SignupViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class WorkExperienceActivity : AppCompatActivity(), workexpadapter.DataClicked {
    lateinit var binding: ActivityWorkExperienceBinding
    lateinit var progressDialog: ProgressDialog
    var dialog: AlertDialog? = null
    val viewModel: SignupViewModel by viewModels()
    var dojstr: String = ""
    var dolstr: String = ""
    lateinit var workexpadapter: workexpadapter
    lateinit var dateofJoingDate: LocalDate;
    lateinit var dateOfLeavingDate: LocalDate;

    var employee_id: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_work_experience)
        employee_id = intent.getIntExtra("employee_id", 0)
        progressDialog = ProgressDialog(this)
        workexpadapter = workexpadapter(this, this)

        setListeners()

        apiCallForWorkExpList(employee_id)
        responseHandle()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setListeners() {
        binding.FabBtn.setOnClickListener {
            val mbuilder = AlertDialog.Builder(this)
            val inflater1: LayoutInflater = LayoutInflater.from(this).context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
            ) as LayoutInflater
            val v1: View = inflater1.inflate(R.layout.work_exp_dialog, null)
            val transition_in_view = AnimationUtils.loadAnimation(
                this,
                R.anim.dialog_animation
            ) //customer animation appearance

            val OrgEt: EditText = v1.findViewById(R.id.OrgEt)
            val DesigEt: EditText = v1.findViewById(R.id.DesigEt)
            val tvdoj: TextView = v1.findViewById(R.id.tvdoj)
            val tvdol: TextView = v1.findViewById(R.id.tvdoL)
            val ReasonLeavingEt: EditText = v1.findViewById(R.id.ReasonLeavingEt)
//            val workexpet: EditText = v1.findViewById(R.id.workexpet)
            val Step5Submit: Button = v1.findViewById(R.id.Step5Submit)

            val cancelBtnDialog: ImageButton = v1.findViewById(R.id.cancelBtnDialog)
            Step5Submit.setOnClickListener {
                var countDays = ChronoUnit.DAYS.between(dateofJoingDate, dateOfLeavingDate)
                if (countDays <= 0) {
                    Toast.makeText(
                        this,
                        "Date of Leaving must be greater than Date of Joining",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    apiCallForWorkExp(
                        WorkExpDto(
                            dojstr,
                            dolstr,
                            DesigEt.text.toString(),
                            employee_id,
                            OrgEt.text.toString(),
                            ReasonLeavingEt.text.toString()
                        )
                    )
                    insertResponseHandle()
                }

            }


            tvdoj.setOnClickListener {
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

                        tvdoj.setText(dojstr)

                        val formatter = DateTimeFormatter.ofPattern("d-MM-yyyy")
                        dateofJoingDate = LocalDate.parse(dojstr, formatter)
                    },
                    year,
                    month,
                    day
                )
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis())
                dpd.show()
            }

            tvdol.setOnClickListener {
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

                        dolstr = dayofmonth.toString() + "-" + monthofyear + "-" + year.toString()

                        tvdol.setText(dolstr)

                        val formatter = DateTimeFormatter.ofPattern("d-MM-yyyy")
                        dateOfLeavingDate = LocalDate.parse(dolstr, formatter)
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

    fun apiCallForWorkExp(workExpDto: WorkExpDto) {
        viewModel.addWorkExp(workExpDto, this)
    }

    fun insertResponseHandle() {
        viewModel.workExpResponse.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.code == 200) {
                        progressDialog.dismiss()
                        dialog!!.dismiss()
                        Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show()
                        apiCallForWorkExpList(employee_id)
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

    fun apiCallForWorkExpList(employee_id: Int) {
        viewModel.getWorkExp(employee_id!!, this)
    }

    fun responseHandle() {
        viewModel.getworkExpResponse.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.code == 200) {
                        if (dataSate.data.data.size > 0) {
                            var totalexp = 0;
                            progressDialog?.dismiss()
                            workexpadapter.setData(dataSate.data.data)
                            binding.recyclerView.adapter = workexpadapter
                            binding.recyclerView.visibility = View.VISIBLE
                            for (i in 0 until dataSate.data.data.size) {
                                totalexp =
                                    dataSate.data.data[i].total_work_experience.toInt() + totalexp
                            }
                            binding.totalexptv.setText("Total Work Expereince : " + totalexp.toString())
                        } else {
                            progressDialog?.dismiss()
                            binding.recyclerView.visibility = View.GONE
                            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show()
                        }
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
        viewModel.deleteWorkExp(id, this);
        deleteWorkExpResponseHandle()
    }

    fun deleteWorkExpResponseHandle() {
        viewModel.deleteWorkExpResponse.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                    if (dataSate.data.code == 200) {
                        Toast.makeText(this, dataSate.data.message, Toast.LENGTH_SHORT).show()
                        apiCallForWorkExpList(employee_id)

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
}