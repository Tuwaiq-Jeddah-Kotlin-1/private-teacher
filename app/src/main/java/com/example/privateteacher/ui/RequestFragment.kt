package com.example.privateteacher.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.privateteacher.R
import com.example.privateteacher.model.Request
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*


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
        //FirebaseAuth.getInstance().currentUser?.email?.let { Log.e("userUID", it) }

        recyclerView = view.findViewById(R.id.recyclerRequest)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        requestList = arrayListOf()
        myRequestAdapter = RequestAdapter(requestList)
        recyclerView.adapter = myRequestAdapter
        eventChangeListener()
    }

    private fun eventChangeListener() {
        var uid=FirebaseAuth.getInstance().currentUser?.uid
        db = FirebaseFirestore.getInstance()

        db.collection("Request").whereEqualTo("teacherUid",uid.toString()).addSnapshotListener(object : EventListener<QuerySnapshot> {

            override fun onEvent(
                value: QuerySnapshot?,
                error: FirebaseFirestoreException?
            ) {
                if (error != null) {
                    Log.e("fireStore Error", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        requestList.add(dc.document.toObject(Request::class.java))
                    }
                }
                myRequestAdapter.notifyDataSetChanged()


            }

        })

    }

}