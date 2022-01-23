package com.example.privateteacher.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.privateteacher.R
import com.example.privateteacher.model.Teacher
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

private const val TAG = "HomeFragment"
class HomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var teacherList: ArrayList<Teacher>
    private lateinit var myAdapter: MyAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var search:androidx.appcompat.widget.SearchView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        search=view.findViewById(R.id.search)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        eventChangeListener()
        search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                myAdapter.filter.filter(newText)
                return false
            }
        })
    }

    private fun eventChangeListener() {
        db = FirebaseFirestore.getInstance()
        val preference = requireContext().getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        var pLevel = preference.getString(LEVEL, "")
        db.collection("Teacher").whereEqualTo("level", pLevel.toString()).addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(
                value: QuerySnapshot?,
                error: FirebaseFirestoreException?
            ) {
                if (error != null) {
                    Log.e("fireStore Error", error.message.toString())
                    return
                }
                if (value != null) {
                    myAdapter = MyAdapter(value.toObjects(Teacher::class.java))
                    recyclerView.adapter = myAdapter
                }
            }
        })
    }
}
