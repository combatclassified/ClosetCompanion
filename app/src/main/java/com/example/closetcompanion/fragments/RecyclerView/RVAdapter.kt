package com.example.closetcompanion.fragments.RecyclerView

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.closetcompanion.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class RVAdapter (private val dataList: List<closetItem>, var mContext: Context): RecyclerView.Adapter<RVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_design, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.title.text = "Clothing Item: " + item.title.toString()
        holder.type.text = "Clothing Type: " + item.type.toString()
        //var storageRef: StorageReference? = null
        var storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef?.child(item.image)
        imageRef?.metadata?.addOnSuccessListener { metadata ->
            // Image exists, download the URL and display in ImageView
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                Glide.with(holder.itemView.context)
                    .load(uri)
                    .into(holder.image)
            }
        }?.addOnFailureListener {
            // Image does not exist
        }
        //holder.image.setImageDrawable(item.image.drawable)

    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.Title)
        val type: TextView = itemView.findViewById(R.id.Type)
        val image: ImageView = itemView.findViewById(R.id.clothesPic)


        init {
            itemView.setOnClickListener {
                //Toast.makeText(itemView.context, "You have clicked ${dataList[adapterPosition]}", Toast.LENGTH_LONG).show()
                val intent = Intent(itemView.context, Details::class.java)
                intent.putExtra("thang", dataList[adapterPosition] as java.io.Serializable)
                mContext.startActivity(intent)
            }
        }
    }


}



