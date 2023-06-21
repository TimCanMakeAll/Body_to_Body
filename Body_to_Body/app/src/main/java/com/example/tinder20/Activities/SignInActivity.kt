package com.example.tinder20.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.graphics.toColor
import com.example.tinder20.R
import com.example.tinder20.databinding.ActivitySignInBinding
import com.example.tinder20.functions.hashString
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignInActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.statusBarColor = this.resources.getColor(R.color.pinkPurpleDeep)

        mAuth = FirebaseAuth.getInstance()

        binding.btnConfirmSignIn.setOnClickListener {
            Log.d("TestTest", "btnConfirmSingIn was clicked")
            val dbEmail = binding.etEmail.text.toString().trim()
            val dbPassword = binding.etPassword.text.toString().trim()
            val hashedPassword = hashString(dbPassword)
            if (dbEmail.isEmpty()) {
                Log.d("TestTest", "email is empty, enter email")
                binding.layoutEmailInput.helperText = "You should enter email!"
                binding.layoutEmailInput.setHelperTextColor(this.getColorStateList(R.color.redError))
                return@setOnClickListener
            } else {
                binding.layoutEmailInput.isHelperTextEnabled = false
                if (dbPassword.isEmpty()) {
                    Log.d("TestTest", "password is empty, enter password")
                    binding.layoutPasswordInput.helperText = "You should enter password!"
                    binding.layoutPasswordInput.setHelperTextColor(this.getColorStateList(R.color.redError))
                    return@setOnClickListener
                }
                if (dbPassword.length < 8) {
                    Log.d("TestTest", "password should be 8+ numbers, enter password")
                    binding.layoutPasswordInput.helperText = "8+ symbols!"
                    binding.layoutPasswordInput.setHelperTextColor(this.getColorStateList(R.color.redError))
                    return@setOnClickListener
                }
            }
            binding.layoutPasswordInput.isHelperTextEnabled = false

            mAuth.signInWithEmailAndPassword(dbEmail, hashedPassword)
                .addOnSuccessListener {
                    // Login successful
                    Log.d("TestTest", "This email registered in app")
                    binding.layoutEmailInput.helperText = "This email registered in app"
                    startActivity(Intent(this, MainPageActivity::class.java))
                }
                .addOnFailureListener {
                    //Login failed
                    Log.d("TestTest", "This e-mail not registered yet")
                    binding.layoutEmailInput.helperText = "This user does not exist! You can go back and register this Email"
                    binding.layoutEmailInput.setHelperTextColor(this.getColorStateList(R.color.redError))
                }
            binding.layoutEmailInput.isHelperTextEnabled = false
        }

        binding.btnBackToRegistration.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }
}