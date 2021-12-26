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
import com.example.privateteacher.model.Teacher
import com.google.firebase.firestore.*

private const val TAG = "HomeFragment"
class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var teacherList: ArrayList<Teacher>
    //
//private lateinit var search: SearchView
  //  private lateinit var myAdapter: MyAdapter
    private lateinit var db: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //search=view.findViewById(R.id.search)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        teacherList = arrayListOf()
            recyclerView.adapter=myAdapter

        eventChangeListener()
        //search view
//        search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                myAdapter.filter.filter(newText)
//                return false
//            }
//
//        })






    }


    private fun eventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("Teacher").addSnapshotListener(object : EventListener<QuerySnapshot> {
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
                        val teacher = Teacher()
                      val data = dc.document.data
//                        teacher.startTime = data["startTime"]
                       // Log.d(TAG, "onEvent: ${data["startTime"] as Long}")
                       // teacher.startTime = dc.document.getString("startTime")?.toInt()!!
                       teacherList.add(dc.document.toObject(Teacher::class.java))
                    }
                }
              myAdapter.notifyDataSetChanged()
            }

        })

    }

}