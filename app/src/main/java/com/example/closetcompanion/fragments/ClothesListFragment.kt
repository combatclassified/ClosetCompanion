package com.example.closetcompanion.fragments

import AddFragment
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.closetcompanion.R
import com.example.closetcompanion.data.Closet
import com.example.closetcompanion.data.ClosetDao
import com.example.closetcompanion.data.ClosetDatabase
import com.example.closetcompanion.data.Clothes
import com.example.closetcompanion.data.ClothesDao
import com.example.closetcompanion.data.Outfit
import com.example.closetcompanion.data.OutfitDao
import com.example.closetcompanion.fragments.RecyclerView.ClothesAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var closetDatabase: ClosetDatabase
private lateinit var closetDao: ClosetDao
private lateinit var outfitsDao: OutfitDao
private lateinit var clothesDao: ClothesDao
private lateinit var recyclerView: RecyclerView

/**
 * A simple [Fragment] subclass.
 * Use the [ClothesListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClothesListFragment : Fragment() {
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

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_clothes_list, container, false)

        val outfit = arguments?.getSerializable("outfit") as? Outfit

        // Initialize database and DAO
        closetDatabase = ClosetDatabase.getDatabase(requireContext())
        outfitsDao = closetDatabase.outfitDao()
        closetDao = closetDatabase.closetDao()
        clothesDao = closetDatabase.clothesDao()

        // Set click listener for add closet button
        val addClothesButton = view.findViewById<FloatingActionButton>(R.id.add_outfit_button)
        addClothesButton.setOnClickListener {
            // Create an instance of the new fragment
            val addFragment = AddFragment()

            // Get the FragmentManager
            val fragmentManager = requireActivity().supportFragmentManager

            // Replace the current fragment with the new fragment
            fragmentManager.beginTransaction()
                .replace(R.id.home_page_fragment_container, addFragment)
                .commit()
        }
    if(outfit == null) {
        recyclerView = view.findViewById(R.id.outfit_recycler_view)

    var clothesList = mutableListOf<Clothes>()
    GlobalScope.launch {

        clothesList.addAll(clothesDao.getAllClothes())

        withContext(Dispatchers.Main) {
            recyclerView.adapter = ClothesAdapter(clothesList, requireContext())
        }

         }
        }
        else {

        recyclerView = view.findViewById(R.id.outfit_recycler_view)

        var clothesList = mutableListOf<Clothes>()
        GlobalScope.launch {

            outfit.clothesIds.forEach { oid ->
                clothesDao.getClothesById(oid)?.let { clothesList.add(it) }
            }
            withContext(Dispatchers.Main) {
                recyclerView.adapter = ClothesAdapter(clothesList, requireContext())
            }

        }

    }
        // Inflate the layout for this fragment
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ClothesListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ClothesListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}