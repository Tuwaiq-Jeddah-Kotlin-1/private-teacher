package com.example.privateteacher.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.privateteacher.R
import com.example.privateteacher.model.Teacher


class MyAdapter(private val teacherList: ArrayList<Teacher>) :
    RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val teacher: Teacher = teacherList[position]
        holder.name.text = teacher.name
        holder.subject.text = teacher.subject

        holder.itemView.setOnClickListener {

            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(teacher)
            it.findNavController().navigate(action)
        }
    }


    override fun getItemCount(): Int {
        return teacherList.size
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val name: TextView = itemView.findViewById(R.id.name)
    val subject: TextView = itemView.findViewById(R.id.subject)


}


