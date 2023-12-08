package com.workseasy.com.ui.hradmin.employeeRegistration.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hr.hrmanagement.R
import com.workseasy.com.ui.hradmin.employeeRegistration.response.*

    class earningadapter(context: Context,var dataClicked: step5Adapter.DataClicked? = null): RecyclerView.Adapter<earningadapter.Viewholder>()  {
    var list = ArrayList<EarningData?>()
    var context = context
    val dataClick = dataClicked

    class Viewholder(itemView: View): RecyclerView.ViewHolder(itemView){
        val earningvalue = itemView.findViewById<TextView>(R.id.earningvalue)
        val earningname = itemView.findViewById<TextView>(R.id.earningname)


        val deletearning = itemView.findViewById<ImageView>(R.id.deletearning)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): earningadapter.Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.earninglist_item, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: earningadapter.Viewholder, position: Int) {
        holder.earningname.setText(list.get(position)?.earning_type)
        holder.earningvalue.setText(list.get(position)?.value)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface DataClicked {
        fun itemClicked(id: Int?)
    }

    fun setData(data: ArrayList<EarningData>?) {
        list.clear()
        list.addAll(data!!)
        notifyDataSetChanged()
    }

}