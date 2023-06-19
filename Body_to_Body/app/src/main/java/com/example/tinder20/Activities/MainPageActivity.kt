package com.example.tinder20.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tinder20.R
import com.example.tinder20.databinding.ActivityMainPageBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MainPageActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainPageBinding

    private lateinit var client: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSingOut.setOnClickListener {
            options?.let {
                client = GoogleSignIn.getClient(this, it)
            }
            if(client != null) {
                client.revokeAccess()
            }
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, RegistrationActivity::class.java).putExtra("AccountSingOut", true))
        }
    }
}
