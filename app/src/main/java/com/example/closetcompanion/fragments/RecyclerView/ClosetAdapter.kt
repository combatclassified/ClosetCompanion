package com.example.closetcompanion.fragments.RecyclerView

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.closetcompanion.R
import com.example.closetcompanion.data.Closet

class ClosetAdapter(
    private val closetList: List<Closet>,
    private val mContext: Context
) : RecyclerView.Adapter<ClosetAdapter.ViewHolder>() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.closet_item_design, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val closet = closetList[position]
        holder.closetName.text = closet.name
    }

    override fun getItemCount(): Int {
        return closetList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val closetName: TextView = itemView.findViewById(R.id.closetName)

        init {
            itemView.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    "You have clicked ${closetList[adapterPosition].name}",
                    Toast.LENGTH_LONG
                ).show()
                val closetListFrag = ClosetListFragment()
                val bundle = Bundle()
                bundle.putSerializable("closet", closetList[adapterPosition])
                closetListFrag.arguments = bundle
                val fragmentManager = (itemView.context as AppCompatActivity).supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.home_page_fragment_container, closetListFrag)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}
