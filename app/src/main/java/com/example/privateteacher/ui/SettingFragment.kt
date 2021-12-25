package com.example.privateteacher.ui

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
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class SettingFragment : Fragment() {
lateinit var signOut:TextView
    private lateinit var preferences: SharedPreferences
private lateinit var changelanguage:TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signOut= view.findViewById(R.id.signOut)
        preferences = requireContext().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        changelanguage=view.findViewById(R.id.tvChangeLanguage)
        signOut.setOnClickListener {
            preferences = this.requireActivity().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
            val emailPref = preferences.getString("EMAIL", "")
            val passwordPref = preferences.getString("PASSWORD", "")
            val editor: SharedPreferences.Editor = preferences.edit()
            editor.clear()
            editor.apply()

            editor.apply()
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_settingFragment_to_login)

        }

changelanguage.setOnClickListener {
    showChangeLanguage()
}
    }



    private fun setLocaleKoala(localeName: String) {

        val locale = Locale(localeName.toString())

        Locale.setDefault(locale)

        val config = Configuration()

        config.locale = locale

        //---------------------------------------------------------------
        context?.resources?.updateConfiguration(config, requireContext().resources.displayMetrics)


        preferences = this.requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)

        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putString("language", "${locale.toString()}")
        editor.apply()


        val refresh = Intent(context, MainActivity::class.java)
        //    refresh.putExtra("currentLang", localeName)
        startActivity(refresh)
    }

    private fun showChangeLanguage(){

        val listItmes = arrayOf("عربي","English")


        val mBuilder = AlertDialog.Builder(this.requireContext())

        mBuilder.setTitle("Choose Language")

        mBuilder.setSingleChoiceItems(listItmes,-1){
                dialog, which ->
            if (which ==0){

                //setLocate("ar")
                setLocaleKoala("ar")

            }else if (which==1){

                setLocaleKoala("en")

            }

            dialog.dismiss()


        }
        val mDialog =mBuilder.create()

        mDialog.show()


    }











}