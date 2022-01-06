package com.example.privateteacher.ui

import android.content.Context
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "SignupStudent"

class SignupStudent : Fragment() {

    private lateinit var name: EditText
    private lateinit var emaile: EditText
    private lateinit var password: EditText
    private lateinit var level:Spinner
    private lateinit var alreadyMember: TextView
    private lateinit var signupBtn: Button
    private var db = FirebaseFirestore.getInstance()
    private val refAuth = FirebaseAuth.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sign_up_student, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        signupBtn = view.findViewById(R.id.btnSingupStudent)
        name = view.findViewById(R.id.studentName)
        emaile = view.findViewById(R.id.teacherEmail)
        password = view.findViewById(R.id.teacherPassword)
        level=view.findViewById(R.id.level)
        alreadyMember = view.findViewById(R.id.alreadyTeacherMember)
        // have account
        alreadyMember.setOnClickListener {
            findNavController().navigate(R.id.login)
        }

        signupBtn.setOnClickListener {

            when {
                TextUtils.isEmpty(emaile.text.toString().trim()) -> {
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
                TextUtils.isEmpty(level.selectedItem.toString())->{
                    Toast.makeText(
                        context,
                        "choose level",
                    Toast.LENGTH_LONG
                    ).show()
                }
                TextUtils.isEmpty(name.text.toString())->{
                    Toast.makeText(
                        context,
                        "enter your name",
                        Toast.LENGTH_LONG
                    ).show()
                }

                // if not empty
                else -> {
                    refAuth.createUserWithEmailAndPassword(
                        emaile.text.toString().trim().lowercase(),
                        password.text.toString().trim(),



                    ).addOnCompleteListener { register ->

                        // if the registration is successfully done
                        if (register.isSuccessful) {
                            Toast.makeText(
                                context,
                                "You were registered successfully",
                                Toast.LENGTH_LONG
                            ).show()
                            // here the authentacition sucessed + generate UID
                            saveData(
                                name.text.toString(),
                                emaile.text.toString(),
                                level.selectedItem.toString()
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

    private fun saveData(name: String, emaile: String,level:String) {
        val student = Student(name = name, email = emaile,level=level)
        val preference = requireContext().getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        preference.edit().putString(NAME, name).putString(LEVEL, level).apply()

        saveUserFireStore(student)

    }

    private fun saveUserFireStore(student: Student) {// = CoroutineScope(Dispatchers.IO).launch {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        try {
            db.collection("Student").document("$uid").set(student).addOnSuccessListener {
                Toast.makeText(context, "Successfully saved data.", Toast.LENGTH_SHORT).show()




                findNavController().navigate(R.id.home_fragment)
            }

        } catch (e: Exception) {
            //withContext(Dispatchers.Main) {
            Log.e("Exception", e.localizedMessage)

        }
    }
}
