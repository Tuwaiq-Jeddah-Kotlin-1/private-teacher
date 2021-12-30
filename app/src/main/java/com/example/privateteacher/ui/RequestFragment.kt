package com.example.privateteacher.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.privateteacher.R
import com.example.privateteacher.model.Request
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RequestFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var requestList: ArrayList<Request>
    private lateinit var myRequestAdapter: RequestAdapter

    private lateinit var db: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_request2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerRequest)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        requestList = arrayListOf()
        myRequestAdapter = RequestAdapter(requestList)
        recyclerView.adapter = myRequestAdapter
        val userID = FirebaseAuth.getInstance().currentUser?.uid
        cheakStudentOrTeacher(userID.toString())
    }

    fun cheakStudentOrTeacher(userID: String) = CoroutineScope(Dispatchers.IO).launch {
        //shared preference
       lateinit var level: Spinner
       lateinit var name: TextView
          val preference = requireContext().getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)

      try {
         val db = FirebaseFirestore.getInstance()
           db.collection("Teacher")
               .document("$userID")
               .get().addOnCompleteListener {
                   it
                    if (it.result?.exists()!!) {//    com.google.android.gms.tasks.RuntimeExecutionException: com.google.firebase.firestore.FirebaseFirestoreException: Failed to get document because the client is offline.

                        requestTeacher()
                      preference.edit().putString(TYPE,TEACHER).apply()
                   } else {
                        requestStudent()
                       preference.edit().putString(TYPE,STUDENT).apply()
                 }
               }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.e("FUNCTION createUserFire", "${e.message}")
            }
        }
    }
    private fun requestStudent() {
        var uid = FirebaseAuth.getInstance().currentUser?.uid
        db = FirebaseFirestore.getInstance()
        db.collection("Request").whereEqualTo("studentUid", uid.toString())
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error != null) {
                        Log.e("fireStore Error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED ) {
                            requestList.add(dc.document.toObject(Request::class.java))
                            Log.e("Error","${requestList.size}")
                            //send netification
                            PrivateTeacherNotification().myNotification("your request is accepted")

                        }
                    }
                    myRequestAdapter.notifyDataSetChanged()


                }

            })

    }
    private fun requestTeacher() {
        var uid = FirebaseAuth.getInstance().currentUser?.uid
        db = FirebaseFirestore.getInstance()

        db.collection("Request").whereEqualTo("teacherUid", uid.toString())
            .addSnapshotListener(object : EventListener<QuerySnapshot> {

                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error != null) {
                        Log.e("fireStore Error", error.message.toString())
                        return
                    }

                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED ) {
                            requestList.add(dc.document.toObject(Request::class.java))
                            Log.e("Error","${requestList.size}")

                            //send netification
                            PrivateTeacherNotification().myNotification("you have new request")





                        }
                       else if ( dc.type == DocumentChange.Type.REMOVED) {
                            requestList.add(dc.document.toObject(Request::class.java))
                            Log.e("Error", "${requestList.size}")
                            //send netification
                            PrivateTeacherNotification().myNotification("your request is delete")
                        }
                    }

                    myRequestAdapter.notifyDataSetChanged()


                }

            })

    }

}