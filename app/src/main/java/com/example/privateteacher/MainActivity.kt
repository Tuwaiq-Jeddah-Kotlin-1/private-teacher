package com.example.privateteacher

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val sharedPreferencesSettings = this.getSharedPreferences("SHARED_PREF", Activity.MODE_PRIVATE)
        val language = sharedPreferencesSettings.getString("language", "")
        if (language.toString() == "ar") {
            //Toast.makeText(this, " arabic",Toast.LENGTH_LONG).show()
            setLocate()

        } else {
            //Toast.makeText(this, "English", Toast.LENGTH_LONG).show()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController = findNavController(R.id.fragmentContainerView)
        bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.login -> bottom_navigation.visibility = View.GONE
                R.id.signupTeacher -> bottom_navigation.visibility = View.GONE
                R.id.signupStudent -> bottom_navigation.visibility = View.GONE
                else -> bottom_navigation.visibility = View.VISIBLE
            }
        }
    }




    private fun setLocate() {
        val locale = Locale("ar")

        Locale.setDefault(locale)

        val config = Configuration()

        config.locale = locale

        //---------------------------------------------------------------
        this?.resources?.updateConfiguration(config, this.resources.displayMetrics)

    }







}