package com.workseasy.com.ui.purchase.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.hr.hrmanagement.R
import com.workseasy.com.ui.hradmin.employeeDetails.adapter.HomeAdapter
import com.workseasy.com.ui.login.LoginViewModel
import com.workseasy.com.ui.login.response.DataX
import com.workseasy.com.ui.purchase.response.QuotationApprovalListDataItem
import com.workseasy.com.utils.SharedPref
import de.hdodenhof.circleimageview.CircleImageView

class QuotationApprovalListAdapter(context: Context,
                                   var dataClicked: QuotationApprovalListAdapter.OnClickListener? = null
): RecyclerView.Adapter<QuotationApprovalListAdapter.ViewHolder>() {

    var quotationList = ArrayList<QuotationApprovalListDataItem?>()
    var context = context
    var dialog: AlertDialog? = null
    var sharedPref: SharedPref? = null


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val vandorName = itemView.findViewById<TextView>(R.id.vendorNameValue)
        val reqDateValue = itemView.findViewById<TextView>(R.id.reqDateValue)
        val deliveryDateValue = itemView.findViewById<TextView>(R.id.deliveryDateValue)
        val discValue = itemView.findViewById<TextView>(R.id.discValue)
        val qtyValue = itemView.findViewById<TextView>(R.id.qtyValue)
        val rateValue = itemView.findViewById<TextView>(R.id.rateValue)
        val gstValue = itemView.findViewById<TextView>(R.id.gstValue)
        val fraightValue = itemView.findViewById<TextView>(R.id.fraightValue)
        val orderValue = itemView.findViewById<TextView>(R.id.orderValue)
        val statusValue = itemView.findViewById<TextView>(R.id.statusValue)


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotationApprovalListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.quotation_list_layout, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return quotationList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        holder.vandorName.setText("Vendor Name:  "+quotationList.get(position)!!.name)
        holder.discValue.setText("Disc:  "+quotationList.get(position)!!.disc)
        holder.qtyValue.setText("Qty:  "+quotationList.get(position)!!.quantity)
        holder.rateValue.setText("Rate:  "+quotationList.get(position)!!.rate)
        holder.gstValue.setText("GST:  "+quotationList.get(position)!!.gst)
        holder.fraightValue.setText("Fraight:  "+quotationList.get(position)!!.fraight)
        holder.orderValue.setText("Order Value:  "+quotationList.get(position)!!.order_value)
        holder.statusValue.setText("Status:  "+quotationList.get(position)!!.status)
        holder.deliveryDateValue.setText("Delivery Date:  "+quotationList.get(position)!!.delivery_time)

        holder.reqDateValue.setText("Required Date:  "+quotationList.get(position)!!.delivery)
        holder.itemView.setOnClickListener {
            dataClicked!!.onClick()
        }


    }

    fun setData(data: ArrayList<QuotationApprovalListDataItem>?) {
        quotationList.clear()
        quotationList.addAll(data!!)
        notifyDataSetChanged()
    }

    interface OnClickListener {
        fun onClick()
    }
}