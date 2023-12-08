package com.workseasy.com.ui.purchase.screens

import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityPrApprovalBinding
import com.squareup.picasso.Picasso
import com.workseasy.com.ui.purchase.response.PrData
import com.workseasy.com.ui.purchase.viewmodel.RaisePrViewModel
import com.workseasy.com.utils.GenericTextWatcher

class PrApprovalActivity : AppCompatActivity() {
    lateinit var _binding: ActivityPrApprovalBinding
    val viewModel: RaisePrViewModel by viewModels()
    val binding get() = _binding
    var pr_id: Int?=null
    var dialog: AlertDialog? = null
    var progressDialog: ProgressDialog?=null
    var prlistData : ArrayList<PrData>? = null
    var index : Int =0
    var projectname : String? = null
    var item: String? =null
    var make: String? =null
    var grade: String? =null
    var quantity: String? =null
    var unit: String? =null
    var deliveryrequired: String? =null
    var remark:String? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_pr_approval)
        pr_id = intent.getIntExtra("pr_id", 0)
        projectname = intent.getStringExtra("projectname")
        item = intent.getStringExtra("item")
        make = intent.getStringExtra("make")
        grade = intent.getStringExtra("grade")
        quantity = intent.getStringExtra("quantity")
        unit = intent.getStringExtra("unit")
        deliveryrequired = intent.getStringExtra("deliveryrequired")
        remark = intent.getStringExtra("remark")
        index = intent.getIntExtra("index", 0)
        setListeners()
//        callApiForViewPr()
//        responseHandle()

        binding.projectNameEt.setText(projectname)
        binding.itemEt.setText(item)
        binding.makeEt.setText(make)
        binding.GradeEt.setText(grade)
        binding.QtyEt.setText(quantity)
        binding.UnitEt.setText(unit)
        binding.DeliveryEt.setText(deliveryrequired)
        binding.RemarkEt.setText(remark)

    }

    fun setListeners()
    {
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.approvebtn.setOnClickListener {
            showDialog("approved")
        }
        binding.rejectbtn.setOnClickListener {
            showDialog("reject")
        }
    }

    fun callApiForViewPr()
    {
        viewModel.viewPr(pr_id, this)
    }
    fun responseHandle()
    {
        viewModel?.viewprResponse?.observe(this) { dataSate ->
            when (dataSate) {
                is com.workseasy.com.network.DataState.Success -> {
                        if (dataSate.data.status == 200) {

                        if(dataSate.data.data!=null)
                        {
                            binding.itemEt.setText(dataSate.data.data.item.toString())
                            binding.makeEt.setText(dataSate.data.data.make.toString())
                            binding.itemEt.setText(dataSate.data.data.grade.toString())
                            binding.QtyEt.setText(dataSate.data.data.quantity.toString())
                            binding.UnitEt.setText(dataSate.data.data.unit.toString())
                            binding.DeliveryEt.setText(dataSate.data.data.delivery.toString())
                            binding.RemarkEt.setText(dataSate.data.data.remark.toString())
                            Picasso.get().load(dataSate.data.data.image).into(binding.Photo1)


                        }

                    } else {
                        Toast.makeText(
                            this,
                            "" + dataSate.data.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                }

                is com.workseasy.com.network.DataState.Error -> {
                    Toast.makeText(this, "" + dataSate.exception, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    fun showDialog(status: String?)
    {
        val mbuilder = AlertDialog.Builder(this)
        val inflater1: LayoutInflater = LayoutInflater.from(this).context.getSystemService(
            Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v1: View = inflater1.inflate(R.layout.dialog_remarks, null)
        val transition_in_view = AnimationUtils.loadAnimation(
            this,
            R.anim.dialog_animation
        ) //customer animation appearance
        val RemarkEt: EditText = v1.findViewById(R.id.remarksEt)
        val remarkError: TextView = v1.findViewById(R.id.remarkError)
        val btnSent: Button = v1.findViewById(R.id.remarkBtnSent)
        val cancelBtnDialog : ImageButton = v1.findViewById(R.id.cancelBtnDialog)
        RemarkEt.addTextChangedListener(GenericTextWatcher(RemarkEt, null, remarkError))
        btnSent.setOnClickListener {
            if(RemarkEt.text.toString().equals(""))
            {
                RemarkEt.requestFocus()
                remarkError.visibility = View.VISIBLE
                remarkError.text = "*Please enter Remarks"
            }else{
                viewModel?.updatePr(pr_id, status , RemarkEt.text.toString(),this)
                updatePrResponse()
            }
        }
        v1.animation = transition_in_view
        v1.startAnimation(transition_in_view)
        mbuilder.setView(v1)
        dialog = mbuilder.create()
        cancelBtnDialog.setOnClickListener {
            dialog?.dismiss()
        }
        dialog!!.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.show()
    }

    fun updatePrResponse()
    {
        viewModel?.updatePrResponse?.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
                    if(dataSate.data.status==200)
                    {
                        Toast.makeText(this, "Status Updated", Toast.LENGTH_SHORT).show()
                        progressDialog?.dismiss()
                        dialog!!.dismiss()
                        finish()

                    }else{
                        progressDialog?.dismiss()
                        dialog!!.dismiss()

                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    progressDialog?.show()
                    progressDialog?.setMessage("Please Wait...")
                }

                is com.workseasy.com.network.DataState.Error -> {
                    progressDialog?.dismiss()
                    Toast.makeText(this, ""+dataSate.exception, Toast.LENGTH_SHORT).show()
                    dialog!!.dismiss()

                }
            }
        }
    }
}