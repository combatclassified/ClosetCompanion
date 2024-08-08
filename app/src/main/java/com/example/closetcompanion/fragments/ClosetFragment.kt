package com.example.closetcompanion.fragments

import AddClosetFragment
import AddFragment
import AddOutfitFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.closetcompanion.R
import com.example.closetcompanion.data.Outfit
import com.example.closetcompanion.fragments.RecyclerView.OutfitListFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ClosetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClosetFragment : Fragment() {
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

        val view = inflater.inflate(R.layout.fragment_closet, container, false)

        // Find the buttons in the layout
        val addButton = view.findViewById<Button>(R.id.bAdd)
        val closetButton = view.findViewById<Button>(R.id.bCloset)
        val clothesButton = view.findViewById<Button>(R.id.bClothes)
        val outfitButton = view.findViewById<Button>(R.id.bOutfit)

        addButton.setOnClickListener {
            // Create an instance of the new fragment
            val addFragment = AddFragment()

            // Get the FragmentManager
            val fragmentManager = requireActivity().supportFragmentManager

            // Replace the current fragment with the new fragment
            fragmentManager.beginTransaction()
                .replace(R.id.home_page_fragment_container, addFragment)
                .commit()
        }

        clothesButton.setOnClickListener {
            // Create an instance of the new fragment
            val clothesFragment = ClothesListFragment()

            // Get the FragmentManager
            val fragmentManager = requireActivity().supportFragmentManager

            // Replace the current fragment with the new fragment
            fragmentManager.beginTransaction()
                .replace(R.id.home_page_fragment_container, clothesFragment)
                .commit()
        }

        outfitButton.setOnClickListener {
            // Create an instance of the new fragment
            val outfitsFragment = OutfitListFragment()

            // Get the FragmentManager
            val fragmentManager = requireActivity().supportFragmentManager

            // Replace the current fragment with the new fragment
            fragmentManager.beginTransaction()
                .replace(R.id.home_page_fragment_container, outfitsFragment)
                .commit()
        }

        closetButton.setOnClickListener {
            // Create an instance of the new fragment
            val closetFragment = closetsFragment()

            // Get the FragmentManager
            val fragmentManager = requireActivity().supportFragmentManager

            // Replace the current fragment with the new fragment
            fragmentManager.beginTransaction()
                .replace(R.id.home_page_fragment_container, closetFragment)
                .commit()
        }

        return view
        //
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingsFragement.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ClosetFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}