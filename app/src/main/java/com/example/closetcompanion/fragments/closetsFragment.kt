package com.example.closetcompanion.fragments

import AddClosetFragment
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
import com.example.closetcompanion.data.ClothesDao
import com.example.closetcompanion.fragments.RecyclerView.ClosetAdapter
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
private lateinit var clothesDao: ClothesDao
private lateinit var recyclerView: RecyclerView
//private lateinit var adapter: ClosetsAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [closetsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class closetsFragment : Fragment() {
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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_closets, container, false)

        // Initialize database and DAO
        closetDatabase = ClosetDatabase.getDatabase(requireContext())
        clothesDao = closetDatabase.clothesDao()
        closetDao = closetDatabase.closetDao()

        recyclerView = view.findViewById(R.id.outfit_recycler_view)

        var closetList = mutableListOf<Closet>()
        GlobalScope.launch {
            closetList.addAll(closetDao.getAllClosets())
            withContext(Dispatchers.Main) {
                recyclerView.adapter = ClosetAdapter(closetList, requireContext())
            }
        }

        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize database and DAO
        closetDatabase = ClosetDatabase.getDatabase(requireContext())
        clothesDao = closetDatabase.clothesDao()
        closetDao = closetDatabase.closetDao()

        recyclerView = view.findViewById(R.id.outfit_recycler_view)


        var closetList = mutableListOf<Closet>()
        GlobalScope.launch {
            closetList.addAll(closetDao.getAllClosets())
            withContext(Dispatchers.Main) {
                recyclerView.adapter = ClosetAdapter(closetList, requireContext())
            }
        }

        // Set click listener for add closet button
        val addClosetButton = view.findViewById<FloatingActionButton>(R.id.add_closet_button)
        addClosetButton.setOnClickListener {
            // Create an instance of the new fragment
            val addFragment = AddClosetFragment()

            // Get the FragmentManager
            val fragmentManager = requireActivity().supportFragmentManager

            // Replace the current fragment with the new fragment
            fragmentManager.beginTransaction()
                .replace(R.id.home_page_fragment_container, addFragment)
                .commit()
            }
        }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment closetsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            closetsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}