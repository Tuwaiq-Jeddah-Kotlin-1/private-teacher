package com.example.privateteacher.ui

import android.content.Context
import android.content.SharedPreferences
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
import com.example.privateteacher.model.Student
import com.example.privateteacher.model.Teacher
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.*

var isTeacher: Boolean = false

class Login : Fragment() {
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var signupTeacher: TextView
    private lateinit var signupStudent: TextView
    private lateinit var btnLogin: Button
    private lateinit var myShared: SharedPreferences
    var isRemember = false
    private lateinit var remember: CheckBox


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.login_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        email = view.findViewById(R.id.teacherEmail)
        password = view.findViewById(R.id.teacherPassword)
        btnLogin = view.findViewById(R.id.btnLogin)
        signupTeacher = view.findViewById(R.id.gotoTeacherRegister)
        signupStudent = view.findViewById(R.id.gotoStudentRegister)
        remember = view.findViewById(R.id.check_rem)
        signupTeacher.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_signupTeacher)
        }
        signupStudent.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_signupStudent)
        }
        myShared = requireContext().getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        isRemember = myShared.getBoolean("CHECKBOX", false)
        val tech = myShared.getString(TYPE, STUDENT)
        if (isRemember) {
            if (tech == STUDENT) {
                isTeacher = false
                findNavController().navigate(R.id.action_login_to_home_fragment2)
            } else {
                isTeacher = true
                findNavController().navigate(R.id.action_login_to_requestFragment)
            }
        }
        btnLogin.setOnClickListener {

            when {
                TextUtils.isEmpty(email.text.toString().trim { it <= ' ' }) -> {
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
                else -> {
                    val email: String = email.text.toString().trim { it <= ' ' }
                    val password: String = password.text.toString().trim { it <= ' ' }

                    // create an instance and create a register with email and passwords
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->

                            // if the registration is sucessfully done
                            if (task.isSuccessful) {
                                //firebase register user
                                val firebaseUser: FirebaseUser = task.result!!.user!!

                                Toast.makeText(
                                    requireContext(),
                                    "Welcome",
                                    Toast.LENGTH_LONG
                                ).show()
                                val editor: SharedPreferences.Editor = myShared.edit()
                                val emilData = email
                                val passwordPref = password
                                val check: Boolean = remember.isChecked
                                editor.putString(EMAIL, emilData)
                                editor.putBoolean("CHECKBOX", check)
                                editor.apply()
                                Toast.makeText(context, "Information Saved!", Toast.LENGTH_LONG)
                                    .show()
                                val userID = FirebaseAuth.getInstance().currentUser?.uid
                                cheakStudentOrTeacher(userID.toString())
//                                findNavController().navigate(R.id.action_login_to_home_fragment2)
                            } else {
                                // if the registreation is not succsesful then show error massage
                                Toast.makeText(
                                    requireContext(),
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                }
            }
        }
    }

    fun cheakStudentOrTeacher(userID: String) = CoroutineScope(Dispatchers.IO).launch {
        val editor: SharedPreferences.Editor = myShared.edit()

        try {
            val db = FirebaseFirestore.getInstance()
            db.collection("Teacher")
                .document("$userID")
                .get().addOnCompleteListener {
                    if (it.result?.exists()!!) {

                   //     Toast.makeText(context, "i am teacher", Toast.LENGTH_SHORT).show()
                        editor.putString(TYPE, TEACHER).apply()
                        db.collection("Teacher")
                            .document("$userID")
                            .get().addOnSuccessListener {
                                val teacher = it.toObject<Teacher>()
                                teacher?.let {
                                    editor.putString(NAME, teacher.name)
                                        .putString(SUBJECT, teacher.subject).putString(
                                        MAJOR, teacher.major
                                    ).putString(LEVEL, teacher.level)
                                        .putInt(START_TIME, teacher.startTime.toInt()).putInt(
                                        END_TIME, teacher.endTime.toInt()
                                    ).putString(PHONE_NUMBER, teacher.phoneNumber).apply()
                                }
                            }

                        isTeacher = true
                        findNavController().navigate(R.id.action_login_to_requestFragment)
                    } else {
                       // Toast.makeText(context, "i am student", Toast.LENGTH_SHORT).show()
                        editor.putString(TYPE, STUDENT).apply()
                        db.collection("Student")
                            .document("$userID")
                            .get().addOnSuccessListener {
                                val student = it.toObject<Student>()
                                student?.let {
                                    editor.putString(NAME, student.name).putString(LEVEL, student.level).apply()
                                }
                            }
                        isTeacher = false
                        findNavController().navigate(R.id.action_login_to_home_fragment2)
                    }


                }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.e("FUNCTION createUserFire", "${e.message}")
            }
        }


    }

}


