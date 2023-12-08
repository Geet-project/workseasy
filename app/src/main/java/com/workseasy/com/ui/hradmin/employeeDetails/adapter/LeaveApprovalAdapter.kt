package com.workseasy.com.ui.hradmin.employeeDetails.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.hr.hrmanagement.R
import com.squareup.picasso.Picasso
import com.workseasy.com.ui.hradmin.employeeDetails.response.LeaveRequestListData
import de.hdodenhof.circleimageview.CircleImageView

class LeaveApprovalAdapter(context: Context,var dataClicked: DataClicked? = null): RecyclerView.Adapter<LeaveApprovalAdapter.LeaveViewHolder>() {
    var approveList = ArrayList<LeaveRequestListData?>()
    var context = context
    val dataClick = dataClicked

    inner class LeaveViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val profilePhoto = itemView.findViewById<CircleImageView>(R.id.empProfilePhoto)
        val empName = itemView.findViewById<TextView>(R.id.empName)
        val empDesignation = itemView.findViewById<TextView>(R.id.empDesignation)
        val empCode = itemView.findViewById<TextView>(R.id.empCode)
        val leaveStatus = itemView.findViewById<TextView>(R.id.leaveApproveStatus)
        val reason = itemView.findViewById<TextView>(R.id.leaveReasonTv)
        val leaveDate = itemView.findViewById<TextView>(R.id.leaveDuration)
        val leaveCount =itemView.findViewById<TextView>(R.id.totalLeaveDays)
        val currentDate = itemView.findViewById<TextView>(R.id.ApproveDate)
        val approveTopLayout = itemView.findViewById<LinearLayout>(R.id.approveTopLayout)
        val RejectedLayout = itemView.findViewById<RelativeLayout>(R.id.RejectedLayout)
        var approveBtn = itemView.findViewById<AppCompatButton>(R.id.approvebtn)
        var rejectBtn = itemView.findViewById<AppCompatButton>(R.id.rejectbtn)
        val ApproveRejectLayout = itemView.findViewById<LinearLayout>(R.id.ApproveRejectLayout)
        val date = itemView.findViewById<TextView>(R.id.currentDate)
        val commentbtn = itemView.findViewById<AppCompatButton>(R.id.commentbtn)

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LeaveApprovalAdapter.LeaveViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.leave_request_approve_item, parent, false)
        return LeaveViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaveApprovalAdapter.LeaveViewHolder, position: Int) {
        if(approveList.get(position)?.photo.equals("") || approveList.get(position)?.photo==null){
            holder.profilePhoto.setImageResource(R.drawable.profilephoto)
        }else{
            Picasso.get().load(approveList.get(position)?.photo).into(holder.profilePhoto)
        }
        holder.empName.setText(approveList.get(position)?.name)
        holder.empDesignation.setText(approveList.get(position)?.designation.toString())
        holder.empCode.setText("Employee Code: "+approveList.get(position)?.employee_code.toString())
        holder.leaveStatus.setText(approveList.get(position)?.status)
        holder.reason.text = approveList.get(position)?.reason
        var leavestr = approveList.get(position)?.from_date + "  To  " + approveList.get(position)?.to_date
        holder.leaveDate.text = leavestr
        holder.currentDate.text = approveList.get(position)?.date
        holder.leaveCount.text = ""+approveList.get(position)?.days+" Days"
        holder.date.text = approveList.get(position)?.date


        if(approveList.get(position)?.status.equals("pending"))
        {
            holder.approveTopLayout.visibility = View.VISIBLE
            holder.RejectedLayout.visibility = View.GONE
            holder.ApproveRejectLayout.visibility = View.VISIBLE
        }else if(approveList.get(position)?.status.equals("reject"))
        {
            holder.approveTopLayout.visibility = View.GONE
            holder.RejectedLayout.visibility = View.VISIBLE
            holder.ApproveRejectLayout.visibility = View.GONE
            holder.leaveStatus.text = "Rejected"
            holder.leaveStatus.setTextColor(Color.parseColor("#F10101"))
        }else if(approveList.get(position)?.status.equals("approved"))
        {
            holder.approveTopLayout.visibility = View.GONE
            holder.RejectedLayout.visibility = View.VISIBLE
            holder.ApproveRejectLayout.visibility = View.GONE
            holder.leaveStatus.text = "Approved"
            holder.leaveStatus.setTextColor(Color.parseColor("#06DF47"))
        }

        holder.approveBtn.setOnClickListener{
            dataClick!!.itemClicked("approved",
                approveList.get(position)?.id)
        }

        holder.rejectBtn.setOnClickListener {
            dataClick!!.itemClicked("reject",
                approveList.get(position)?.id)
        }

        holder.commentbtn.setOnClickListener {
            dataClick!!.itemClicked("comment",
                approveList.get(position)?.id)
        }

    }
    fun setData(data: ArrayList<LeaveRequestListData>?) {
        approveList.clear()
        approveList.addAll(data!!)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return approveList.size
    }

    interface DataClicked {
        fun itemClicked(buttonstatus: String?,leave_id: Int?)
    }

}