package com.example.privateteacher.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.privateteacher.R
import com.example.privateteacher.ViewModel
import com.example.privateteacher.model.Request
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
//const val TOPIC="/topic/myTopic"
class DetailFragment : Fragment() {
    //

    val TAG="DetailFragment"

    private lateinit var name: TextView
    private lateinit var subject: TextView
    private lateinit var startTime: TextView
    private lateinit var endTime: TextView
    private lateinit var scheduleBtn: Button
    private lateinit var whatsBtn: ImageButton
    private lateinit var major: TextView
    private var timeVal: Int = 0
    private lateinit var spinner: Spinner
    private var endTimeInt: Int = 0
    var startTimeInt: Int = 0
    private var db = FirebaseFirestore.getInstance()
    private var availableTimes: MutableList<Int> = mutableListOf()
    private var teacherUid: String? = null

    private val refAuth = FirebaseAuth.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainView = ViewModelProvider(this).get(ViewModel::class.java)
        val args: DetailFragmentArgs by navArgs()
        val receivedData = args.teacherKeyArg
        name = view.findViewById(R.id.name)
        subject = view.findViewById(R.id.subject)
        startTime = view.findViewById(R.id.startTime)
        endTime = view.findViewById(R.id.endTime)
        scheduleBtn = view.findViewById(R.id.schedulebtn)
        whatsBtn = view.findViewById(R.id.watsbtn)
        major = view.findViewById(R.id.major)
        spinner = view.findViewById(R.id.from)





        name.text = receivedData.name
        teacherUid = receivedData.teacherId
        subject.text = receivedData.subject
        startTime.text = receivedData.startTime.toString()
        endTime.text = receivedData.endTime.toString()
        major.text = receivedData.major
        fillTimeList()
        // showSpinner(availableTimes)

        spinner.visibility=View.GONE

        whatsBtn.setOnClickListener {

            val number = 1234
            val url = "https://api.whatsapp.com/send?phone=$number"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        scheduleBtn.setOnClickListener {




            val dialog = AlertDialog.Builder(requireContext())//.setView(R.layout.spinner_dialog)
            val viewSpinner = layoutInflater.inflate(R.layout.spinner_dialog, null)
            dialog.setTitle(R.string.choose_time)


            val timeSpinner = viewSpinner.findViewById<Spinner>(R.id.dialog_spinner)

            val dataAdapter: ArrayAdapter<Int> = ArrayAdapter<Int>(
                requireContext(),
                android.R.layout.simple_spinner_item, availableTimes
            )
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spinner.adapter = dataAdapter
            timeSpinner.adapter = dataAdapter
            dialog.setPositiveButton(
                R.string.request,

                DialogInterface.OnClickListener { dialogInterface, i ->
                    timeVal = spinner.selectedItem.toString().toInt()

                    saveData(
                        name.text.toString(),
                        subject.text.toString(),
                        timeVal,
                        teacherUid!!,
                        refAuth.currentUser!!.uid

                    )
                }

            )
            dialog.setNegativeButton(
                R.string.cancel,
                DialogInterface.OnClickListener { dialogInterface, i ->

                })
            dialog.setView(viewSpinner)
            dialog.create().show()

        }


    }

    private fun fillTimeList() {
        startTimeInt = startTime.text.toString().toInt()

        endTimeInt = endTime.text.toString().toInt()

        for (i in startTimeInt until endTimeInt) {
            availableTimes.add(i)
        }
        Toast.makeText(requireContext(), availableTimes.toString(), Toast.LENGTH_LONG).show()

    }

    private fun saveData(
        name: String,
        subject: String,
        timeVal: Int,
        teacherUid: String,
        studentUid: String
    ) {
        val request = Request(

            subject = subject,
            timeVal = timeVal,
            teacherUid = teacherUid,
            studentUid = studentUid,
        )

        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val formatted = current.format(formatter)
        val studentId=FirebaseAuth.getInstance().currentUser?.uid
        request.idrequest="${studentUid}${formatted}"
        saveUserFireStore(request)

    }

    private fun saveUserFireStore(request: Request) {
        try {

            db.collection("Request").document(request.idrequest).set(request).addOnSuccessListener {


                Toast.makeText(context, "Successfully saved data.", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.requestFragment)
            }

        } catch (e: Exception) {
            Log.e("Exception", e.localizedMessage)

        }
    }

}

