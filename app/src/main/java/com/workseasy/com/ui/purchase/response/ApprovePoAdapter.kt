package com.workseasy.com.ui.purchase.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hr.hrmanagement.R
import com.workseasy.com.ui.purchase.response.PoDataItem
import com.workseasy.com.ui.purchase.response.Pr
import com.workseasy.com.ui.purchase.response.PrData

class ApprovePoAdapter (context: Context, var dataClicked: ApprovePoAdapter.DataClicked? = null) : RecyclerView.Adapter<ApprovePoAdapter.PrViewHolder>(){
    var prList = ArrayList<PoDataItem>()
    var context = context

    inner class PrViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val poIdValue = itemView.findViewById<TextView>(R.id.poIdValue)
        val vendorNameValue = itemView.findViewById<TextView>(R.id.vendorNameValue)
        val dateValue = itemView.findViewById<TextView>(R.id.dateValue)
        val statusValue = itemView.findViewById<TextView>(R.id.statusValue)
        val approvebtn = itemView.findViewById<Button>(R.id.approvebtn)
        val rejectbtn = itemView.findViewById<Button>(R.id.rejectbtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.approve_layout, parent, false)
        return PrViewHolder(view)
    }

    override fun onBindViewHolder(holder: PrViewHolder, position: Int) {
        holder.poIdValue.text = prList.get(position)?.po_id
        holder.vendorNameValue.text = prList.get(position)?.vendor_name
        holder.dateValue.text = prList.get(position)?.created_at
        holder.statusValue.text = prList.get(position)?.status

        holder.itemView.setOnClickListener {
            dataClicked?.itemClicked(prList, position, prList.get(position).id)
        }
    }
    fun setData(data: ArrayList<PoDataItem>?) {
        prList.clear()
        prList.addAll(data!!)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return prList.size
    }
    interface DataClicked {
        fun itemClicked(data: ArrayList<PoDataItem>?, index : Int?, prid:Int?)
    }

}