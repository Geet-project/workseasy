package com.workseasy.com.ui.hradmin.employeelist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hr.hrmanagement.R
import com.squareup.picasso.Picasso
import com.workseasy.com.ui.hradmin.employeeDetails.response.Employee
import de.hdodenhof.circleimageview.CircleImageView

class EmployeeAdapter(context: Context,
                      var dataClicked: DataClicked? = null)
    : RecyclerView.Adapter<EmployeeAdapter.EmpVieHolder>() {
    var userList = ArrayList<com.workseasy.com.ui.hradmin.employeelist.response.Employee?>()
    var context = context
    val dataClick = dataClicked



    inner class EmpVieHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val profilePhoto = itemView.findViewById<CircleImageView>(R.id.empProfilePhoto)
        val empName = itemView.findViewById<TextView>(R.id.empName)
        val empDesignation = itemView.findViewById<TextView>(R.id.empDesignation)
        val empDepartment = itemView.findViewById<TextView>(R.id.empDepartment)
        val payCode = itemView.findViewById<TextView>(R.id.payCode)
        val empNumber = itemView.findViewById<TextView>(R.id.empNumber)
        val empCode = itemView.findViewById<TextView>(R.id.empCode)

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EmployeeAdapter.EmpVieHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_emplist, parent, false)
        return EmpVieHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeeAdapter.EmpVieHolder, position: Int) {
        if(userList.get(position)?.photo.equals("") || userList.get(position)?.photo==null){
            holder.profilePhoto.setImageResource(R.drawable.profilephoto)
        }else{
            Picasso.get().load(userList.get(position)?.photo).into(holder.profilePhoto)
        }
        holder.empName.setText(userList.get(position)?.name)
        holder.payCode.setText("Pay Code: "+userList.get(position)?.payCode)
        if(userList.get(position)?.department.equals("")|| userList.get(position)?.department==null){
            holder.empDepartment.visibility = View.GONE
        }else{
            holder.empDepartment.setText(userList.get(position)?.designation.toString())
            holder.empDepartment.visibility = View.VISIBLE
        }


        if(userList.get(position)?.designation.equals("")|| userList.get(position)?.designation==null){
            holder.empDesignation.visibility = View.GONE
        }else{
            holder.empDesignation.setText(userList.get(position)?.designation.toString())

            holder.empDesignation.visibility = View.VISIBLE

        }

        if(userList.get(position)?.designation.equals("")|| userList.get(position)?.designation==null){
            holder.empDesignation.visibility = View.GONE
        }else{
            holder.empDesignation.setText(userList.get(position)?.designation.toString())

            holder.empDesignation.visibility = View.GONE

        }

        if(userList.get(position)?.designation.equals("")|| userList.get(position)?.designation==null){
            holder.empDesignation.visibility = View.GONE
        }else{
            holder.empDesignation.setText(userList.get(position)?.designation.toString())

            holder.empDesignation.visibility = View.GONE

        }
        holder.empCode.setText(userList.get(position)?.employee_code.toString())
        if(userList.get(position)?.mob_no.equals("")|| userList.get(position)?.mob_no==null) {
            holder.empNumber.visibility = View.GONE
        }else{
            holder.empNumber.setText(userList.get(position)?.mob_no)
            holder.empNumber.visibility = View.VISIBLE
        }
        holder.itemView.setOnClickListener {
            dataClick!!.itemClicked(userList.get(position)?.name,
            userList.get(position)?.payCode, userList.get(position)?.id, userList.get(position)?.form_type,
            userList.get(position)?.photo, userList.get(position)?.mob_no,userList.get(position)?.designation,
                userList.get(position)?.department
            )
        }


    }

    override fun getItemCount(): Int {
       return userList.size
    }

    fun setData(data: ArrayList<com.workseasy.com.ui.hradmin.employeelist.response.Employee>?) {
        userList.clear()
        userList.addAll(data!!)
        notifyDataSetChanged()
    }

    interface DataClicked {
        fun itemClicked(empName: String?,empCode: String?, empId: Int?, form_type: Int?,
        photo: String?, mobile: String?, designation: String?, department: String?)
    }
}