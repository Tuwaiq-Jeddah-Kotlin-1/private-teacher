package com.example.privateteacher.ui

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import com.example.privateteacher.MainActivity
import com.example.privateteacher.R

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val sharedPreferencesSettings = this.getSharedPreferences("SHARED_PREF", Activity.MODE_PRIVATE)
        val theme=sharedPreferencesSettings.getString("theme","")

        val mode = resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)

        if (theme.toString()=="dark") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            if (mode == Configuration.UI_MODE_NIGHT_YES) {
                supportActionBar?.hide()
                Handler(Looper.getMainLooper()).postDelayed({
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                    finish()
                }, 2000)
            }
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            supportActionBar?.hide()
            Handler(Looper.getMainLooper()).postDelayed({
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                finish()
            }, 2000)
        }


    }
}