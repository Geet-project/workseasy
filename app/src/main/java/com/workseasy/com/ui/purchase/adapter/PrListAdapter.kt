package com.workseasy.com.ui.purchase.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hr.hrmanagement.R
import com.workseasy.com.ui.purchase.response.Pr
import com.workseasy.com.ui.purchase.response.PrData

class PrListAdapter (context: Context, var dataClicked: PrListAdapter.DataClicked? = null) : RecyclerView.Adapter<PrListAdapter.PrViewHolder>(){
    var prList = ArrayList<PrData>()
    var context = context

    inner class PrViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val prvalue = itemView.findViewById<TextView>(R.id.prnovalue)
        val makeValue = itemView.findViewById<TextView>(R.id.makeValue)
        val itemValue = itemView.findViewById<TextView>(R.id.itemvalue)
        val qtyValue = itemView.findViewById<TextView>(R.id.qtyvalue)
        val reqbydateValue = itemView.findViewById<TextView>(R.id.requestedbyvalue)
        val prdateValue = itemView.findViewById<TextView>(R.id.prdateValue)
        val prstatus = itemView.findViewById<TextView>(R.id.prstatusValue);

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pr_list_layout, parent, false)
        return PrViewHolder(view)
    }

    override fun onBindViewHolder(holder: PrViewHolder, position: Int) {
        holder.prvalue.text = prList.get(position)?.pr_code
        holder.itemValue.text = prList.get(position)?.item
        holder.qtyValue.text = prList.get(position)?.quantity
        holder.makeValue.text = prList.get(position)?.make
        holder.reqbydateValue.text = prList.get(position)?.name
        holder.itemView.setOnClickListener {
            dataClicked?.itemClicked(prList, position, prList.get(position).id)
        }
        holder.prdateValue.text = prList.get(position).created_at
        holder.prstatus.text = prList.get(position).status
    }
    fun setData(data: ArrayList<PrData>?) {
        prList.clear()
        prList.addAll(data!!)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return prList.size
    }
    interface DataClicked {
        fun itemClicked(data: ArrayList<PrData>?, index : Int?, prid:Int?)
    }

}