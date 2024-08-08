package com.example.closetcompanion.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.closetcompanion.R
import com.example.closetcompanion.R.id
import com.example.closetcompanion.fragments.RecyclerView.RVAdapter
import com.example.closetcompanion.fragments.RecyclerView.closetItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FeedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FeedFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_feed, container, false)
        val thang = mutableListOf<closetItem>()



        var storageRef: StorageReference? = null
        storageRef = FirebaseStorage.getInstance().reference

        val db = FirebaseFirestore.getInstance()
        db.collection("Items/" + "Imurphy92064@gmail.com/" + "Closet")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    var docNum = 1
                    var count = 0
                    var count2 = 0
                    for (document in it.result!!)
                        count = count + 1
                    while (docNum <= count) {
                        count2 = count2 + 1
                        for (document in it.result!!) {
                            val nameTitle = document.data.getValue("name").toString()
                            val nameColor = document.data.getValue("color").toString()
                            val nameType = document.data.getValue("type").toString()
                            val nameSize = document.data.getValue("size").toString()
                            val nameStatus = document.data.getValue("status").toString()
                            val nameUser = document.data.getValue("user").toString()
                            val imageRef = document.data.getValue("image").toString()
                            //val complete: Boolean = tComplete == "true"
                            //if (tID.toString() == count2.toString()) {
                            val temp = closetItem(nameTitle, nameType, nameColor, nameSize, nameStatus, nameUser, imageRef)
                            thang.add(temp)
                            docNum += 1
                            //}
                        }
                    }
                    // }
                    val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
                    recyclerView.adapter = context?.let { it1 -> RVAdapter(thang, it1) }
                    //}
                    /*while (thang.size > 0) {
                        thang.removeAt(thang.size - 1)
                    }*/


                    //return inflater.inflate(R.layout.fragment_feed, container, false)
                }
            }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FeedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FeedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}