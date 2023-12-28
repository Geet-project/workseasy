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

class QuotationVendorNameAdapter(
    context: Context
): RecyclerView.Adapter<QuotationVendorNameAdapter.ViewHolder>() {

    var quotationList= ArrayList<QuotationApprovedListData?>()
    var context = context
    var dialog: AlertDialog? = null
    var sharedPref: SharedPref? = null


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val head = itemView.findViewById<TextView>(R.id.headValue)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotationVendorNameAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.quotation_vendorname_item, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return quotationList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position!=0)
            holder.head.setText("Vendor - "+(position+1).toString())
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