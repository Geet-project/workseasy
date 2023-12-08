package com.workseasy.com.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.hr.hrmanagement.R
import com.workseasy.com.ui.login.LoginActivity
import com.workseasy.com.utils.SharedPref

class SplashActivity : AppCompatActivity() {
    var sharedPref: SharedPref? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sharedPref = SharedPref(this)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler().postDelayed({
            if(sharedPref!!.getData(SharedPref.isLogin, false)==true)
            {
                val intent = Intent(this, com.workseasy.com.ui.HomeActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, 3000)
    }
}