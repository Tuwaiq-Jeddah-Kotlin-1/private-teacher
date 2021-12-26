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
    RecyclerView.Adapter<MyViewHolder>(){
//    var teacherFilterList = teacherList
//
//    override fun getFilter(): Filter = object : Filter(){
//        override fun performFiltering(constraint: CharSequence?): FilterResults? {
//            val filteredList: MutableList<Teacher> = ArrayList()
//            if (constraint == null || constraint.length == 0) {
//                filteredList.addAll(teacherList)
//            } else {
//                val filterPattern = constraint.toString().toLowerCase().trim { it <= ' ' }
//                for (item in teacherList) {
//                    if (item.subject.toLowerCase().contains(filterPattern)||item.level.toLowerCase().contains(filterPattern)) {
//                        filteredList.add(item)
//                    }
//                }
//            }
//            val results = FilterResults()
//            results.values = filteredList
//            return results
//        }
//
//        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
//            teacherFilterList.clear()
//            teacherFilterList.addAll(results.values as List<Teacher>)
//            notifyDataSetChanged()
//        }
//    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
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


