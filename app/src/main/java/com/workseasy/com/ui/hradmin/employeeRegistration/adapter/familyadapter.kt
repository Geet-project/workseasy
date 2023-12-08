package com.workseasy.com.ui.hradmin.employeeRegistration.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hr.hrmanagement.R
import com.workseasy.com.ui.hradmin.employeeRegistration.response.AddFamilyData
import com.workseasy.com.ui.hradmin.employeeRegistration.response.Step5Response
import com.workseasy.com.ui.hradmin.employeeRegistration.response.WorkExpData
import com.workseasy.com.ui.hradmin.employeeRegistration.response.WorkExpResponse

class familyadapter(context: Context,var dataClicked: familyadapter.DataClicked? = null): RecyclerView.Adapter<familyadapter.Viewholder>()  {
    var list = ArrayList<AddFamilyData?>()
    var context = context
    val dataClick = dataClicked

    class Viewholder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.name)
        val relation = itemView.findViewById<TextView>(R.id.relation)
        val dobtv = itemView.findViewById<TextView>(R.id.dobtv)


        val deleteCardStep5 = itemView.findViewById<ImageView>(R.id.deleteCardStep5)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): familyadapter.Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.add_family_layout, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: familyadapter.Viewholder, position: Int) {
        holder.name.setText("Name:   " + list.get(position)?.name)
        holder.dobtv.setText("Date of Birth:   " + list.get(position)?.dob)
        holder.relation.setText("Relation:   "+list.get(position)?.relation)
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

    fun setData(data: ArrayList<AddFamilyData>?) {
        list.clear()
        list.addAll(data!!)
        notifyDataSetChanged()
    }

}