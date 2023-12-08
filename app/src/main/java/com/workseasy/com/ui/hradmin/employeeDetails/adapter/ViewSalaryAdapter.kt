package com.workseasy.com.ui.hradmin.employeeDetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hr.hrmanagement.R
import com.workseasy.com.ui.hradmin.employeeDetails.response.Salary

class ViewSalaryAdapter(context: Context): RecyclerView.Adapter<ViewSalaryAdapter.Viewholder>() {
    var salarylist = ArrayList<Salary?>()
    var context = context
    inner class Viewholder(itemView: View): RecyclerView.ViewHolder(itemView){
        val wdvalue = itemView.findViewById<TextView>(R.id.wdvalue)
        val elvalue = itemView.findViewById<TextView>(R.id.elvalue)
        val clvalue = itemView.findViewById<TextView>(R.id.clvalue)
        val slvalue = itemView.findViewById<TextView>(R.id.slvalue)
        val hdvalue = itemView.findViewById<TextView>(R.id.hdvalue)
        val pdvalue = itemView.findViewById<TextView>(R.id.pdvalue)
        val otvalue = itemView.findViewById<TextView>(R.id.otvalue)
        val basicvalue = itemView.findViewById<TextView>(R.id.basicvalue)
        val davalue = itemView.findViewById<TextView>(R.id.davalue)
        val hravalue = itemView.findViewById<TextView>(R.id.hravalue)
        val conveyancevalue = itemView.findViewById<TextView>(R.id.conveyancevalue)
        val medicalvalue = itemView.findViewById<TextView>(R.id.medicalvalue)
        val telephonevalue = itemView.findViewById<TextView>(R.id.telephonevalue)
        val othervalue = itemView.findViewById<TextView>(R.id.othervalue)
        val otEarningValue = itemView.findViewById<TextView>(R.id.otEarningValue)
        val epfvalue = itemView.findViewById<TextView>(R.id.epfvalue)
        val vpfvalue = itemView.findViewById<TextView>(R.id.vpfvalue)
        val esivalue = itemView.findViewById<TextView>(R.id.esivalue)
        val tdsvalue = itemView.findViewById<TextView>(R.id.tdsvalue)
        val lwfvalue = itemView.findViewById<TextView>(R.id.lwfvalue)
        val advValue = itemView.findViewById<TextView>(R.id.advValue)
        val TotalValue = itemView.findViewById<TextView>(R.id.TotalValue)
        val totalEarningValue = itemView.findViewById<TextView>(R.id.totalEarningValue)
        val NetPayableValue = itemView.findViewById<TextView>(R.id.NetPayableValue)
        val monthname = itemView.findViewById<TextView>(R.id.monthname)
        val ptValue = itemView.findViewById<TextView>(R.id.ptValue)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewSalaryAdapter.Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.salary_layout_item, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: ViewSalaryAdapter.Viewholder, position: Int) {
        holder.wdvalue.text = ""+salarylist.get(position)?.attendance?.wd
        holder.elvalue.text = ""+salarylist.get(position)?.attendance?.el
        holder.clvalue.text = ""+salarylist.get(position)?.attendance?.cl
        holder.slvalue.text = ""+salarylist.get(position)?.attendance?.sl
        holder.hdvalue.text = ""+salarylist.get(position)?.attendance?.hd
        holder.pdvalue.text = ""+salarylist.get(position)?.attendance?.pd
        holder.otvalue.text = ""+salarylist.get(position)?.attendance?.ot

        holder.basicvalue.text = ""+salarylist.get(position)?.earning?.basic
        holder.davalue.text = ""+salarylist.get(position)?.earning?.da.toString()
        holder.hravalue.text = ""+salarylist.get(position)?.earning?.hra.toString()
        holder.conveyancevalue.text = ""+salarylist.get(position)?.earning?.conveyance.toString()
        holder.medicalvalue.text = ""+salarylist.get(position)?.earning?.medical.toString()
        holder.telephonevalue.text = ""+salarylist.get(position)?.earning?.telephone.toString()
        holder.othervalue.text = ""+salarylist.get(position)?.earning?.other.toString()
        holder.otEarningValue.text = ""+salarylist.get(position)?.earning?.ot.toString()

        holder.epfvalue.text = ""+salarylist.get(position)?.deducation?.epf.toString()
        holder.vpfvalue.text = ""+salarylist.get(position)?.deducation?.vpf.toString()
        holder.esivalue.text = ""+salarylist.get(position)?.deducation?.esic.toString()
        holder.tdsvalue.text = ""+salarylist.get(position)?.deducation?.tds.toString()
        holder.lwfvalue.text = ""+salarylist.get(position)?.deducation?.lwf.toString()
        holder.advValue.text = ""+salarylist.get(position)?.deducation?.adv.toString()
        holder.TotalValue.text = ""+salarylist.get(position)?.deducation?.total.toString()
        holder.ptValue.text = ""+ salarylist.get(position)?.deducation?.pt.toString()

        holder.totalEarningValue.text = ""+salarylist.get(position)?.total.toString()
        holder.NetPayableValue.text = ""+salarylist.get(position)?.net_payble.toString()
        holder.monthname.text = ""+salarylist.get(position)?.month+","+salarylist.get(position)?.year


    }

    override fun getItemCount(): Int {
        return salarylist.size
    }

    fun setData(data: ArrayList<Salary>?) {
        salarylist.clear()
        salarylist.addAll(data!!)
        notifyDataSetChanged()
    }
}