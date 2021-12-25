package com.example.privateteacher.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.privateteacher.R
import com.google.firebase.auth.FirebaseAuth

class SettingFragment : Fragment() {
lateinit var signOut:TextView
    private lateinit var preferences: SharedPreferences

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
        signOut.setOnClickListener {
            val editor: SharedPreferences.Editor = preferences.edit().clear()
            editor.apply()
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_settingFragment_to_login)
        }

    }

}