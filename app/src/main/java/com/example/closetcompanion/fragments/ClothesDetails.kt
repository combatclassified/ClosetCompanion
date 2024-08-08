package com.example.closetcompanion.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.closetcompanion.R
import com.example.closetcompanion.data.Clothes

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ClothesDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClothesDetails : Fragment() {
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
    ): View? {
        val view = inflater.inflate(R.layout.fragment_clothes_details, container, false)
        val bundle = arguments
        val clothes = bundle?.getSerializable("clothes") as Clothes
        // Use the clothes object here

        //get the shid for the sharts
        var title = view.findViewById<TextView>(R.id.titleTextView)

        var pic = view.findViewById<ImageView>(R.id.imageView)
        val picData = clothes.imageData
        val clothesBitmap = BitmapFactory.decodeByteArray(picData, 0, picData.size)
        pic.setImageBitmap(clothesBitmap)

        var thang1 = view.findViewById<TextView>(R.id.textView1)
        var thang2 = view.findViewById<TextView>(R.id.textView2)
        var thang3 = view.findViewById<TextView>(R.id.textView3)
        var thang4 = view.findViewById<TextView>(R.id.textView4)

        title.text = clothes.name
        thang1.text = clothes.type
        thang2.text = clothes.size
        thang3.text = clothes.color
        thang4.text = clothes.status

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ClothesDetails.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ClothesDetails().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}