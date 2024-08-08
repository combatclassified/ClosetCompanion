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
import com.example.closetcompanion.fragments.ClothesListFragment
import com.example.closetcompanion.fragments.closetsFragment
import com.example.closetcompanion.models.Item
import com.example.closetcompanion.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.UUID

class AddFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var button: Button
    private lateinit var closetDatabase: ClosetDatabase
    private lateinit var clothesDao: ClothesDao
    private var imageUri: Uri? = null

    companion object {
        private const val PICK_IMAGE = 100
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        imageView = view.findViewById(R.id.imageView)
        button = view.findViewById(R.id.saveButton)

        imageView.setOnClickListener {
            permissionRequest()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val homeActivity = requireActivity() as HomePage
        val userr = homeActivity.user
        val email = userr!!.email_address
        if (userr != null) {
           Log.d(TAG, "lmao")
        }
        else
            Log.d(TAG, "lmawo")

        //gets the thing for the thang
        val nameText = view.findViewById<EditText>(R.id.nameInput)
        val typeText = view.findViewById<EditText>(R.id.typeInput)
        val sizeText = view.findViewById<EditText>(R.id.sizeInput)
        val colorText = view.findViewById<EditText>(R.id.colorInput)
        val statusText = view.findViewById<EditText>(R.id.statusInput)
        val saveB = view.findViewById<Button>(R.id.saveButton)
        val shareB = view.findViewById<Button>(R.id.shareButton)

        // Get the Firebase Storage and Firestore instances
        val picture = FirebaseStorage.getInstance()
        val db = FirebaseFirestore.getInstance()

        // Initialize closetDatabase and closetDao
        closetDatabase = ClosetDatabase.getDatabase(requireContext())
        clothesDao = closetDatabase.clothesDao()

        saveB.setOnClickListener {
            // Convert the image URI to a Bitmap
            val uri = Uri.parse(imageUri.toString())
            val imageBitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, uri)

            // Convert the Bitmap to a ByteArray
            val outputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            val byteArray = outputStream.toByteArray()

            val name = nameText.text.toString()
            val type = typeText.text.toString()
            val size = sizeText.text.toString()
            val color = colorText.text.toString()
            val status = statusText.text.toString()

            // Insert the image data into Room
            GlobalScope.launch {
                clothesDao.insertClothes(Clothes(imageData = byteArray, name = name, type = type, size = size, color = color, status = status))

                val clothesList = clothesDao.getAllClothes()
                clothesList.forEach { clothes ->
                    println("Clothes id: ${clothes.id}")
                    println("Clothes name: ${clothes.name}")
                    println("Clothes type: ${clothes.type}")
                    println("Clothes size: ${clothes.size}")
                    println("Clothes color: ${clothes.color}")
                    println("Clothes status: ${clothes.status}")
                }
            }

            // Create an instance of the new fragment
            val clothesFragment = ClothesListFragment()

            // Get the FragmentManager
            val fragmentManager = requireActivity().supportFragmentManager

            // Replace the current fragment with the new fragment
            fragmentManager.beginTransaction()
                .replace(R.id.home_page_fragment_container, clothesFragment)
                .commit()

        }

        shareB.setOnClickListener {

            // Create a reference to the image file in Firebase Storage
            val timestamp = System.currentTimeMillis()
            val randomString = UUID.randomUUID().toString()
            val imageUrl = "$email/items/$timestamp-$randomString.jpg"
            val imageRef = picture.reference.child(imageUrl)

            // Upload the image file to Firebase Storage
            imageUri?.let { it1 ->
                imageRef.putFile(it1)
                    .addOnSuccessListener {
                        // Get the download URL for the image
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            // Create a new document in Firestore with the image URL and the 5 strings

                            val newItem = Item(
                                name = nameText.text.toString(),
                                type = typeText.text.toString(),
                                size = sizeText.text.toString(),
                                color = colorText.text.toString(),
                                status = statusText.text.toString(),
                                image = imageUrl,
                                user = userr!!.username
                            )
                            if (userr != null) {
                                db.collection("Items").document(userr.email_address).collection("Closet").document()
                                    .set(newItem)
                            }

                        }
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error uploading image", e)
                    }
            }
        }
    }

    private fun permissionRequest(){
        var permissionList = mutableListOf<String>()
        if(!(ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){
            permissionList.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        else
        {
            openGallery()
        }
        if(permissionList.isNotEmpty()){
            ActivityCompat.requestPermissions(requireActivity(), permissionList.toTypedArray(), 100)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            100 -> {
                for(index in grantResults.indices) {
                    if (grantResults[index] == PackageManager.PERMISSION_GRANTED)
                        openGallery()
                }
            }
        }
    }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }
}
