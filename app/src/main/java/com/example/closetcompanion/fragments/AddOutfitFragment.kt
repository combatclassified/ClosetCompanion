import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import com.example.closetcompanion.R
import com.example.closetcompanion.activities.HomePage
import com.example.closetcompanion.data.*
import com.example.closetcompanion.fragments.RecyclerView.OutfitListFragment
import com.example.closetcompanion.fragments.closetsFragment
import com.example.closetcompanion.models.Item
import com.example.closetcompanion.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.UUID

class AddOutfitFragment : Fragment() {

    private lateinit var closetDatabase: ClosetDatabase
    private lateinit var outfitDao: OutfitDao
    private lateinit var clothesDao: ClothesDao



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_outfit, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //gets the thing for the thang
        val nameText = view.findViewById<EditText>(R.id.nameInput2)
        val clothesText = view.findViewById<EditText>(R.id.clothesInput)
        val saveB = view.findViewById<Button>(R.id.saveButton)

        // Initialize closetDatabase and closetDao
        closetDatabase = ClosetDatabase.getDatabase(requireContext())
        outfitDao = closetDatabase.outfitDao()
        clothesDao = closetDatabase.clothesDao()

        saveB.setOnClickListener {

            val name = nameText.text.toString()
            val clothes = clothesText.text.toString()

            val clothesList = clothes.split(",").map { it.trim() }
            val clothesIds = mutableListOf<Int>()

            // Insert the image data into Room
            GlobalScope.launch {

                clothesList.forEach { cname ->
                    val clothes = clothesDao.getClothesByName(cname)
                    if (clothes != null) {
                        println("cname: " + cname + "/ id: " + clothes.id)
                        clothesIds.add(clothes.id)
                    }
                }
                println(clothesIds)
                outfitDao.insertOutfit(Outfit( name = name, clothesIds = clothesIds))

                val outfitList = outfitDao.getAllOutfits()
                outfitList.forEach { outfit ->
                    println("outfit id: ${outfit.id}")
                    println("outfit name: ${outfit.name}")
                    outfit.clothesIds.forEach { cid ->
                        println("clothes id: ${cid}")
                    }
                }
            }
            // Create an instance of the new fragment
            val outfitFragment = OutfitListFragment()

            // Get the FragmentManager
            val fragmentManager = requireActivity().supportFragmentManager

            // Replace the current fragment with the new fragment
            fragmentManager.beginTransaction()
                .replace(R.id.home_page_fragment_container, outfitFragment)
                .commit()

        }


    }
}
