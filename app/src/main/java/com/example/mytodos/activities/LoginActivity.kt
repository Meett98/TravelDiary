package com.example.mytodos.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mytodos.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)



        auth = Firebase.auth
        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }



        binding.btnLoginLG.setOnClickListener {
            val email = binding.emailLg.text.toString()
            val password = binding.passwordLg.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signInWithEmailAndPassword(email, password)
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnRegisterLG.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }


        //Using Shared Preference
//        val sharedPreference =getSharedPreferences("Login_Activity", Context.MODE_PRIVATE)
//        val getusername = sharedPreference.getString("USERNAME","")
//        val getpassword = sharedPreference.getString("PASSWORD","")
//        if(getusername != "" && getpassword != "")
//        {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.btnLogin.setOnClickListener{
//            val username = binding.usernameLg.text.toString()
//            val password = binding.passwordLg.text.toString()
//
//            if(username == "" || password == "")
//            {
//                Toast.makeText(this,"Please enter all the details",Toast.LENGTH_SHORT).show()
//            }
//            else {
//
//
//                val editor = sharedPreference.edit()
//                editor.putString("USERNAME", username)
//                editor.putString("PASSWORD", password)
//                editor.apply()
//
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//            }
//        }
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show()
                }
            }
    }

}