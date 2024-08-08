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
import androidx.work.*
import com.example.closetcompanion.R
import com.example.closetcompanion.activities.HomePage
import com.example.closetcompanion.data.LoginWorker
import com.example.closetcompanion.data.WorkerKeys
import com.example.closetcompanion.models.User
import com.google.firebase.firestore.DocumentSnapshot

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userNameEditText = view.findViewById<EditText>(R.id.login_username_edit_text)
        val passwordEditText = view.findViewById<EditText>(R.id.login_password_edit_text)
        val progressbar = view.findViewById<ProgressBar>(R.id.login_progress_bar)

        view.findViewById<Button>(R.id.login_back_button).setOnClickListener{
            switchToNewFragment(LandingFragment())
        }

        val workManager = WorkManager.getInstance(this.requireContext().applicationContext)

        //This is the listener that is activated when a user tries to log in.
        view.findViewById<Button>(R.id.login_to_landing_button).setOnClickListener {
            userNameEditText.error = null
            passwordEditText.error = null
            switchVisibility(progressbar)

            //Get the Text the inside the fields.
            val username = userNameEditText.text.toString()
            val password = passwordEditText.text.toString()

            //Creating a work Request and giving it a LoginWorker type to do the work.
            val loginRequest = OneTimeWorkRequestBuilder<LoginWorker>()
                .setInputData(
                    Data.Builder()
                        .putString(WorkerKeys.USERNAME, username)
                        .putString(WorkerKeys.PASSWORD, password)
                        .build()
                ).build()

            // Start the work in the back ground.
            workManager.beginUniqueWork("Login", ExistingWorkPolicy.KEEP, loginRequest).enqueue()

            // Use Live Data to check to observe the workInfo object that is associated with the
            // workRequest. We also check to make sure that the workInfo is in the Finished state.
            // before we compare the input data against the database data.
            workManager.getWorkInfoByIdLiveData(loginRequest.id)
                .observe(this.viewLifecycleOwner) {
                    if(it != null && it.state.isFinished){
                        switchVisibility(progressbar)
                        val result = it.outputData.getString(WorkerKeys.CORRECT_PASSWORD).toString()

                        if(result.toBoolean()){
                            val intent = Intent(context, HomePage::class.java)
                            val user = User(it.outputData.getString(WorkerKeys.USERNAME).toString(),
                                it.outputData.getString(WorkerKeys.PASSWORD).toString(),
                                it.outputData.getString(WorkerKeys.FIRST_NAME).toString(),
                                it.outputData.getString(WorkerKeys.LAST_NAME).toString(),
                                it.outputData.getString(WorkerKeys.EMAIL).toString(),
                                it.outputData.getString(WorkerKeys.DOB).toString())
                            intent.putExtra("user", user)
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

    fun parseUserDocument(map: MutableMap<String, String>): User {
        return User(
            map["username"].toString(),
            map["password"].toString(),
            map["first_name"].toString(),
            map["last_name"].toString(),
            map["email_address"].toString(),
            map["dob"].toString()
        )
    }
}