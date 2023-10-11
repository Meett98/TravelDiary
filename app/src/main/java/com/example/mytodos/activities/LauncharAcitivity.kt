package com.example.mytodos.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.mytodos.R
import com.google.firebase.auth.FirebaseAuth

class LauncharAcitivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launchar_acitivity)
        auth = FirebaseAuth.getInstance()


        Handler(Looper.getMainLooper()).postDelayed({
            val currentUser = auth.currentUser

            if (currentUser != null) {
                // User is already logged in, redirect to MainActivity
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // User is not logged in, redirect to LoginActivity
                startActivity(Intent(this, RegisterActivity::class.java))
            }

            finish() // Close the launcher activity


        }, 2000)
    }
}