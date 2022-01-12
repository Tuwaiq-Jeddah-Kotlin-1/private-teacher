package com.example.privateteacher

import android.app.TimePickerDialog
import android.content.Context
import android.icu.number.NumberFormatter
import android.icu.text.DateTimePatternGenerator
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.privateteacher.ui.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.signup_page.*
import java.text.SimpleDateFormat


class UpdateTeacher : Fragment() {
    private lateinit var db: FirebaseFirestore
    lateinit var Update: Button
    lateinit var name: EditText
    private lateinit var subject: Spinner
    private lateinit var level: Spinner
    private lateinit var statTime: TextView
    private lateinit var endTime: TextView
    private lateinit var major: EditText
    private lateinit var phoneNumber: EditText
    private lateinit var sTime: ImageButton
    private lateinit var eTime: ImageButton
    private var subjectList: List<String> = listOf(
        "MATH",
        "The holy Quran and its recitation",
        "Islamic studies",
        "Social studies",
        "Arabic",
        "English",
        "Science",
        "Biology",
        "physics",
        "digital technology"
    )
    private var levelList: List<String> = listOf("primary", "middle", "high")

    // HERE SAVE THE VALUE
    private var timeStart: Long = 0
    private var timeEnd: Long = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_up_date__teatchr, container, false)
    }

    fun updateTeacherInformation(field: String, value: Any) {
        var type: String
        if (!isTeacher) {
            type = "Student"
        } else {
            type = "Teacher"
        }
        db.collection(type).document(uId!!).update(field, value)
            .addOnCompleteListener {
                it.addOnSuccessListener {
                    Log.e("updateUser", "success $field $value")
                }
                it.addOnFailureListener {
                    Log.e("updateUser", it.message.toString() +" $field")
                }
            }
    }

    fun timePicker() {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, _ ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            //set time to textView
            startTime.text = SimpleDateFormat("HH").format(cal.time)
            timeStart = hour.toLong()// startTime.text.toString().toInt()
            Toast.makeText(requireContext(), timeStart.toString(), Toast.LENGTH_SHORT).show()
        }
        TimePickerDialog(
            requireContext(),
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    fun endTimePicker() {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, _ ->
            cal.set(Calendar.HOUR_OF_DAY, hour)

            //set time to textVive

            endTime.text = SimpleDateFormat("HH").format(cal.time)
            timeEnd = hour.toLong()
            Toast.makeText(requireContext(), timeEnd.toString(), Toast.LENGTH_SHORT).show()

        }
        TimePickerDialog(
            requireContext(),
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    val uId = FirebaseAuth.getInstance().currentUser?.uid
    val upDateUserData = Firebase.firestore.collection("Teacher")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Update = view.findViewById(R.id.UpdateTeacher)
        name = view.findViewById(R.id.TeacherUserName)
        subject = view.findViewById(R.id.subject)
        level = view.findViewById(R.id.uplevel)
        statTime = view.findViewById(R.id.startTime)
        endTime = view.findViewById(R.id.endTime)
        sTime = view.findViewById(R.id.startIcon)
        eTime = view.findViewById(R.id.endIcon)
        phoneNumber = view.findViewById(R.id.phoneNumber)
        major = view.findViewById(R.id.major)
        db = FirebaseFirestore.getInstance()

        val preference = requireContext().getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        var pName = preference.getString(NAME, "")
        var pSubject = preference.getString(SUBJECT, "")
        var pLevel = preference.getString(LEVEL, "")
        var pSTime = preference.getInt(START_TIME, 0)
        var pETime = preference.getInt(END_TIME, 0)
        var pPhoneNumber = preference.getString(PHONE_NUMBER, "")
        var pMajor = preference.getString(MAJOR, "")
        var pType = preference.getString(TYPE, "")


        if (!isTeacher) {
            // level.gravity= Gravity.CENTER_HORIZONTAL
            // level.width =ViewGroup.LayoutParams.WRAP_CONTENT
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                weight = 1.0f
                gravity = Gravity.CENTER
            }
            subject.visibility = View.GONE
            statTime.visibility = View.GONE
            endTime.visibility = View.GONE
            phoneNumber.visibility = View.GONE
            major.visibility = View.GONE
            sTime.visibility = View.GONE
            eTime.visibility = View.GONE
        }

        name.setText(pName)
        val indexOfPS = subjectList.indexOf(pSubject)
        subject.setSelection(indexOfPS)

        val indexofP = levelList.indexOf(pLevel)
        level.setSelection(indexofP)

        statTime.text = pSTime.toString()
        endTime.text = pETime.toString()
        phoneNumber.setText(pPhoneNumber)
        major.setText(pMajor)
//
        name.isEnabled = false
        subject.isEnabled = false
        level.isEnabled = false
        statTime.isEnabled = false
        endTime.isEnabled = false
        phoneNumber.isEnabled = false
        major.isEnabled = false


        Update.setOnClickListener {
            name.isEnabled = true
            subject.isEnabled = true
            level.isEnabled = true
            statTime.isEnabled = true
            endTime.isEnabled = true
            phoneNumber.isEnabled = true
            major.isEnabled = true

            if (name.isEnabled) {
                Update.text = "Submit"
            } else {
                Update.text = "Update"
            }

            if (name.text.toString() != pName) {
                pName = name.text.toString()
                //update in database
                updateTeacherInformation("name", pName!!)
                //update in shared prefernce
                preference.edit().putString(NAME, pName).apply()
            }

            if (level.selectedItem.toString() != pLevel) {
                 pLevel = levelList[level.selectedItemPosition]
                updateTeacherInformation("level", pLevel!!)
                preference.edit().putString(LEVEL, pLevel).apply()
            }
            if (isTeacher) {
                if (subject.selectedItem.toString() != pSubject) {
                    pSubject = subject.selectedItem.toString()
                    updateTeacherInformation("subject", pSubject!!)

                    preference.edit().putString(SUBJECT, pSubject).apply()

                }
                if (statTime.text.toString() != pSTime.toString()) {
                    if (timeEnd <= timeStart) {
                        Toast.makeText(
                            requireContext(),
                            "end time must be bigger than start time",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {

                        Log.e("sTime", "${statTime.text.toString()} GDFGFG")
                        pSTime = statTime.text.toString().toInt()
                        updateTeacherInformation("startTime", pSTime)
                        preference.edit().putInt(START_TIME, pSTime!!.toInt()).apply()
                    }

                }
                if (endTime.text.toString() != pETime.toString()) {
                    if (timeEnd <= timeStart) {
                        Toast.makeText(
                            requireContext(),
                            "end time must be bigger than start time",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {

                        pETime = endTime.text.toString().toInt()
                        updateTeacherInformation("endTime", pETime)

                        preference.edit().putInt(END_TIME, pETime!!.toInt()).apply()
                    }
                }
                if (major.text.toString() != pMajor) {
                    pMajor = major.text.toString()
                    updateTeacherInformation("major", pMajor!!)
                    preference.edit().putString(MAJOR, pMajor).apply()
                }

                if (phoneNumber.text.toString() != pPhoneNumber) {
                    pPhoneNumber = phoneNumber.text.toString()
                    updateTeacherInformation("phoneNumber", pPhoneNumber!!)
                    preference.edit().putString(PHONE_NUMBER, pPhoneNumber).apply()
                }

                fun timePicker() {
                    val cal = Calendar.getInstance()
                    val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, _ ->
                        cal.set(Calendar.HOUR_OF_DAY, hour)
                        //set time to textView
                        startTime.text = SimpleDateFormat("HH").format(cal.time)
                        timeStart = hour.toLong()// startTime.text.toString().toInt()
                        Toast.makeText(requireContext(), timeStart.toString(), Toast.LENGTH_SHORT)
                            .show()

                    }
                    TimePickerDialog(
                        requireContext(),
                        timeSetListener,
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        true
                    ).show()
                }

                sTime.setOnClickListener {
                    timePicker()
                }
                eTime.setOnClickListener {
                    endTimePicker()
                }
            }
        }
        fun endTimePicker() {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, _ ->
                cal.set(Calendar.HOUR_OF_DAY, hour)

                //set time to textVive

                endTime.text = SimpleDateFormat("HH").format(cal.time)
                timeEnd = hour.toLong()
                Toast.makeText(requireContext(), timeEnd.toString(), Toast.LENGTH_SHORT).show()

            }


            TimePickerDialog(
                requireContext(),
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()


        }

        fun updateTeacher() {
            upDateUserData.document(uId.toString())

                .update(
                    "name", name.toString(),
                    "subject", subject.toString(),
                    "level", level.toString(),
                    "startTime", startTime.text.toString(),
                    "endTime", endTime.text.toString().toInt(),
                    "phoneNumber", phoneNumber.toString().toInt(),
                    "major", major.toString()
                )
        }
    }
}


