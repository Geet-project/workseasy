package com.workseasy.com.ui.hradmin.employeeDetails.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.hr.hrmanagement.R
import com.squareup.picasso.Picasso
import com.workseasy.com.ui.HomeActivity
import com.workseasy.com.ui.hradmin.attendance.AttendanceForm1Activity
import com.workseasy.com.ui.hradmin.attendance.AttendanceWithLocationActivity
import com.workseasy.com.ui.hradmin.employeeDetails.response.AdvanceSalary
import com.workseasy.com.ui.hradmin.employeeRegistration.EmployeeRegistrationStep1
import com.workseasy.com.ui.hradmin.employeeRegistration.response.AdharUniqueDto
import com.workseasy.com.ui.hradmin.employeelist.EmployeeListActivity
import com.workseasy.com.ui.login.LoginViewModel
import com.workseasy.com.ui.login.response.DataX
import com.workseasy.com.ui.purchase.screens.ConsumptionEntryActivity
import com.workseasy.com.ui.purchase.screens.CreatePoActivity
import com.workseasy.com.ui.purchase.screens.GrnEntryActivity
import com.workseasy.com.ui.purchase.screens.PrApprovalActivity
import com.workseasy.com.ui.purchase.screens.PrListingActiivty
import com.workseasy.com.ui.purchase.screens.QuotationActivity
import com.workseasy.com.ui.purchase.screens.RaisePrActivity
import com.workseasy.com.utils.GenericTextWatcher
import com.workseasy.com.utils.SharedPref
import de.hdodenhof.circleimageview.CircleImageView

class HomeAdapter(context: Context,private val loginViewModel: LoginViewModel,
                  val listener: OnClickListener): RecyclerView.Adapter<HomeAdapter.AdvViewHolder>() {
    var homelist = ArrayList<DataX?>()
    var context = context
    var dialog: AlertDialog? = null
    var sharedPref: SharedPref? = null


    inner class AdvViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val homeDashboarditemTxt = itemView.findViewById<TextView>(R.id.homeDashboarditemTxt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dashboard_home_layout, parent, false)
        return AdvViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdvViewHolder , position: Int) {
        sharedPref = SharedPref(context);
        holder.homeDashboarditemTxt.setText(homelist.get(position)?.name)

        holder.itemView.setOnClickListener {
            if(homelist.get(position)!!.name=="Employee Registration") {
                val mbuilder = AlertDialog.Builder(context)
                val inflater1: LayoutInflater = LayoutInflater.from(context).context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val v1: View = inflater1.inflate(R.layout.adhar_dialog, null)
                val transition_in_view = AnimationUtils.loadAnimation(
                    context,
                    R.anim.dialog_animation
                ) //customer animation appearance
                val RemarkEt: EditText = v1.findViewById(R.id.adharEt)
                val remarkError: TextView = v1.findViewById(R.id.adharError)
                val btnSent: Button = v1.findViewById(R.id.remarkBtnSent)
                val cancelBtnDialog : ImageButton = v1.findViewById(R.id.cancelBtnDialog)
                RemarkEt.addTextChangedListener(GenericTextWatcher(RemarkEt, null, remarkError))
                btnSent.setOnClickListener {
                    if(RemarkEt.text.toString().length<12)
                    {
                        RemarkEt.requestFocus()
                        remarkError.visibility = View.VISIBLE
                        remarkError.text = "*Please enter valid aadhaar number"
                    }else {
                        var adharnumber = RemarkEt.text.toString()
                        var companyid =  sharedPref!!.getData(SharedPref.COMPANYID, "0")
                        loginViewModel?.checkAdhar(AdharUniqueDto(adharnumber,companyid.toString().toInt()),context)
                        listener.onClick(adharnumber, dialog!!)
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
            } else if(homelist.get(position)!!.name=="Employee List") {
                val intent = Intent(context, EmployeeListActivity::class.java)
                    .putExtra("comeFrom", "list")
                context.startActivity(intent)
            } else if(homelist.get(position)!!.name=="Daily attendance entry") {
                val intent = Intent(context, EmployeeListActivity::class.java)
                    .putExtra("comeFrom", "Attendance")
                    .putExtra("formtype", 1)
                context.startActivity(intent)
            } else if(homelist.get(position)!!.name=="Monthly attendance entry") {
                val intent = Intent(context, EmployeeListActivity::class.java)
                    .putExtra("comeFrom", "Attendance")
                    .putExtra("formtype", 3)
                context.startActivity(intent)
            } else if(homelist.get(position)!!.name=="Monthly attendance entry With Leave details"){
                val intent = Intent(context, EmployeeListActivity::class.java)
                    .putExtra("comeFrom", "Attendance")
                    .putExtra("formtype", 2)
                context.startActivity(intent)
            } else if(homelist.get(position)!!.name=="Attendance With Location entry") {
                val intent = Intent(context, EmployeeListActivity::class.java)
                    .putExtra("comeFrom", "Attendance")
                    .putExtra("formtype", 4)
                context.startActivity(intent)
            } else if(homelist.get(position)!!.name=="Auto attendance entry") {
                val intent = Intent(context, EmployeeListActivity::class.java)
                    .putExtra("comeFrom", "Attendance")
                    .putExtra("formtype", 5)
                context.startActivity(intent)
            } else if(homelist.get(position)!!.name=="Purchase Request") {
                val intent = Intent(context, RaisePrActivity::class.java)
//                    .putExtra("comeFrom", "Attendance")
//                    .putExtra("formtype", 5)
                context.startActivity(intent)
            } else if(homelist.get(position)!!.name=="Purchase Approval") {
                val intent = Intent(context, PrListingActiivty::class.java)
                    .putExtra("comeFrom", "PR")
//                    .putExtra("formtype", 5)
                context.startActivity(intent)
            } else if(homelist.get(position)!!.name=="GRN Entry") {
                val intent = Intent(context, GrnEntryActivity::class.java)
//                    .putExtra("comeFrom", "Attendance")
//                    .putExtra("formtype", 5)
                context.startActivity(intent)
            } else if(homelist.get(position)!!.name=="GRN Approval") {
                val intent = Intent(context, GrnEntryActivity::class.java)
//                    .putExtra("comeFrom", "Attendance")
//                    .putExtra("formtype", 5)
                context.startActivity(intent)
            } else if(homelist.get(position)!!.name=="Quotation Approval") {
                val intent = Intent(context, QuotationActivity::class.java)
//                    .putExtra("comeFrom", "Attendance")
//                    .putExtra("formtype", 5)
                context.startActivity(intent)
            } else if(homelist.get(position)!!.name=="Consumption Entry") {
                val intent = Intent(context, ConsumptionEntryActivity::class.java)
//                    .putExtra("comeFrom", "Attendance")
//                    .putExtra("formtype", 5)
                context.startActivity(intent)
            }else if(homelist.get(position)!!.name=="PR Pending For Quotation") {
                val intent = Intent(context, PrListingActiivty::class.java)
                    .putExtra("comeFrom", "Quotation")
//                    .putExtra("formtype", 5)
                context.startActivity(intent)
            }else if(homelist.get(position)!!.name=="Create PO") {
                val intent = Intent(context, CreatePoActivity::class.java)
//                    .putExtra("formtype", 5)
                context.startActivity(intent)
            }
        }

    }

    override fun getItemCount(): Int {
        return homelist.size
    }

    fun setData(data: ArrayList<DataX>?) {
        homelist.clear()
        homelist.addAll(data!!)
        notifyDataSetChanged()
    }

    interface OnClickListener {
        fun onClick(adharnumber: String,dialog: AlertDialog)
    }

}