package com.workseasy.com.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.hr.hrmanagement.R
import java.util.regex.Pattern


class GenericTextWatcher(val edittext: EditText, val edittext2: EditText?,val textview: TextView) : TextWatcher{
    override fun afterTextChanged(p0: Editable?) {
        val text = p0.toString()
        val text1 = edittext2?.text.toString()
        when(edittext.id)
        {

            R.id.EmailEt -> if(text.equals("")) {
                edittext.requestFocus()
                textview.text = "*Please enter email"
                textview.visibility = View.VISIBLE
            } else if(!com.workseasy.com.utils.Patterns.EMAIL_ADDRESS.matcher(text).matches())
            {
                edittext.requestFocus()
                textview.text = "*Please enter valid email"
                textview.visibility = View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

//            R.id.PasswordEt -> if(text.equals(""))
//            {
//                edittext.requestFocus()
//                textview.text = "*Please enter password"
//                textview.visibility = View.VISIBLE
//            }else{
//                textview.visibility = View.GONE
//            }

            R.id.oldpassEt -> if(text.equals(""))
            {
                edittext.requestFocus()
                textview.text = "*Please enter password"
                textview.visibility = View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.newpassEt -> if(text.equals(""))
            {
                edittext.requestFocus()
                textview.text = "*Please enter password"
                textview.visibility = View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.cpasset -> if(!text.equals(text1))
            {
                edittext.requestFocus()
                textview.text = "*Passwords didn't match"
                textview.visibility = View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.oldMpinEt -> if(text.equals(""))
            {
                edittext.requestFocus()
                textview.text = "*Please enter old pin"
                textview.visibility = View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.newpinEt -> if(text.equals(""))
            {
                edittext.requestFocus()
                textview.text = "*Please enter new pin."
                textview.visibility = View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }


            R.id.cMPin -> if(!text1.equals(text))
            {
                edittext.requestFocus()
                textview.text = "*Pins didn't match."
                textview.visibility = View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }


            R.id.PhoneEt -> if(text.equals(""))
            {
                edittext.requestFocus();
                textview.text = "*Please enter Phone Number"
                textview.visibility = View.VISIBLE
            }else if(!com.workseasy.com.utils.Patterns.PHONEUMBER.matcher(text).matches()){
                edittext.requestFocus();
                textview.text = "*Please Enter Valid Phone Number"
                textview.visibility = View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.NameEt -> if(text.equals(""))
            {
                edittext.requestFocus();
                textview.text = "*Please enter your Name"
                textview.visibility = View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.FatherNameET -> if(text.equals(""))
            {
                edittext.requestFocus();
                textview.text = "*Please enter Father/Husband Name"
                textview.visibility = View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.AdhaarEt -> if(text.equals(""))
            {
                edittext.requestFocus();
                textview.text = "*Please enter Adhaar Number"
                textview.visibility = View.VISIBLE
            }else if(!com.workseasy.com.utils.Patterns.ADHAR_NUMBER.matcher(text).matches()){
                edittext.requestFocus();
                textview.text = "*Please Enter Valid Adhaar Number"
                textview.visibility = View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.adharEt -> if(text.equals(""))
            {
                edittext.requestFocus();
                textview.text = "*Please enter Adhaar Number"
                textview.visibility = View.VISIBLE
            }else if(text.startsWith(" ")){
                edittext.setText(text.trim())
            }
            else if(!com.workseasy.com.utils.Patterns.ADHAR_NUMBER.matcher(text).matches()){
                edittext.requestFocus();
                textview.text = "*Please Enter Valid Adhaar Number"
                textview.visibility = View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.amtRequiredEt -> if(text.equals(""))
            {
                edittext.requestFocus();
                textview.text = "*Please enter Required Amount"
                textview.visibility = View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.advReasonEt -> if(text.equals(""))
            {
                edittext.requestFocus();
                textview.text = "*Please enter reason of advance"
                textview.visibility = View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.MaxDeductionEt -> if(text.equals(""))
            {
                edittext.requestFocus()
                textview.text = "*Please enter maximum deduction."
                textview.visibility= View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.wdEt -> if(text.equals(""))
            {
                edittext.requestFocus()
                textview.text = "*Please enter wd."
                textview.visibility= View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.OtET -> if(text.equals(""))
            {
                edittext.requestFocus()
                textview.text = "*Please enter OT."
                textview.visibility= View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }


            R.id.elET -> if(text.equals(""))
            {
                edittext.requestFocus()
                textview.text = "*Please enter EL."
                textview.visibility= View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }
            R.id.elET -> if(text.equals(""))
            {
                edittext.requestFocus()
                textview.text = "*Please enter EL."
                textview.visibility= View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.clET -> if(text.equals(""))
            {
                edittext.requestFocus()
                textview.text = "*Please enter CL."
                textview.visibility= View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.slET -> if(text.equals(""))
            {
                edittext.requestFocus()
                textview.text = "*Please enter SL."
                textview.visibility= View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.HdET -> if(text.equals(""))
            {
                edittext.requestFocus()
                textview.text = "*Please enter HD."
                textview.visibility= View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.wdEt -> if(text.equals(""))
            {
                edittext.requestFocus()
                textview.text = "*Please enter WD."
                textview.visibility= View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.PdET -> if(text.equals(""))
            {
                edittext.requestFocus()
                textview.text = "*Please enter PD."
                textview.visibility= View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.woEt -> if(text.equals(""))
            {
                edittext.requestFocus()
                textview.text = "*Please enter WO."
                textview.visibility= View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.QtyEt -> if(text.equals(""))
            {
                edittext.requestFocus()
                textview.text = "*Please enter Quantity."
                textview.visibility= View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.UnitEt -> if(text.equals(""))
            {
                edittext.requestFocus()
                textview.text = "*Please enter Unit."
                textview.visibility= View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.DeliveryEt -> if(text.equals(""))
            {
                edittext.requestFocus()
                textview.text = "*Please enter Delivery."
                textview.visibility= View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.RemarkEt -> if(text.equals(""))
            {
                edittext.requestFocus()
                textview.text = "*Please enter Remark."
                textview.visibility= View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.remarksEt -> if(text.equals(""))
            {
                edittext.requestFocus()
                textview.text = "*Please enter Remark."
                textview.visibility= View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.BillEt -> if(text.equals(""))
            {
                edittext.requestFocus()
                textview.text = "*Please enter Bill No."
                textview.visibility= View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.BeneficiaryEt -> if(text.equals(""))
            {
                edittext.requestFocus()
                textview.text = "*Please enter Beneficiary Name"
                textview.visibility= View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }
            R.id.EmpCodeEt -> if(text.equals(""))
            {
                edittext.requestFocus()
                textview.text = "*Please enter Employee Code."
                textview.visibility= View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.PaymentHeadEt -> if(text.equals(""))
            {
                edittext.requestFocus()
                textview.text = "*Please enter Payment Head. "
                textview.visibility= View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.PaymentDateEt -> if(text.equals(""))
            {
                edittext.requestFocus()
                textview.text = "*Please enter Payment Date."
                textview.visibility= View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.TransactionEt -> if(text.equals(""))
            {
                edittext.requestFocus()
                textview.text = "*Please enter Transaction Referencen."
                textview.visibility= View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.LeaveReasonEt -> if(text.equals(""))
            {
                edittext.requestFocus()
                textview.text = "*Please enter leave Reason"
                textview.visibility= View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }

            R.id.ifscEt -> if(!isIFSCValid(text)){
                edittext.requestFocus()
                textview.text = "*Please enter valid ifsc code"
                textview.visibility= View.VISIBLE
            }else{
                textview.visibility = View.GONE
            }
        }

    }
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }


    companion object{
        fun isIFSCValid(email: String): Boolean {
            val expression = "^[A-Z]{4}[0][A-Z0-9]{6}$"

            val inputStr: CharSequence = email
            val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(inputStr)
            return if (matcher.matches()) {
                true
            } else {
                false
            }
        }

    }


}