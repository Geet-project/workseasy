package com.workseasy.com.ui.purchase.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hr.hrmanagement.R
import com.workseasy.com.ui.purchase.response.QuotationApprovalListDataItem
import com.workseasy.com.ui.purchase.response.QuotationApprovedListData
import com.workseasy.com.ui.purchase.response.QuotationData

class QuotationAdapter(context: Context
): RecyclerView.Adapter<QuotationAdapter.ViewHolder>() {
    var quotationList = ArrayList<QuotationApprovedListData?>()
    var prList = ArrayList<QuotationApprovedListData?>()
    var context = context
    var nestedAdapter: QuotationNestedAdapter? = null




    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {


        val nestedRecyclerView = itemView.findViewById<RecyclerView>(R.id.nestedRecyclerView)

    }
    override fun onCreateViewHolder (
        parent: ViewGroup,
        viewType: Int
    ): QuotationAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.quotation_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuotationAdapter.ViewHolder, position: Int) {
        nestedAdapter = QuotationNestedAdapter(context)
        nestedAdapter!!.setData(prList)
        holder.nestedRecyclerView.adapter = nestedAdapter
//        if(position==0) {
//            holder.head.text = "Vendor Name"
//            holder.head.setTypeface(null, Typeface.BOLD);
//
//            holder.vendorname.text = prList.get(position)?.vendor
//            holder.requiredDate.text = prList.get(position)?.delivery_time
//            holder.discValue.text = prList.get(position)?.disc
//            holder.qtyValue.text = prList.get(position)?.quantity
//            holder.rateValue.text = prList.get(position)?.rate
//            holder.gstValue.text = prList.get(position)?.gst
//            holder.fraightValue.text = prList.get(position)?.fraight
//            holder.orderValue.text = prList.get(position)?.order_value
//            holder.statusValue.text = prList.get(position)?.status
//
////            holder.vendorname.setText("Vendor - "+position)
////            holder.vendorname.setTypeface(null, Typeface.BOLD);
////
////            holder.requiredDate.setText("Vendor - "+(position+1))
////            holder.requiredDate.setTypeface(null, Typeface.BOLD);
////
////            holder.discValue.setText("Vendor - "+(position+2))
////            holder.discValue.setTypeface(null, Typeface.BOLD);
////
////            holder.qtyValue.setText("Vendor - "+(position+3))
////            holder.qtyValue.setTypeface(null, Typeface.BOLD);
////
////            holder.rateValue.setText("Vendor - "+(position+4))
////            holder.rateValue.setTypeface(null, Typeface.BOLD);
////
////            holder.gstValue.setText("Vendor - "+(position+5))
////            holder.gstValue.setTypeface(null, Typeface.BOLD);
////
////            holder.fraightValue.setText("Vendor - "+(position+6))
////            holder.fraightValue.setTypeface(null, Typeface.BOLD);
////
////            holder.orderValue.setText("Vendor - "+(position+7))
////            holder.orderValue.setTypeface(null, Typeface.BOLD);
////
////            holder.statusValue.setText("Vendor - "+(position+8))
////            holder.statusValue.setTypeface(null, Typeface.BOLD);
//
//        }else if(position==1) {
//            holder.head.text = "Required Date"
//            holder.head.setTypeface(null, Typeface.BOLD);
//
//            holder.vendorname.text = prList.get(position)?.vendor
//            holder.requiredDate.text = prList.get(position)?.delivery_time
//            holder.discValue.text = prList.get(position)?.disc
//            holder.qtyValue.text = prList.get(position)?.quantity
//            holder.rateValue.text = prList.get(position)?.rate
//            holder.gstValue.text = prList.get(position)?.gst
//            holder.fraightValue.text = prList.get(position)?.fraight
//            holder.orderValue.text = prList.get(position)?.order_value
//            holder.statusValue.text = prList.get(position)?.status
//        }else if(position==2) {
//            holder.head.text = "Disc"
//            holder.head.setTypeface(null, Typeface.BOLD);
//
//            holder.vendorname.text = prList.get(position)?.vendor
//            holder.requiredDate.text = prList.get(position)?.delivery_time
//            holder.discValue.text = prList.get(position)?.disc
//            holder.qtyValue.text = prList.get(position)?.quantity
//            holder.rateValue.text = prList.get(position)?.rate
//            holder.gstValue.text = prList.get(position)?.gst
//            holder.fraightValue.text = prList.get(position)?.fraight
//            holder.orderValue.text = prList.get(position)?.order_value
//            holder.statusValue.text = prList.get(position)?.status
//        }else if(position==3){
//            holder.head.text = "Qty"
//            holder.head.setTypeface(null, Typeface.BOLD);
//
//            holder.vendorname.text = prList.get(position)?.vendor
//            holder.requiredDate.text = prList.get(position)?.delivery_time
//            holder.discValue.text = prList.get(position)?.disc
//            holder.qtyValue.text = prList.get(position)?.quantity
//            holder.rateValue.text = prList.get(position)?.rate
//            holder.gstValue.text = prList.get(position)?.gst
//            holder.fraightValue.text = prList.get(position)?.fraight
//            holder.orderValue.text = prList.get(position)?.order_value
//            holder.statusValue.text = prList.get(position)?.status
//        }else if(position==4) {
//            holder.head.text = "Rate"
//            holder.head.setTypeface(null, Typeface.BOLD);
//
//            holder.vendorname.text = prList.get(position)?.vendor
//            holder.requiredDate.text = prList.get(position)?.delivery_time
//            holder.discValue.text = prList.get(position)?.disc
//            holder.qtyValue.text = prList.get(position)?.quantity
//            holder.rateValue.text = prList.get(position)?.rate
//            holder.gstValue.text = prList.get(position)?.gst
//            holder.fraightValue.text = prList.get(position)?.fraight
//            holder.orderValue.text = prList.get(position)?.order_value
//            holder.statusValue.text = prList.get(position)?.status
//        }else if(position==5){
//            holder.head.text = "GST"
//            holder.head.setTypeface(null, Typeface.BOLD);
//
//            holder.vendorname.text = prList.get(position)?.vendor
//            holder.requiredDate.text = prList.get(position)?.delivery_time
//            holder.discValue.text = prList.get(position)?.disc
//            holder.qtyValue.text = prList.get(position)?.quantity
//            holder.rateValue.text = prList.get(position)?.rate
//            holder.gstValue.text = prList.get(position)?.gst
//            holder.fraightValue.text = prList.get(position)?.fraight
//            holder.orderValue.text = prList.get(position)?.order_value
//            holder.statusValue.text = prList.get(position)?.status
//        }else if(position==6) {
//            holder.head.text = "Fraight"
//            holder.head.setTypeface(null, Typeface.BOLD);
//
//            holder.vendorname.text = prList.get(position)?.vendor
//            holder.requiredDate.text = prList.get(position)?.delivery_time
//            holder.discValue.text = prList.get(position)?.disc
//            holder.qtyValue.text = prList.get(position)?.quantity
//            holder.rateValue.text = prList.get(position)?.rate
//            holder.gstValue.text = prList.get(position)?.gst
//            holder.fraightValue.text = prList.get(position)?.fraight
//            holder.orderValue.text = prList.get(position)?.order_value
//            holder.statusValue.text = prList.get(position)?.status
//        }else if(position==7) {
//            holder.head.text = "Order Value"
//            holder.head.setTypeface(null, Typeface.BOLD);
//

//        }else if(position==8) {
//            holder.head.text = "Status"
//            holder.head.setTypeface(null, Typeface.BOLD);
//            holder.vendorname.text = prList.get(position)?.vendor
//            holder.requiredDate.text = prList.get(position)?.delivery_time
//            holder.discValue.text = prList.get(position)?.disc
//            holder.qtyValue.text = prList.get(position)?.quantity
//            holder.rateValue.text = prList.get(position)?.rate
//            holder.gstValue.text = prList.get(position)?.gst
//            holder.fraightValue.text = prList.get(position)?.fraight
//            holder.orderValue.text = prList.get(position)?.order_value
//            holder.statusValue.text = prList.get(position)?.status
//        }else {
//            holder.vendorname.text = prList.get(position)?.vendor
//            holder.requiredDate.text = prList.get(position)?.delivery_time
//            holder.discValue.text = prList.get(position)?.disc
//            holder.qtyValue.text = prList.get(position)?.quantity
//            holder.rateValue.text = prList.get(position)?.rate
//            holder.gstValue.text = prList.get(position)?.gst
//            holder.fraightValue.text = prList.get(position)?.fraight
//            holder.orderValue.text = prList.get(position)?.order_value
//            holder.statusValue.text = prList.get(position)?.status
//        }


    }

    override fun getItemCount(): Int {
        return 1
    }

    fun setData(data: ArrayList<QuotationApprovedListData?>) {
        prList.clear()
        prList.addAll(data!!)
        notifyDataSetChanged()
    }
}