package com.example.tinder20.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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

    var backPressedTime: Long = 0

    private lateinit var binding: ActivityMainPageBinding

    private val profileFragment = ProfileFragment()
    private val cardSelectionFragment = CardSelectionFragment()
    private val locationDatingFragment = LocationDatingFragment()
    private val chatFragment = ChatFragment()
    private val personalitySearchFragment = PersonalitySearchFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("TestTest", ".\nMainPageActivity - onCreate")

        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.statusBarColor = this.resources.getColor(R.color.softPurpleDark)

        makeCurrentFragment(cardSelectionFragment)

        val bottomNavigationMenu = binding.bottomNavigationMenu
        bottomNavigationMenu.selectedItemId = R.id.cardSelectionFragment
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

    private fun makeCurrentFragment(fragment: Fragment) {

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHolder, fragment)
            commit()
        }
    }

    override fun onBackPressed() {
        if (backPressedTime + 3000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        } else {
            Toast.makeText(this, "Press back again to leave the app.", Toast.LENGTH_LONG).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}
