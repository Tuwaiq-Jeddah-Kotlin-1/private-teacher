package com.example.privateteacher.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.privateteacher.MainActivity
import com.example.privateteacher.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_setting.*
import java.util.*

class SettingFragment : Fragment() {
    lateinit var signOut: TextView
    lateinit var englishBtn: MaterialButton
    lateinit var arabicBtn: MaterialButton
    lateinit var Dark: MaterialButton
    lateinit var Light: MaterialButton
    private lateinit var preferences: SharedPreferences
    private lateinit var changelanguage: TextView
    private lateinit var languageToggleButton: MaterialButtonToggleGroup
    private lateinit var themeToggleBtn:MaterialButtonToggleGroup
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        languageToggleButton = view.findViewById(R.id.LanguageToggleButton)
        signOut = view.findViewById(R.id.signOut)
        preferences = requireContext().getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        changelanguage = view.findViewById(R.id.tvChangeLanguage)
        themeToggleBtn=view.findViewById(R.id.Theme)
        Light=view.findViewById(R.id.Light)
        Dark=view.findViewById(R.id.Dark)
        englishBtn=view.findViewById(R.id.englishBtn)
        arabicBtn=view.findViewById(R.id.arabicBtn)
        signOut.setOnClickListener {
            val emailPref = preferences.getString("EMAIL", "")
            val passwordPref = preferences.getString("PASSWORD", "")
            val editor: SharedPreferences.Editor = preferences.edit()
            editor.clear()
          //  editor.apply()

            editor.apply()
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_settingFragment_to_login)
        }


        if (preferences.getString("theme","dark")=="dark"){
            Dark.isChecked=true
        }else{
            Light.isChecked=true

        }
        if (preferences.getString("LOCALE","en")=="en"){
englishBtn.isChecked=true
        }else{
arabicBtn.isChecked=true
        }

        themeToggleBtn.addOnButtonCheckedListener { group, checkedId, isChecked ->
//            if (isChecked){
                when(checkedId){
                    R.id.Dark ->activity?.let {
                        preferences.edit().putString("theme","dark").apply()
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    R.id.Light->activity?.let {
                        preferences.edit().putString("theme","light").apply()
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


                    }
              //  }
            }
        }
        languageToggleButton.addOnButtonCheckedListener { ToggleButtonGroup, checkedId, isChecked ->
//            if (isChecked) {
                when (checkedId) {
                    R.id.arabicBtn -> activity?.let {
                        preferences.edit().putString("LOCALE", "ar").apply()
                        setLocate(it, "ar")
                    }
                    R.id.englishBtn -> activity?.let {
                        preferences.edit().putString("LOCALE", "en").apply()
                        setLocate(it, "en")
                    }
                }
//            }
        }
    }

    fun setLocate(activity: Activity, Lang: String) {
        val locale = Locale(Lang)
        Locale.setDefault(locale)
        val resources = activity.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        startActivity(Intent(activity, MainActivity::class.java))
       activity.finish()
     //   recreate(context as Activity)


    }
        }
