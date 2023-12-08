package com.workseasy.com.ui.purchase.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hr.hrmanagement.R
import com.workseasy.com.ui.purchase.response.QuotationApprovedListData
import com.workseasy.com.ui.purchase.response.QuotationData

class QuotationAdapter(context: Context): RecyclerView.Adapter<QuotationAdapter.ViewHolder>() {
    var prList = ArrayList<QuotationApprovedListData?>()
    var context = context
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val head = itemView.findViewById<TextView>(R.id.headValue)
        val vendorname = itemView.findViewById<TextView>(R.id.vendorName)
        val requiredDate = itemView.findViewById<TextView>(R.id.requiredDate)
        val discValue = itemView.findViewById<TextView>(R.id.discValue)
        val qtyValue = itemView.findViewById<TextView>(R.id.qtyValue)
        val rateValue = itemView.findViewById<TextView>(R.id.rateValue)
        val gstValue = itemView.findViewById<TextView>(R.id.gstValue)
        val fraightValue = itemView.findViewById<TextView>(R.id.fraightValue)
        val orderValue = itemView.findViewById<TextView>(R.id.orderValue)
        val statusValue = itemView.findViewById<TextView>(R.id.statusValue)
    }
    override fun onCreateViewHolder (
        parent: ViewGroup,
        viewType: Int
    ): QuotationAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.quotation_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuotationAdapter.ViewHolder, position: Int) {
        if(position==0) {
            holder.head.text = ""
            holder.head.setTypeface(null, Typeface.BOLD);
            holder.vendorname.text = "Vendor Name"
            holder.vendorname.setTypeface(null, Typeface.BOLD);
            holder.requiredDate.text = "Required Date"
            holder.requiredDate.setTypeface(null, Typeface.BOLD);
            holder.discValue.text = "Disc"
            holder.discValue.setTypeface(null, Typeface.BOLD);
            holder.qtyValue.text = "Qty"
            holder.qtyValue.setTypeface(null, Typeface.BOLD);
            holder.rateValue.text = "Rate"
            holder.rateValue.setTypeface(null, Typeface.BOLD);
            holder.gstValue.text = "GST"
            holder.gstValue.setTypeface(null, Typeface.BOLD);
            holder.fraightValue.text = "Fraight"
            holder.fraightValue.setTypeface(null, Typeface.BOLD);
            holder.orderValue.text = "Order Value"
            holder.orderValue.setTypeface(null, Typeface.BOLD);
            holder.statusValue.text = "Status"
            holder.statusValue.setTypeface(null, Typeface.BOLD);
        }else{
            holder.head.text = "Vendor -"+ position.toString()
            holder.vendorname.text = prList.get(position)?.vendor
            holder.requiredDate.text = prList.get(position)?.delivery_time
            holder.discValue.text = prList.get(position)?.disc
            holder.qtyValue.text = prList.get(position)?.quantity
            holder.rateValue.text = prList.get(position)?.rate
            holder.gstValue.text = prList.get(position)?.gst
            holder.fraightValue.text = prList.get(position)?.fraight
            holder.orderValue.text = prList.get(position)?.order_value
            holder.statusValue.text = prList.get(position)?.status
        }

    }

    override fun getItemCount(): Int {
        return prList.size
    }

    fun setData(data: ArrayList<QuotationApprovedListData>?) {
        prList.clear()
        prList.addAll(data!!)
        notifyDataSetChanged()
    }
}