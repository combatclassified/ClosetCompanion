package com.example.closetcompanion.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.closetcompanion.R


class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed(Runnable {
            val i = Intent(this@SplashScreen, LandingPage::class.java)
            startActivity(i)
            finish()
        }, 2000) // 2000 milliseconds = 2 seconds


    }
}