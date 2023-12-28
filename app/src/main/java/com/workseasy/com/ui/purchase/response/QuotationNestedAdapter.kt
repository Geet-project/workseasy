package com.workseasy.com.ui.purchase.adapter

import android.content.Context
import android.graphics.Typeface
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
import com.workseasy.com.ui.purchase.response.QuotationApprovedListData
import com.workseasy.com.utils.SharedPref
import de.hdodenhof.circleimageview.CircleImageView

class QuotationNestedAdapter(
    context: Context
): RecyclerView.Adapter<QuotationNestedAdapter.ViewHolder>() {

    var quotationList= ArrayList<QuotationApprovedListData?>()
    var context = context
    var dialog: AlertDialog? = null
    var sharedPref: SharedPref? = null


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotationNestedAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.quotation_nested_listitem, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return quotationList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position==0) {
            holder.vendorname.setText("Vendor Name")
            holder.vendorname.setTypeface(null, Typeface.BOLD);

            holder.requiredDate.setText("Required Date")
            holder.requiredDate.setTypeface(null, Typeface.BOLD);

            holder.discValue.setText("Disc")
            holder.discValue.setTypeface(null, Typeface.BOLD);

            holder.qtyValue.setText("Qty")
            holder.qtyValue.setTypeface(null, Typeface.BOLD);

            holder.rateValue.setText("Rate")
            holder.rateValue.setTypeface(null, Typeface.BOLD);

            holder.gstValue.setText("GST")
            holder.gstValue.setTypeface(null, Typeface.BOLD);

            holder.fraightValue.setText("Fraight")
            holder.fraightValue.setTypeface(null, Typeface.BOLD);

            holder.orderValue.setText("Order Value")
            holder.orderValue.setTypeface(null, Typeface.BOLD);

            holder.statusValue.setText("Status")
            holder.statusValue.setTypeface(null, Typeface.BOLD);

        }
        else{
            holder.vendorname.text = quotationList.get(position)?.vendor
            holder.requiredDate.text = quotationList.get(position)?.delivery_time
            holder.discValue.text = quotationList.get(position)?.disc
            holder.qtyValue.text = quotationList.get(position)?.quantity
            holder.rateValue.text = quotationList.get(position)?.rate
            holder.gstValue.text = quotationList.get(position)?.gst
            holder.fraightValue.text = quotationList.get(position)?.fraight
            holder.orderValue.text = quotationList.get(position)?.order_value
            holder.statusValue.text = quotationList.get(position)?.status
        }
    }

    fun setData(data: ArrayList<QuotationApprovedListData?>) {
        quotationList.clear()
        quotationList.addAll(data!!)
        notifyDataSetChanged()
    }

    interface OnClickListener {
        fun onClick()
    }
}