package com.example.closetcompanion.activities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.example.closetcompanion.R
import com.example.closetcompanion.fragments.FeedFragment
import com.example.closetcompanion.fragments.ProfileFragment
import com.example.closetcompanion.fragments.ClosetFragment
import com.example.closetcompanion.models.User
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomePage : AppCompatActivity() {

    var user: User? = null
    var image: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(intent.hasExtra("user")){
            user = intent.extras?.get("user") as User
            //println(user)
            switchToNewFragmentWithUser("profile", user)
        }
        val fragContainer = findViewById<FragmentContainerView>(R.id.home_page_fragment_container)
        setContentView(R.layout.activity_home_page)

        findViewById<BottomNavigationView>(R.id.home_page_bottom_nav).setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.ic_profile -> {
                    switchToNewFragment(ProfileFragment())
                    switchToNewFragmentWithUser("profile", user)
                    true
                }
                R.id.ic_feed -> {
                    switchToNewFragment(FeedFragment())
                    true
                }
                R.id.ic_closet -> {
                    switchToNewFragment(ClosetFragment())
                    true
                }
                else -> false
            }
        }
    }

    fun switchToNewFragmentWithUser(frag: String, user: User?) {
        val bundle = Bundle().apply {
        putParcelable("user", user)
        }
        when(frag){
            "profile" -> {
                val profileFragment = ProfileFragment().apply {
                    arguments = bundle
                }
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.home_page_fragment_container, profileFragment)
                transaction.commit()
            }
            "feed" -> {
                val feedFragment = FeedFragment()
                feedFragment.arguments = bundle
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.home_page_fragment_container, feedFragment)
                transaction.commit()
            }
            "closet" -> {
                val closetFragment = ClosetFragment()
                closetFragment.arguments = bundle
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.home_page_fragment_container, closetFragment)
                transaction.commit()
            }
        }


    }

    fun switchToNewFragment(frag: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.home_page_fragment_container, frag)
            commit()
        }
    }
}