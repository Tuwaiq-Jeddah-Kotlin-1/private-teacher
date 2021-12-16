package com.example.privateteacher.ui

import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.privateteacher.R
import com.example.privateteacher.model.Teacher
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.signup_page.*
import java.text.SimpleDateFormat

private const val TAG = "SignupTeacher"

class SignupTeacher : Fragment() {

    private lateinit var username: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var alreadyMember: TextView
    private lateinit var signupBtn: Button
    private lateinit var major: EditText
    private lateinit var subject: EditText
    private lateinit var level: EditText
    private lateinit var starTime: TextView
    private lateinit var sTime: ImageButton
    private lateinit var endTime: TextView
    private lateinit var eTime: ImageButton
    private var timeStart: Int = 0
    private var timeEnd: Int = 0
    private lateinit var phoneNumber: EditText
    private var db = FirebaseFirestore.getInstance()
    private val refAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.signup_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signupBtn = view.findViewById(R.id.btnSingUpTeacher)
        username = view.findViewById(R.id.TeacherUserName)
        email = view.findViewById(R.id.teacherEmail)
        password = view.findViewById(R.id.teacherPassword)
        major = view.findViewById(R.id.major)
        subject = view.findViewById(R.id.subject)
        level = view.findViewById(R.id.level)
        starTime = view.findViewById(R.id.startTime)
        endTime = view.findViewById(R.id.endTime)
        phoneNumber = view.findViewById(R.id.phoneNumber)
        sTime = view.findViewById(R.id.startIcon)
        eTime = view.findViewById(R.id.endIcon)
        alreadyMember = view.findViewById(R.id.alreadyTeacherMember)
        alreadyMember.setOnClickListener {
            findNavController().navigate(R.id.login)
        }

        signupBtn.setOnClickListener {
            if (timeEnd <= timeStart) {
                Toast.makeText(
                    requireContext(),
                    "end time must be bigger than start time",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                when {
                    TextUtils.isEmpty(email.text.toString().trim()) -> {
                        Toast.makeText(
                            context,
                            "Please Enter Email",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    TextUtils.isEmpty(password.text.toString().trim { it <= ' ' }) -> {
                        Toast.makeText(
                            context,
                            "Please Enter Password",
                            Toast.LENGTH_LONG
                        ).show()


                    }
                    TextUtils.isEmpty(username.text.toString().trim { it <= ' ' }) -> {
                        Toast.makeText(
                            context,
                            "Please Enter username",
                            Toast.LENGTH_LONG
                        ).show()


                    }
                    // if not empty
                    else -> {
                        refAuth.createUserWithEmailAndPassword(
                            email.text.toString().trim().lowercase(),
                            password.text.toString().trim()
                        ).addOnCompleteListener { register ->

                            // if the registration is successfully done
                            if (register.isSuccessful) {

                                Toast.makeText(
                                    context,
                                    "You were registered successfully",
                                    Toast.LENGTH_LONG
                                ).show()
                                // here the authentacition sucessed + generate UID

                                saveUserFireStore(
                                    username.text.toString(),
                                    email.text.toString(),
                                    major.text.toString(),
                                    subject.text.toString(),
                                    level.text.toString(),
                                    starTime.text.toString().toInt(),
                                    endTime.text.toString().toInt(),
                                    phoneNumber.text.toString(),

                                    )


                            } else {
                                // if the registration is not successful then show error massage
                                Log.e(TAG, register.exception!!.message.toString())
                                Toast.makeText(
                                    context,
                                    register.exception!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
            }


        }

        sTime.setOnClickListener {
            timePicker()
        }
        eTime.setOnClickListener {
            endTimePicker()
        }

    }


    private fun timePicker(): Int {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, _ ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            //set time to textView
            startTime.text = SimpleDateFormat("HH").format(cal.time)
            timeStart = hour// startTime.text.toString().toInt()
            Toast.makeText(requireContext(), timeStart.toString(), Toast.LENGTH_SHORT).show()

        }


        TimePickerDialog(
            requireContext(),
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()



        return timeStart
    }

    private fun endTimePicker(): Int {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, _ ->
            cal.set(Calendar.HOUR_OF_DAY, hour)

            //set time to textVive

            endTime.text = SimpleDateFormat("HH").format(cal.time)
            timeEnd = hour
            Toast.makeText(requireContext(), timeEnd.toString(), Toast.LENGTH_SHORT).show()

            // MIN: hours, minute, secconds
//            if (endTime.toString().toInt() >= starTime.toString().toInt()) {
//
//              Toast.makeText(requireContext(), "end time must be bigger than start time", Toast.LENGTH_SHORT).show()
//            }
        }


        TimePickerDialog(
            requireContext(),
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()

        return timeEnd

    }
    /* private fun saveData(username: String, email: String,major:String,subject:String,level:String,starrtTime:Int,endTime:Int,phoneNumber:String ) {
       *//*  if (endTime <= starrtTime) {

        Toast.makeText(requireContext(), "end time must be bigger than start time", Toast.LENGTH_SHORT).show()
         }else{*//*

            saveUserFireStore(teacher)

       // }

    }*/


    private fun saveUserFireStore(
        username: String,
        email: String,
        major: String,
        subject: String,
        level: String,
        starrtTime: Int,
        endTime: Int,
        phoneNumber: String
    ) {// = CoroutineScope(Dispatchers.IO).launch {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
//uid!!,
        val teacher = Teacher(
            teacherId = uid.toString(),
            name = username,
            email = email,
            major = major,
            subject = subject,
            level = level,
            startTime = starrtTime,
            endTime = endTime,
            phoneNumber = phoneNumber
        )

        try {
            db.collection("Teacher").document(uid!!).set(teacher).addOnSuccessListener {
                Toast.makeText(context, "Successfully saved data.", Toast.LENGTH_SHORT).show()

                findNavController().navigate(R.id.home_fragment)

            }

        } catch (e: Exception) {
            //withContext(Dispatchers.Main) {

            // Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            //  }
        }
    }


}




