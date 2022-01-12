package com.example.privateteacher.ui

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.privateteacher.R
import com.example.privateteacher.model.Request
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore




class RequestAdapter(private val requestList: ArrayList<Request>) :
    RecyclerView.Adapter<MyHolder>() {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    private var db = FirebaseFirestore.getInstance()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val itemView =

            LayoutInflater.from(parent.context).inflate(R.layout.item_request, parent, false)
        return MyHolder(itemView)
    }


    override fun getItemCount(): Int {
        return requestList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val request: Request = requestList[position]
        holder.subject.text = request.subject
        holder.timeval.text = request.timeVal.toString()
        holder.stateText.text = request.state
        holder.idRequest=request.idrequest
        changeColorState(holder,"Pending")

       if (isTeacher){
           if(request.state == "Pending"){
               holder.acceptButton.visibility = View.VISIBLE
               holder.rejectButton.visibility = View.VISIBLE
               holder.acceptButton.setOnClickListener {
                   db.collection("Request").document(request.idrequest).update("state", "accept")
                       .addOnCompleteListener {
                           it.addOnSuccessListener {
                               Log.e("adapter", "success")
                               changeColorState(holder,"accept")

                           }
                           it.addOnFailureListener {
                               Log.e("adapter ${request.idrequest}", it.message.toString())
                           }
                       }
                   request.state ="accept"
                   notifyDataSetChanged()
               }
               holder.rejectButton.setOnClickListener {
                   db.collection("Request").document(request.idrequest).update("state", "reject")
                       .addOnCompleteListener {
                           it.addOnSuccessListener {
                               Log.e("adapter", "success")
                               changeColorState(holder,"reject")
                           }
                           it.addOnFailureListener {
                               Log.e("adapter ${request.idrequest}", it.message.toString())
                           }
                       }
                   request.state ="reject"
                   notifyDataSetChanged()
               }

           }else{
               holder.acceptButton.visibility = View.GONE

               holder.rejectButton.visibility = View.GONE
               changeColorState(holder,request.state)
           }

       }else{

           holder.deletRequst.visibility=View.VISIBLE
           holder.acceptButton.visibility = View.GONE
           holder.rejectButton.visibility = View.GONE
           changeColorState(holder,request.state)
       }
        holder.deletRequst.setOnClickListener {
            val deletRequest=db.collection("Request").document(request.idrequest).delete()
            requestList.removeAt(position)
            notifyDataSetChanged()
        }
    }

    fun changeColorState(holder: MyHolder, state:String){
        holder.acceptButton.visibility = View.GONE
        holder.rejectButton.visibility = View.GONE
        if (state == "Pending") {

            val unwrappedDrawable: Drawable = holder.state.getBackground()
            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable)
            DrawableCompat.setTint(wrappedDrawable, Color.YELLOW)
        }
            if (state == "accept"){

            val unwrappedDrawable: Drawable = holder.state.getBackground()
            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable)
            DrawableCompat.setTint(wrappedDrawable, Color.GREEN)

        }else if (state=="reject"){

            val unwrappedDrawable: Drawable = holder.state.getBackground()
            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable)
            DrawableCompat.setTint(wrappedDrawable, Color.RED)
        }
    }
}

class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val subject: TextView = itemView.findViewById(R.id.subject)
    val timeval: TextView = itemView.findViewById(R.id.time)
    val state: TextView = itemView.findViewById(R.id.circle_image)
    val stateText: TextView = itemView.findViewById(R.id.stateText)
    var acceptButton: Button = itemView.findViewById(R.id.acceptBtn)
    var rejectButton: Button = itemView.findViewById(R.id.rejectBtn)
    var deletRequst:ImageView=itemView.findViewById(R.id.deletRequest)
    lateinit var idRequest:String
}
