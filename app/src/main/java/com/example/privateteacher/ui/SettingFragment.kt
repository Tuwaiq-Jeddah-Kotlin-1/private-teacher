package com.example.privateteacher.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.privateteacher.R

class SettingFragment : Fragment() {
lateinit var profile:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profile= view.findViewById(R.id.tvprofile)
        profile.setOnClickListener {
            //findNavController().navigate(R.id.action_settingFragment_to_upDate_Teatchr)
        }
    }

}