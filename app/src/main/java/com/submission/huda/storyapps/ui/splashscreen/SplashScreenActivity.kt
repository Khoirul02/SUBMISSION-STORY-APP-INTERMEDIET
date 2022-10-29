package com.submission.huda.storyapps.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.submission.huda.storyapps.R
import com.submission.huda.storyapps.helper.Config
import com.submission.huda.storyapps.ui.dashboard.DashboardActivity
import com.submission.huda.storyapps.ui.login.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val actionBar = supportActionBar
        actionBar!!.hide()
        sharedPreferences = getSharedPreferences(Config.SHARED_PRED_NAME, Context.MODE_PRIVATE)
        val checking = sharedPreferences.getString(Config.USERID,"")
        val background = object : Thread(){
            override fun run() {
                try {
                    sleep(2000)
                    if (checking === ""){
                        val intent = Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intent)
                    } else {
                        val intent = Intent(applicationContext, DashboardActivity::class.java)
                        startActivity(intent)
                    }
                } catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }
}