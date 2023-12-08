package com.workseasy.com.ui.hradmin.employeeDetails.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.hr.hrmanagement.R
import com.squareup.picasso.Picasso
import com.workseasy.com.ui.hradmin.employeeDetails.response.AdvanceSalary
import de.hdodenhof.circleimageview.CircleImageView

class ApprovalSalaryAdapter(context: Context,var dataClicked: DataClicked? = null): RecyclerView.Adapter<ApprovalSalaryAdapter.AdvViewHolder>() {
    var advlist = ArrayList<AdvanceSalary?>()
    var context = context
    val dataClick = dataClicked

    inner class AdvViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val profilePhoto = itemView.findViewById<CircleImageView>(R.id.empProfilePhoto)
        val empName = itemView.findViewById<TextView>(R.id.empName)
        val empDesignation = itemView.findViewById<TextView>(R.id.empDesignation)
        val empCode = itemView.findViewById<TextView>(R.id.empCode)
        val requestedAmount = itemView.findViewById<TextView>(R.id.requestedAmount)
        val advanceReason = itemView.findViewById<TextView>(R.id.advanceReason)
        val maxdeductionTv = itemView.findViewById<TextView>(R.id.maxdeductionTv)
        val salarytilldate = itemView.findViewById<TextView>(R.id.salarytilldate)
        val approvebtn = itemView.findViewById<Button>(R.id.approvebtn)
        val rejectbtn = itemView.findViewById<Button>(R.id.rejectbtn)
        val currentDate = itemView.findViewById<TextView>(R.id.currentDate)
        val firstLayout = itemView.findViewById<LinearLayout>(R.id.FirstLayout)
        val secndLayout = itemView.findViewById<LinearLayout>(R.id.secondLayout)

        val date = itemView.findViewById<TextView>(R.id.date)
        val status = itemView.findViewById<TextView>(R.id.status)
        val advanceamount = itemView.findViewById<TextView>(R.id.advanceamount)
        val advanceReason2 = itemView.findViewById<TextView>(R.id.advanceReason2)
        val remarks = itemView.findViewById<TextView>(R.id.remarks)
        val commentbtn = itemView.findViewById<AppCompatButton>(R.id.commentbtn)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ApprovalSalaryAdapter.AdvViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.salary_approval_item, parent, false)
        return AdvViewHolder(view)    }

    override fun onBindViewHolder(holder: ApprovalSalaryAdapter.AdvViewHolder, position: Int) {
        if(advlist.get(position)?.photo.equals("") || advlist.get(position)?.photo==null){
            holder.profilePhoto.setImageResource(R.drawable.profilephoto)
        }else{
            Picasso.get().load(advlist.get(position)?.photo).into(holder.profilePhoto)
        }
        holder.empName.setText(advlist.get(position)?.name)
        holder.empDesignation.setText(advlist.get(position)?.designation.toString())
        holder.empCode.setText("Employee Code :"+advlist.get(position)?.employee_code.toString())
        holder.requestedAmount.setText("Requested Amount: "+advlist.get(position)?.amount)
        holder.advanceReason.text = advlist.get(position)?.reason
        holder.maxdeductionTv.text = advlist.get(position)?.maximum_deduction_per_month
        holder.salarytilldate.text= advlist.get(position)?.salary_payable_till_date
        holder.currentDate.text = advlist.get(position)?.date

        holder.date.text = advlist.get(position)?.date
        holder.status.text = advlist.get(position)?.remark
        holder.advanceamount.text = "Advanced: "+advlist.get(position)?.amount
        holder.advanceReason2.text = advlist.get(position)?.reason
        holder.remarks.text = advlist.get(position)?.remark

        if(advlist.get(position)?.remark.equals("approved"))
        {
            holder.status.setTextColor(Color.parseColor("#06DF47"))
            holder.firstLayout.visibility = View.GONE
            holder.secndLayout.visibility = View.VISIBLE

        }else if(advlist.get(position)?.remark.equals("reject")){
            holder.status.setTextColor(Color.parseColor("#F10101"))
            holder.firstLayout.visibility = View.GONE
            holder.secndLayout.visibility = View.VISIBLE
        }else{
            holder.firstLayout.visibility = View.VISIBLE
            holder.secndLayout.visibility = View.GONE
        }

        holder.approvebtn.setOnClickListener {
            dataClick!!.itemClicked("approved",
                advlist.get(position)?.id)
        }

        holder.rejectbtn.setOnClickListener {
            dataClick!!.itemClicked("reject",
                advlist.get(position)?.id)
        }

        holder.commentbtn.setOnClickListener {
            dataClick!!.itemClicked("comment",
                advlist.get(position)?.id)
        }
    }

    fun setData(data: ArrayList<AdvanceSalary>?) {
        advlist.clear()
        advlist.addAll(data!!)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return advlist.size
    }
    interface DataClicked {
        fun itemClicked(buttonstatus: String?,leave_id: Int?)
    }
}