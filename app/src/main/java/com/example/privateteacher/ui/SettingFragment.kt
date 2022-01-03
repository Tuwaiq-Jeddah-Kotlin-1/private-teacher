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
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.privateteacher.MainActivity
import com.example.privateteacher.R
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class SettingFragment : Fragment() {
    lateinit var signOut: TextView
    private lateinit var preferences: SharedPreferences
    private lateinit var changelanguage: TextView
    private lateinit var languageToggleButton: MaterialButtonToggleGroup
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
        preferences = requireContext().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        changelanguage = view.findViewById(R.id.tvChangeLanguage)
        signOut.setOnClickListener {
            preferences =
                this.requireActivity().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
            val emailPref = preferences.getString("EMAIL", "")
            val passwordPref = preferences.getString("PASSWORD", "")
            val editor: SharedPreferences.Editor = preferences.edit()
            editor.clear()
            editor.apply()

            editor.apply()
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_settingFragment_to_login)

        }

//        changelanguage.setOnClickListener {
//            showChangeLanguage()
//        }
        languageToggleButton.addOnButtonCheckedListener { ToggleButtonGroup, checkedId, isChecked ->
            if (isChecked) {
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


            }
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

    }



//            fun showChangeLanguage() {
//
//                val listItmes = arrayOf("عربي", "English")
//
//
//                val mBuilder = AlertDialog.Builder(this.requireContext())
//
//                mBuilder.setTitle("Choose Language")
//
//                mBuilder.setSingleChoiceItems(listItmes, -1) { dialog, which ->
//                    if (which == 0) {
//
//                        //setLocate("ar")
//                        setLocal("ar")
//
//                    } else if (which == 1) {
//
//                        setLocal("en")
//
//                    }
//
//                    dialog.dismiss()
//
//
//                }
//                val mDialog = mBuilder.create()
//
//                mDialog.show()
//
//
//            }


        }
