package com.example.tinder20.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tinder20.databinding.ActivityMainPageBinding

class MainPageActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSingOut.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java).putExtra("AccountSingOut", true))
        }
    }
}
