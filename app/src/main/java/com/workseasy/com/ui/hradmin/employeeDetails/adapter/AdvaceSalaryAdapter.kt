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
import com.workseasy.com.ui.hradmin.employeeDetails.response.AdvanceSalary
import de.hdodenhof.circleimageview.CircleImageView

class AdvaceSalaryAdapter(context: Context): RecyclerView.Adapter<AdvaceSalaryAdapter.AdvViewHolder>() {
    var advlist = ArrayList<AdvanceSalary?>()
    var context = context
    inner class AdvViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val profilePhoto = itemView.findViewById<CircleImageView>(R.id.empProfilePhoto)
        val empName = itemView.findViewById<TextView>(R.id.empName)
        val empDesignation = itemView.findViewById<TextView>(R.id.empDesignation)
        val leaveStatus = itemView.findViewById<TextView>(R.id.advStatus)
        val reason = itemView.findViewById<TextView>(R.id.advReason)
        val leaveDate = itemView.findViewById<TextView>(R.id.advAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adv_item, parent, false)
        return AdvViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdvViewHolder
                                  , position: Int) {
        if(advlist.get(position)?.photo.equals("") || advlist.get(position)?.photo==null){
            holder.profilePhoto.setImageResource(R.drawable.profilephoto)
        }else{
            Picasso.get().load(advlist.get(position)?.photo).into(holder.profilePhoto)
        }
        holder.empName.setText(advlist.get(position)?.name)
        holder.empDesignation.setText(advlist.get(position)?.designation.toString())
        holder.leaveStatus.setText(advlist.get(position)?.status)
        if(advlist.get(position)?.status.equals("pending"))
            holder.leaveStatus.setTextColor(Color.parseColor("#119FA4"))
        else if(advlist.get(position)?.status.equals("reject"))
            holder.leaveStatus.setTextColor(Color.parseColor("#FF0707"))
        else if(advlist.get(position)?.status.equals("approved"))
            holder.leaveStatus.setTextColor(Color.parseColor("#06DF47"))

        holder.reason.text = advlist.get(position)?.reason
        var amount = advlist.get(position)?.amount
        holder.leaveDate.text = "Advance Amount: "+amount
    }

    override fun getItemCount(): Int {
        return advlist.size
    }
    fun setData(data: ArrayList<AdvanceSalary>?) {
        advlist.clear()
        advlist.addAll(data!!)
        notifyDataSetChanged()
    }
}