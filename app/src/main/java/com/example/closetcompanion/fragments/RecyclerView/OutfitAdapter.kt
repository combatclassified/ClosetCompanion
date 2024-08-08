package com.example.closetcompanion.fragments.RecyclerView

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.closetcompanion.R
import com.example.closetcompanion.data.Outfit
import com.example.closetcompanion.fragments.ClothesListFragment

class OutfitAdapter(
    private val outfitList: List<Outfit>,
    private val mContext: Context
) : RecyclerView.Adapter<OutfitAdapter.ViewHolder>() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.closet_item_design, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val outfit = outfitList[position]
        holder.outfitName.text = outfit.name
    }

    override fun getItemCount(): Int {
        return outfitList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val outfitName: TextView = itemView.findViewById(R.id.closetName)

        init {
            itemView.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    "You have clicked ${outfitList[adapterPosition].name}",
                    Toast.LENGTH_LONG
                ).show()
                val outfitListFrag = ClothesListFragment()
                val bundle = Bundle()
                bundle.putSerializable("outfit", outfitList[adapterPosition])
                outfitListFrag.arguments = bundle
                val fragmentManager = (itemView.context as AppCompatActivity).supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.home_page_fragment_container, outfitListFrag)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}