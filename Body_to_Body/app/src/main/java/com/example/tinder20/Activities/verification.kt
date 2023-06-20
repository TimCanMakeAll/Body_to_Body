package com.example.tinder20.Activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tinder20.R
import com.example.tinder20.databinding.ActivityRegistrationBinding
import com.example.tinder20.databinding.ActivityVerificationBinding
import com.google.firebase.auth.FirebaseAuth

class verification : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var binding: ActivityVerificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        binding.btnVerification.setOnClickListener{
            val user = mAuth.currentUser
            user?.reload()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val updatedUser = mAuth.currentUser
                    if (updatedUser != null && updatedUser.isEmailVerified) {
                        Log.d("TestTest", "User email verified")
                        startActivity(Intent(this, MainPageActivity::class.java))
                    } else {
                        Log.d("TestTest", "User email not verified")
                    }
                } else {
                    Log.d("TestTest", "User email verification error")
                }
            }
        }

        binding.btnBackToRegistration.setOnClickListener{
            mAuth.signOut()
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }
}