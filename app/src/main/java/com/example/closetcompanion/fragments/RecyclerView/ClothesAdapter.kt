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
import com.example.closetcompanion.data.Clothes
import com.example.closetcompanion.fragments.ClothesDetails
import com.example.closetcompanion.fragments.ClothesListFragment

class ClothesAdapter(
    private val clothesList: List<Clothes>,
    private val mContext: Context
) : RecyclerView.Adapter<ClothesAdapter.ViewHolder>() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.closet_item_design, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val clothes = clothesList[position]
        holder.clothesName.text = clothes.name
    }

    override fun getItemCount(): Int {
        return clothesList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val clothesName: TextView = itemView.findViewById(R.id.closetName)

        init {
            itemView.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    "You have clicked ${clothesList[adapterPosition].name}",
                    Toast.LENGTH_LONG
                ).show()
                val clothesDetails = ClothesDetails()
                val bundle = Bundle()
                bundle.putSerializable("clothes", clothesList[adapterPosition])
                clothesDetails.arguments = bundle
                val fragmentManager = (itemView.context as AppCompatActivity).supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.home_page_fragment_container, clothesDetails)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}
