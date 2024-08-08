package com.example.closetcompanion.fragments.RecyclerView

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.closetcompanion.R
import com.google.firebase.firestore.FirebaseFirestore

class Details: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details2)
        var thang: closetItem = intent.extras?.get("thang") as closetItem
        val uTitle = findViewById<TextView>(R.id.UTitle)
        val uType = findViewById<TextView>(R.id.UType)
        val uColor = findViewById<TextView>(R.id.UColor)
        val uStatus = findViewById<TextView>(R.id.UStatus)
        val uUser = findViewById<TextView>(R.id.UUser)
        val uSize = findViewById<TextView>(R.id.USize)
        //val completed = findViewById<TextView>(R.id.USize)

        uTitle.text = ("Name: " + thang.title.toString())
        uType.text = ("Type: " + thang.type.toString())
        uColor.text = ("Color: " + thang.color)
        uStatus.text = ("Status: " + thang.status)
        uUser.text = ("User: " + thang.user)
        uSize.text = ("Color: " + thang.size)
        //completed.text = ("Completed: " + thang.completed.toString())



    }
}