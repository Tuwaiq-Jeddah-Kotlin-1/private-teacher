package com.example.privateteacher.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.privateteacher.R
import com.example.privateteacher.model.Teacher
import java.util.*
import kotlin.collections.ArrayList


class MyAdapter(private val teacherList:MutableList<Teacher>) :
    RecyclerView.Adapter<MyViewHolder>(), Filterable {
    var filterList = mutableListOf<Teacher>()

    init {

        teacherList.forEach {
           filterList .add(it)

        }
    }

    override fun getFilter(): Filter = object : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults? {
            val filteredList: MutableList<Teacher> = ArrayList()

            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(filterList)

            } else {
                val filterPattern = constraint.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }
                for (item in  filterList) {
                    if (item.subject.lowercase(Locale.getDefault()).contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            teacherList.clear()
            teacherList.addAll(results.values as List<Teacher>)
            notifyDataSetChanged()
        }
    }
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


