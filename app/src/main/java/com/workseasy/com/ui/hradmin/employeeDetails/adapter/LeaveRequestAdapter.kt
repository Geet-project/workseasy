package com.workseasy.com.ui.hradmin.employeeDetails.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hr.hrmanagement.R
import com.squareup.picasso.Picasso
import com.workseasy.com.ui.hradmin.employeeDetails.response.LeaveRequestListData
import de.hdodenhof.circleimageview.CircleImageView

class LeaveRequestAdapter(context: Context): RecyclerView.Adapter<LeaveRequestAdapter.LeaveViewHolder>() {
    var leaveList = ArrayList<LeaveRequestListData?>()
    var context = context
    inner class LeaveViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val profilePhoto = itemView.findViewById<CircleImageView>(R.id.empProfilePhoto)
        val empName = itemView.findViewById<TextView>(R.id.empName)
        val empDesignation = itemView.findViewById<TextView>(R.id.empDesignation)
        val leaveStatus = itemView.findViewById<TextView>(R.id.leaveStatus)
        val reason = itemView.findViewById<TextView>(R.id.leaveReason)
        val leaveDate = itemView.findViewById<TextView>(R.id.leaveDate)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaveViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.leave_request_item, parent, false)
        return LeaveViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaveViewHolder, position: Int) {
        if(leaveList.get(position)?.photo.equals("") || leaveList.get(position)?.photo==null){
            holder.profilePhoto.setImageResource(R.drawable.profilephoto)
        }else{
            Picasso.get().load(leaveList.get(position)?.photo).into(holder.profilePhoto)
        }
        holder.empName.setText(leaveList.get(position)?.name)
        holder.empDesignation.setText(leaveList.get(position)?.designation.toString())
        holder.leaveStatus.setText(leaveList.get(position)?.status)
        if(leaveList.get(position)?.status.equals("pending"))
            holder.leaveStatus.setTextColor(Color.parseColor("#119FA4"))
        else if(leaveList.get(position)?.status.equals("reject"))
            holder.leaveStatus.setTextColor(Color.parseColor("#FF0707"))
        else if(leaveList.get(position)?.status.equals("approved"))
            holder.leaveStatus.setTextColor(Color.parseColor("#06DF47"))

        holder.reason.text = leaveList.get(position)?.reason
        var leavestr = leaveList.get(position)?.from_date + "  To  " + leaveList.get(position)?.to_date
        holder.leaveDate.text = leavestr
    }

    fun setData(data: ArrayList<LeaveRequestListData>?) {
        leaveList.clear()
        leaveList.addAll(data!!)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return leaveList.size
    }

}