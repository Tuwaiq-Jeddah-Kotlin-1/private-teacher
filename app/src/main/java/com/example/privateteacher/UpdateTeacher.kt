package com.example.privateteacher

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.privateteacher.ui.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class UpdateTeacher : Fragment() {
    private lateinit var db: FirebaseFirestore


    lateinit var UpdateTeacher: Button
    lateinit var name: EditText
    private lateinit var subject: EditText
    private lateinit var level: EditText
    private lateinit var sTime: TextView
    private lateinit var eTime: TextView
    private lateinit var major:EditText
    private lateinit var phoneNumber: EditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_up_date__teatchr, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        UpdateTeacher = view.findViewById(R.id.UpdateTeacher)
        name = view.findViewById(R.id.TeacherUserName)
        subject = view.findViewById(R.id.subject)
        level = view.findViewById(R.id.level)
        sTime = view.findViewById(R.id.startTime)
        eTime = view.findViewById(R.id.endTime)
        phoneNumber = view.findViewById(R.id.phoneNumber)
        major=view.findViewById(R.id.major)
        db = FirebaseFirestore.getInstance()
        val preference = requireContext().getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)

        var pName = preference.getString(NAME, "")
        var pSubject = preference.getString(SUBJECT, "")
        var pLevel = preference.getString(LEVEL, "")
        var pSTime = preference.getInt(START_TIME, 0)
        var pETime = preference.getInt(END_TIME, 0)
        var pPhoneNumber = preference.getString(PHONE_NUMBER, "")
        var pMajor=preference.getString(MAJOR,"")

        name.hint = pName
        subject.hint = pSubject
        level.hint = pLevel
        sTime.hint = pSTime.toString()
        eTime.hint = pETime.toString()
        phoneNumber.hint = pPhoneNumber
        major.hint=pMajor


        name.isEnabled = false
        subject.isEnabled = false
        level.isEnabled = false
        sTime.isEnabled = false
        eTime.isEnabled = false
        phoneNumber.isEnabled = false
        major.isEnabled=false
        UpdateTeacher.setOnClickListener {
            name.isEnabled = true
            subject.isEnabled = true
            level.isEnabled = true
            sTime.isEnabled = true
            eTime.isEnabled = true
            phoneNumber.isEnabled = true
            major.isEnabled=true

            if (name.isEnabled) {
                UpdateTeacher.text = "Submit"
            } else {
                UpdateTeacher.text = "Update"
            }

            if (name.text.toString() != pName) {
                pName = name.text.toString()
                //update in database
                updateTeacherInformation("name", pName!!)

                //update in shared prefernce
                //preference.edit().putString(NAME, pName).apply()
            }
            if (subject.text.toString() != pSubject){
                pSubject=subject.text.toString()
                updateTeacherInformation("subject", pSubject!!)


               // preference.edit().putString(SUBJECT,pSubject).apply()

            }
            if (sTime.text.toString() != pSTime.toString()){
                pSTime=sTime.text.toString().toInt()
                updateTeacherInformation("startTime", pSTime!!.toString())


                //preference.edit().putInt(START_TIME, pSTime!!.toInt()).apply()
            }
            if (eTime.text.toString() !=pETime.toString()){
                pETime=eTime.text.toString().toInt()
                updateTeacherInformation("endTime", pETime!!.toString())

                //preference.edit().putInt(END_TIME,pETime!!.toInt()).apply()
            }
            if(major.text.toString()!=pMajor){
                pMajor=major.text.toString()
                updateTeacherInformation("major", pMajor!!)
                //preference.edit().putString(MAJOR,pMajor).apply()
            }



        }


    }

    fun updateTeacher() {
        upDateUserData.document(uId.toString())

            .update(
                "name", name.toString(),
                "subject", subject.toString(),
                "level", level.toString(),
                //"startTime", sTime,
               // "EndTime", eTime,
                "phoneNumber", phoneNumber.toString(),
                "major",major.toString()


                )
    }


    val uId = FirebaseAuth.getInstance().currentUser?.uid


    val upDateUserData = Firebase.firestore.collection("Teacher")

    fun updateTeacherInformation(field:String,value:String){
        db.collection("Teacher").document(uId!!).update(field, value)
            .addOnCompleteListener {
                it.addOnSuccessListener {
                    Log.e("updateTeacher", "success")
                }
                it.addOnFailureListener {
                    Log.e("updateTeacher ", it.message.toString())
                }
            }
    }

}

