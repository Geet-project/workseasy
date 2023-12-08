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

class step5Adapter(context: Context,var dataClicked: step5Adapter.DataClicked? = null): RecyclerView.Adapter<step5Adapter.Viewholder>()  {
    var list = ArrayList<Step5Response?>()
    var context = context
    val dataClick = dataClicked

    class Viewholder(itemView: View): RecyclerView.ViewHolder(itemView){
        val Step5Title = itemView.findViewById<TextView>(R.id.Step5Title)
        val Step5Amount = itemView.findViewById<TextView>(R.id.Step5Amount)
        val deleteCardStep5 = itemView.findViewById<ImageView>(R.id.deleteCardStep5)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): step5Adapter.Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.step_5_layout_item, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: step5Adapter.Viewholder, position: Int) {
        holder.Step5Title.setText(list.get(position)?.earning_type)
        holder.Step5Amount.setText(list.get(position)?.value)

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

    fun setData(data: ArrayList<Step5Response>?) {
        list.clear()
        list.addAll(data!!)
        notifyDataSetChanged()
    }

}