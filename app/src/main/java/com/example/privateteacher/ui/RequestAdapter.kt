package com.example.privateteacher.ui
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.Button
    import android.widget.TextView
    import androidx.recyclerview.widget.RecyclerView
    import com.example.privateteacher.R
    import com.example.privateteacher.model.Request
    import com.google.firebase.auth.FirebaseAuth

class RequestAdapter(private val requestList: ArrayList<Request>) :
        RecyclerView.Adapter<MyHolder>() {
        val userId=FirebaseAuth.getInstance().currentUser?.uid



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
            holder.subject.text =request.subject
            holder.timeval.text= request.timeVal.toString()
            holder.state.text=request.state
            holder.idUser=request.teacherUid
            if (userId==request.teacherUid){
               holder.acceptButton.visibility = View.VISIBLE
                holder.rejectButton.visibility = View.VISIBLE
}else{
                holder.acceptButton.visibility = View.GONE
                holder.rejectButton.visibility = View.GONE

}


}
}

class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
val subject : TextView = itemView.findViewById(R.id.subject)
val timeval: TextView = itemView.findViewById(R.id.time)
val state:TextView=itemView.findViewById(R.id.circle_image)
lateinit var idUser:String
var acceptButton:Button=itemView.findViewById(R.id.acceptBtn)
var rejectButton:Button=itemView.findViewById(R.id.rejectBtn)


}
