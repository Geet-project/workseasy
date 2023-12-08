package com.workseasy.com.ui.notification.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hr.hrmanagement.R
import com.workseasy.com.ui.notification.Notification

class NotificationAdapter(context: Context)  : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    var notiList = ArrayList<Notification?>()
    var context = context
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val notiname = itemView.findViewById<TextView>(R.id.notiname)
        val notiText = itemView.findViewById<TextView>(R.id.notiText)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.noti_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationAdapter.ViewHolder, position: Int) {
        holder.notiname.text = notiList.get(position)?.title
        holder.notiText.text = notiList.get(position)?.body

    }

    override fun getItemCount(): Int {
        return notiList.size
    }

    fun setData(data: ArrayList<Notification>?) {
        notiList.clear()
        notiList.addAll(data!!)
        notifyDataSetChanged()
    }
}