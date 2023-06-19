package com.example.tinder20.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.tinder20.Fragments.CardSelectionFragment
import com.example.tinder20.Fragments.ChatFragment
import com.example.tinder20.Fragments.LocationDatingFragment
import com.example.tinder20.Fragments.PersonalitySearchFragment
import com.example.tinder20.Fragments.ProfileFragment
import com.example.tinder20.R
import com.example.tinder20.databinding.ActivityMainPageBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MainPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainPageBinding

    private val profileFragment = ProfileFragment()
    private val cardSelectionFragment = CardSelectionFragment()
    private val locationDatingFragment = LocationDatingFragment()
    private val chatFragment = ChatFragment()
    private val personalitySearchFragment = PersonalitySearchFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationMenu = binding.bottomNavigationMenu
        bottomNavigationMenu.selectedItemId = com.example.tinder20.R.id.cardSelectionFragment
        bottomNavigationMenu.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.cardSelectionFragment -> {
                    makeCurrentFragment(cardSelectionFragment)
                }

                R.id.profileFragment -> {
                    makeCurrentFragment(profileFragment)
                }

                R.id.locationDateFragment -> {
                    makeCurrentFragment(locationDatingFragment)
                }

                R.id.chatFragment -> {
                    makeCurrentFragment(chatFragment)
                }

                R.id.personalitySearchFragment -> {
                    makeCurrentFragment(personalitySearchFragment)
                }
            }
            true
        }
    }

    private fun makeCurrentFragment(fragment: Fragment){

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHolder, fragment)
            commit()
        }
    }
}
