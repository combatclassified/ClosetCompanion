package com.example.closetcompanion.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.closetcompanion.R
import com.example.closetcompanion.activities.HomePage
import com.example.closetcompanion.data.LoginWorker
import com.example.closetcompanion.data.WorkerKeys
import com.example.closetcompanion.models.User
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignUpFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignUpFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userNameEditText = view.findViewById<EditText>(R.id.signup_username_edit_text)
        val firstNameEditText = view.findViewById<EditText>(R.id.signup_first_name_edit_text)
        val lastNameEditText = view.findViewById<EditText>(R.id.signup_last_name_edit_text)
        val passwordEditText = view.findViewById<EditText>(R.id.password_edit_text)
        val emailAddressEditText = view.findViewById<EditText>(R.id.signup_email_address_edit_text)
        val birthdayEditText = view.findViewById<EditText>(R.id.signup_dob_edit_text)
        val progressbar = view.findViewById<ProgressBar>(R.id.signup_progressBar)

        view.findViewById<Button>(R.id.signup_back_button).setOnClickListener{
            switchToNewFragment(LandingFragment())
        }

        val workManager = WorkManager.getInstance(this.requireContext().applicationContext)

        val db = FirebaseFirestore.getInstance()

        view.findViewById<Button>(R.id.create_user_button).setOnClickListener {
            switchVisibility(progressbar)
            //Create a user object and store the object to the firebase.
            val newUser = User(
                username = userNameEditText.text.toString(),
                password = passwordEditText.text.toString(),
                first_name = firstNameEditText.text.toString(),
                last_name = lastNameEditText.text.toString(),
                email_address = emailAddressEditText.text.toString(),
                dob = birthdayEditText.text.toString()
            )
            db.collection("Users").add(newUser).addOnSuccessListener {
                //Creating a work Request and giving it a LoginWorker type to do the work.
                val loginRequest = OneTimeWorkRequestBuilder<LoginWorker>()
                    .setInputData(
                        Data.Builder()
                            .putString(WorkerKeys.USERNAME, newUser.username)
                            .putString(WorkerKeys.PASSWORD, newUser.password)
                            .build()
                    ).build()

                // Start the work in the back ground.
                workManager.beginUniqueWork("Login", ExistingWorkPolicy.KEEP, loginRequest).enqueue()

                workManager.getWorkInfoByIdLiveData(loginRequest.id)
                    .observe(this.viewLifecycleOwner) {
                        if(it != null && it.state.isFinished){
                            switchVisibility(progressbar)
                            val result = it.outputData.getString(WorkerKeys.CORRECT_PASSWORD).toString()
                            if(result.toBoolean()){
                                val intent = Intent(context, HomePage::class.java)
                                startActivity(intent)
                            }
                            else{
                                userNameEditText.error = "Check your username and try again."
                                passwordEditText.error = "Check your password and try again."
                                Toast.makeText(context, "Incorrect username or password.\nPlease try again.", Toast.LENGTH_LONG).show()
                            }
                        }
                    }

            }
        }
    }

    fun switchVisibility(progressBar: ProgressBar){
        if(progressBar.visibility == ProgressBar.INVISIBLE){
            progressBar.visibility = ProgressBar.VISIBLE
        }else{
            progressBar.visibility = ProgressBar.INVISIBLE
        }
    }

    fun switchToNewFragment(frag: Fragment){
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.landing_fragment_container, frag)
            commit()
        }
    }

    fun parseUserDocument(userDoc: DocumentSnapshot): User {
        return User(
            userDoc.getString("username").toString(),
            userDoc.getString("password").toString(),
            userDoc.getString("first_name").toString(),
            userDoc.getString("last_name").toString(),
            userDoc.getString("email_address").toString(),
            userDoc.getString("dob").toString()
        )
    }
}