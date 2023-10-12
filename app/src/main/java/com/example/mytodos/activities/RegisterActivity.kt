package com.example.mytodos.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mytodos.R
import com.example.mytodos.databinding.ActivityLoginBinding
import com.example.mytodos.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreference =getSharedPreferences("Register_Activity", Context.MODE_PRIVATE)
        //val getpassword = sharedPreference.getString("PASSWORD","")
        auth = Firebase.auth
        binding.btnRegister.setOnClickListener {
            val username = binding.usernameLg.text.toString()
            val password = binding.passwordLg.text.toString()
            val email = binding.emailLg.text.toString()
            if (username == "" || password == "" || email == "") {
                Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show()
            }
            else if(username.length >17){
                Toast.makeText(this, "Your Username is too large..", Toast.LENGTH_SHORT).show()

            }
            else {
                val editor = sharedPreference.edit()
                editor.putString("PASSWORD", password)
                editor.apply()
                performRegister(username,email,password)
            }
        }
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performRegister(username : String,email:String,password:String) {



        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(username)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { innerTask ->
                            if (innerTask.isSuccessful) {
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this, "Failed to update username.", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    // Login failed, display an error message
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}