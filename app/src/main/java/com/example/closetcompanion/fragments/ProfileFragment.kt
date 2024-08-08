package com.example.closetcompanion.fragments

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.closetcompanion.R
import com.example.closetcompanion.models.User
import androidx.core.content.ContextCompat
import com.example.closetcompanion.activities.HomePage
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: User? = null
    private var param2: String? = null
    private lateinit var profileView: ImageView

    //private val homeActivity = requireActivity() as HomePage
    //private val user = homeActivity.user

    private lateinit var profileImage: ImageView
    private var storageRef: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val bundle = arguments
            param1 = bundle?.getParcelable<User>("user")
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val bundle = arguments

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)


        profileImage = view.findViewById(R.id.profile_picture_image)
        storageRef = FirebaseStorage.getInstance().reference

        // Load the user's profile image if it exists in Firebase storage
        loadProfileImage()

        // Set up click listener for the profile image
        profileImage.setOnClickListener { chooseImage() }

        return view
    }


    private fun chooseImage() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 2)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            chooseImage()
        }
    }

    private fun loadProfileImage() {
        val imageRef = storageRef?.child("${param1?.email_address}/profile_picture/${param1?.email_address}-profile-pic.jpg")
        imageRef?.metadata?.addOnSuccessListener { metadata ->
            // Image exists, download the URL and display in ImageView
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                Glide.with(this).load(uri).into(profileImage)
            }
        }?.addOnFailureListener {
            // Image does not exist
        }
    }

    // Handle the result of the gallery intent
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri = data.data
            if (imageUri != null) {
                val imageName = "${param1?.email_address}-profile-pic.jpg"
                val imageRef = storageRef?.child("${param1?.email_address}/profile_picture/$imageName")
                val uploadTask = imageRef?.putFile(imageUri)
                uploadTask?.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let { throw it }
                    }
                    imageRef?.downloadUrl
                }?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        loadProfileImage()
                    }
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val usernameTextField = view.findViewById<TextView>(R.id.username_text_field)
        val nameTextField = view.findViewById<TextView>(R.id.name_text_field)
        val emailTextField = view.findViewById<TextView>(R.id.email_text_field)
        val dobTextField = view.findViewById<TextView>(R.id.dob_text_field)

        val name = param1?.first_name + " " + param1?.last_name
        usernameTextField.text = param1?.username
        nameTextField.text = name
        emailTextField.text = param1?.email_address
        dobTextField.text =  param1?.dob
    }
}