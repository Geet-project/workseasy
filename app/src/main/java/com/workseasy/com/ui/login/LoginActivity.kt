package com.workseasy.com.ui.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hr.hrmanagement.R
import com.hr.hrmanagement.databinding.ActivityLoginBinding
import com.workseasy.com.ui.login.request.LoginRequest
import com.workseasy.com.utils.GenericTextWatcher
import com.workseasy.com.utils.SharedPref

class LoginActivity : AppCompatActivity() {
    lateinit var loginBinding: ActivityLoginBinding
    val viewModel: LoginViewModel by viewModels()
    lateinit var progressDialog: ProgressDialog
    var sharedPref: SharedPref? = null
    lateinit var e1: EditText
    lateinit var e2: EditText
    lateinit var e3: EditText
    lateinit var e4: EditText
    lateinit var e5: EditText
    lateinit var e6: EditText
    //    lateinit var PasswordEt:List<EditText>
    val mPin = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        progressDialog = ProgressDialog(this)
        sharedPref = SharedPref(this)
        e1 = loginBinding.mPin1
        e2 = loginBinding.mPin2
        e3 = loginBinding.mPin3
        e4 = loginBinding.mPin4
        e5 = loginBinding.mPin5
        e6 = loginBinding.mPin6
        setValidationListeners()
        setListners()
        responseHandle()
        numberotpmove()
    }
    fun setValidationListeners()
    {
        loginBinding.PhoneEt.addTextChangedListener(GenericTextWatcher(loginBinding.PhoneEt, null, loginBinding.phoneError))
    }

    fun setListners()
    {
        loginBinding.nextBtn.setOnClickListener {
            var mobile = loginBinding.PhoneEt.text.toString()
            val editext1 = e1.text.toString().trim { it <= ' ' }
            val editext2 = e2.text.toString().trim { it <= ' ' }
            val editext3 = e3.text.toString().trim { it <= ' ' }
            val editext4 = e4.text.toString().trim { it <= ' ' }
            val editext5 = e5.text.toString().trim { it <= ' ' }
            val editext6 = e6.text.toString().trim { it <= ' ' }
            var mpin = editext1+editext2+editext3+editext4+editext5+editext6
            if(mobile.equals(""))
            {
                loginBinding.PhoneEt.requestFocus();
                loginBinding.phoneError.text = "*Please enter Phone Number"
                loginBinding.phoneError.visibility = View.VISIBLE
            }else if(!com.workseasy.com.utils.Patterns.PHONEUMBER.matcher(mobile).matches())
            {
                loginBinding.PhoneEt.requestFocus();
                loginBinding.phoneError.text = "*Please enter valid Phone Number"
                loginBinding.phoneError.visibility = View.VISIBLE
            }else if(mpin.equals(""))
            {
                loginBinding.passwordError.text = "*Please enter mpin"
                loginBinding.passwordError.visibility = View.VISIBLE
            }else{
                callApi(mobile, mpin)
            }
        }
    }

    fun responseHandle()
    {
        viewModel.loginResponse.observe(this){ dataSate->
            when(dataSate)
            {
                is com.workseasy.com.network.DataState.Success ->{
                    if(dataSate.data.status==200)
                    {
                        sharedPref!!.saveData(SharedPref.isLogin, true)
                        sharedPref!!.saveData(SharedPref.TOKEN, dataSate.data.data.token)
                        sharedPref!!.saveData(SharedPref.USER_NAME, dataSate.data.data.name)
                        sharedPref!!.saveData(SharedPref.USER_ID, dataSate.data.data.user_id)
                        sharedPref!!.saveData(SharedPref.EMAIL_iD, dataSate.data.data.email)
                        sharedPref!!.saveData(SharedPref.IMAGE, dataSate.data.data.image)
                        sharedPref!!.saveData(SharedPref.LEAVE_TYPE, dataSate.data.data.leave_type)
                        sharedPref!!.saveData(SharedPref.SALARY_TYPE, dataSate.data.data.salary_type)
                        sharedPref!!.saveData(SharedPref.ATTENDANCE_TYPE, dataSate.data.data.attendance_type)


                        Toast.makeText(this, "Login Successfully!!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, com.workseasy.com.ui.HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        loginBinding.progressbar.visibility = View.GONE
                        Toast.makeText(this, dataSate.data.error, Toast.LENGTH_SHORT).show()
                    }
                }
                is com.workseasy.com.network.DataState.Loading -> {
                    loginBinding.progressbar.visibility = View.VISIBLE
                }

                is com.workseasy.com.network.DataState.Error -> {
                    loginBinding.progressbar.visibility = View.GONE
                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun callApi(mobile: String, mpin: String) {
        viewModel.managerLogin(LoginRequest( mobile, mpin), this)
    }
    private fun numberotpmove() {

        e1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) {
                    e2.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        e2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) {
                    e3.requestFocus()
                } else if (s?.length == 0) {
                    e1.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        e3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) {
                    e4.requestFocus()
                } else if (s?.length == 0) {
                    e2.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        e4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) {
                    e5.requestFocus()
                } else if (s?.length == 0) {
                    e3.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        e5.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) {
                    e6.requestFocus()
                } else if (s?.length == 0) {
                    e4.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        e6.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 0) {
                    e5.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}