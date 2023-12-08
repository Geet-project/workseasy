package com.workseasy.com.ui.hradmin.employeeRegistration.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hr.hrmanagement.R
import com.workseasy.com.ui.hradmin.employeeRegistration.response.Step5Response
import com.workseasy.com.ui.hradmin.employeeRegistration.response.WorkExpData
import com.workseasy.com.ui.hradmin.employeeRegistration.response.WorkExpResponse

class workexpadapter(context: Context,var dataClicked: workexpadapter.DataClicked? = null): RecyclerView.Adapter<workexpadapter.Viewholder>()  {
    var list = ArrayList<WorkExpData?>()
    var context = context
    val dataClick = dataClicked

    class Viewholder(itemView: View): RecyclerView.ViewHolder(itemView){
        val orgname = itemView.findViewById<TextView>(R.id.orgname)
        val designame = itemView.findViewById<TextView>(R.id.designame)
        val dojtv = itemView.findViewById<TextView>(R.id.dojtv)
        val doltv = itemView.findViewById<TextView>(R.id.doltv)
        val reasonleavintv = itemView.findViewById<TextView>(R.id.reasonleavintv)


        val deleteCardStep5 = itemView.findViewById<ImageView>(R.id.deleteCardStep5)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): workexpadapter.Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.work_exp_layout, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: workexpadapter.Viewholder, position: Int) {
        holder.orgname.setText("Organization Name:   " + list.get(position)?.name)
        holder.designame.setText("Designation Name:   " + list.get(position)?.designation)
        holder.doltv.setText("Date of Joining:   "+list.get(position)?.date_of_join)
        holder.dojtv.setText("Date of Leaving:   "+list.get(position)?.date_of_leave)
        holder.reasonleavintv.setText("Reason of Leaving:   "+list.get(position)?.reason_of_leave)

        holder.deleteCardStep5.setOnClickListener {
            dataClick?.itemClicked(list.get(position)?.id)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface DataClicked {
        fun itemClicked(id: Int?)
    }

    fun setData(data: ArrayList<WorkExpData>?) {
        list.clear()
        list.addAll(data!!)
        notifyDataSetChanged()
    }

}